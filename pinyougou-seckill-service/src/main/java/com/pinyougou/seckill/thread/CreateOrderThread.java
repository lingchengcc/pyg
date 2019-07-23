package com.pinyougou.seckill.thread;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.MessageInfo;
import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.pojo.SeckillGoods;
import com.pinyougou.pojo.SeckillOrder;
import com.pinyougou.seckill.pojo.SeckillStatus;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/23 18:37
 **/
public class CreateOrderThread {
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private DefaultMQProducer producer;

    /**
     * 多线程执行下单操作 异步方法
     */
    @Async
    public void handleOrder() {
        try {
            System.out.println("模拟处理订单开始========" + Thread.currentThread().getName());
            Thread.sleep(10000);
            System.out.println("模拟处理订单结束 总共耗费10秒钟=======" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(SysConstants.SEC_KILL_USER_ORDER_LIST).rightPop();
        if (seckillStatus != null) {
            //从nosql数据库中获取商品
            SeckillGoods killgoods = (SeckillGoods) redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).get(seckillStatus.getGoodsId());
            //将这个商品的库存减少
            killgoods.setStockCount(killgoods.getStockCount() - 1);

            redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).put(seckillStatus.getGoodsId(), killgoods);
            //如果已经被秒光
            if (killgoods.getStockCount() <= 0) {
                //同步到数据库
                seckillGoodsMapper.updateByPrimaryKey(killgoods);
                //将redis中的该商品清除掉
                redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).delete(seckillStatus.getGoodsId());
            }
            //创建订单
            long orderId = idWorker.nextId();

            SeckillOrder seckillOrder = new SeckillOrder();
            //设置订单的ID 这个就是out_trade_no
            seckillOrder.setId(orderId);
            //创建时间
            seckillOrder.setCreateTime(new Date());
            //秒杀价格  价格
            seckillOrder.setMoney(killgoods.getCostPrice());
            //秒杀商品的ID
            seckillOrder.setSeckillId(seckillStatus.getGoodsId());
            seckillOrder.setSellerId(killgoods.getSellerId());
            //设置用户ID
            seckillOrder.setUserId(seckillStatus.getUserId());
            //状态 未支付
            seckillOrder.setStatus("0");
            //将构建的订单保存到redis中
            redisTemplate.boundHashOps(SysConstants.SEC_KILL_ORDER).put(seckillStatus.getUserId(), seckillOrder);
            //移除排队标识 标识下单成功
            redisTemplate.boundHashOps(SysConstants.SEC_USER_QUEUE_FLAG_KEY).delete(seckillStatus.getUserId());
        }
    }
    private void sendMessage(SeckillOrder seckillOrder) {
        try {
            MessageInfo messageInfo = new MessageInfo(seckillOrder,"TOPIC_SECKILL_DELAY","TAG_SECKILL_DELAY","handleOrder_DELAY", MessageInfo.METHOD_UPDATE);
            //
            System.out.println("多线程下单============");
            Message message = new Message(messageInfo.getTopic(),messageInfo.getTags(),messageInfo.getKeys(), JSON.toJSONString(messageInfo).getBytes());
            //1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            //设置消息演示等级 16=30m
            message.setDelayTimeLevel(5);
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
