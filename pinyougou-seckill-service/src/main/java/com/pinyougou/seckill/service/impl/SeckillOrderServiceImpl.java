package com.pinyougou.seckill.service.impl;

import java.util.Date;
import java.util.List;

import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.pojo.SeckillGoods;
import com.pinyougou.seckill.pojo.SeckillStatus;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.seckill.thread.CreateOrderThread;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;
import com.pinyougou.mapper.SeckillOrderMapper;
import com.pinyougou.pojo.SeckillOrder;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SeckillOrderServiceImpl extends CoreServiceImpl<SeckillOrder> implements SeckillOrderService {

    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private CreateOrderThread orderHandler;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    public SeckillOrderServiceImpl(SeckillOrderMapper seckillOrderMapper) {
        super(seckillOrderMapper, SeckillOrder.class);
        this.seckillOrderMapper = seckillOrderMapper;
    }

    @Override
    public PageInfo<SeckillOrder> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<SeckillOrder> all = seckillOrderMapper.selectAll();
        return new PageInfo<>(all);
    }

    @Override
    public PageInfo<SeckillOrder> findPage(Integer pageNo, Integer pageSize, SeckillOrder seckillOrder) {
        PageHelper.startPage(pageNo, pageSize);
        Example example = new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();

        if (seckillOrder != null) {
            if (StringUtils.isNotBlank(seckillOrder.getUserId())) {
                criteria.andLike("userId", "%" + seckillOrder.getUserId() + "%");
            }
            if (StringUtils.isNotBlank(seckillOrder.getSellerId())) {
                criteria.andLike("sellerId", "%" + seckillOrder.getSellerId() + "%");
            }
            if (StringUtils.isNotBlank(seckillOrder.getStatus())) {
                criteria.andLike("status", "%" + seckillOrder.getStatus() + "%");
            }
            if (StringUtils.isNotBlank(seckillOrder.getReceiverAddress())) {
                criteria.andLike("receiverAddress", "%" + seckillOrder.getReceiverAddress() + "%");
            }
            if (StringUtils.isNotBlank(seckillOrder.getReceiverMobile())) {
                criteria.andLike("receiverMobile", "%" + seckillOrder.getReceiverMobile() + "%");
            }
            if (StringUtils.isNotBlank(seckillOrder.getReceiver())) {
                criteria.andLike("receiver", "%" + seckillOrder.getReceiver() + "%");
            }
            if (StringUtils.isNotBlank(seckillOrder.getTransactionId())) {
                criteria.andLike("transactionId", "%" + seckillOrder.getTransactionId() + "%");
            }
        }
        List<SeckillOrder> all = seckillOrderMapper.selectByExample(example);
        return new PageInfo<>(all);
    }

    @Override
    public void submitOrder(Long seckillId, String userId) {
        //1.根据ID 从redis中获取秒杀商品
        SeckillGoods tbSeckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).get(seckillId);
        //先判断 用户是否已经在排队中  如果 在,提示已在排队中
        Object o2 = redisTemplate.boundHashOps(SysConstants.SEC_USER_QUEUE_FLAG_KEY).get(userId);
        if (o2 != null) {
            throw new RuntimeException("排队中");
        }
        //先判断 是否有未支付的订单
        Object o1 = redisTemplate.boundHashOps(SysConstants.SEC_KILL_ORDER).get(userId);
        if (o1 != null) {
            throw new RuntimeException("有未支付的订单");
        }
        //2.判断商品是否已经售罄 如果卖完了 抛出异常
        Object o = redisTemplate.boundListOps(SysConstants.SEC_KILL_GOODS_PREFIX + seckillId).rightPop();
        if (o == null) {
            throw new RuntimeException("卖完了");
        }
        //压入队列
        redisTemplate.boundListOps(SysConstants.SEC_KILL_USER_ORDER_LIST).leftPush(new SeckillStatus(userId, seckillId, SeckillStatus.SECKILL_queuing));
        //此时 应该 表示用户只能进入队列中,不能保证一定创建订单成功.
        //标记 用户已经进入排队中
        redisTemplate.boundHashOps(SysConstants.SEC_USER_QUEUE_FLAG_KEY).put(userId, seckillId);
        //调用多线程
        orderHandler.handleOrder();

    }

    @Override
    public SeckillOrder getUserOrderStatus(String userId) {
        return (SeckillOrder) redisTemplate.boundHashOps(SysConstants.SEC_KILL_ORDER).get(userId);
    }

    @Override
    public void updateOrderStatus(String transaction_id, String userId) {
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(SysConstants.SEC_KILL_ORDER).get(userId);
        if (seckillOrder != null) {
            seckillOrder.setPayTime(new Date());
            seckillOrder.setStatus("1");
            seckillOrder.setTransactionId(transaction_id);
            //存储到数据库中
            seckillOrderMapper.insert(seckillOrder);
            //删除预订单即可
            redisTemplate.boundHashOps(SysConstants.SEC_KILL_ORDER).delete(userId);
        }
    }

    @Override
    public void deleteOrder(String userId) {
        //获取秒杀订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(SysConstants.SEC_KILL_ORDER).get(userId);
        if (seckillOrder == null) {
            System.out.println("没有该订单");
            return;
        }
        Long seckillId = seckillOrder.getSeckillId();
        //获取商品对象
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).get(seckillId);
        //说明 redis中已经没有了
        if (seckillGoods == null) {
            //从数据库查询
            seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillId);
            seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);
            //重新存储到REDIS中
            redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).put(seckillId, seckillGoods);
        } else {
            //1.恢复库存
            seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);
            redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).put(seckillId, seckillGoods);
        }
        //商品队列中恢复元素
        redisTemplate.boundListOps(SysConstants.SEC_KILL_GOODS_PREFIX + seckillId).leftPush(seckillId);
        //删除该预订单
        redisTemplate.boundHashOps(SysConstants.SEC_KILL_ORDER).delete(userId);
    }
}
