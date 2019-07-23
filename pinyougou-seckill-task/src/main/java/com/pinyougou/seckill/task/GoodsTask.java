package com.pinyougou.seckill.task;

import com.pinyougou.common.entity.SysConstants;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/20 16:38
 **/
@Component
public class GoodsTask {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 每三十秒查询数据库向redis新增秒杀商品数据
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void pushGoodToRedis() {
        Example example = new Example(SeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
        //审核通过
        criteria.andEqualTo("status", "2");
        //库存大于0
        criteria.andGreaterThan("stockCount", 0);
        final Date date = new Date();
        //开始时间小于现在时间
        criteria.andLessThan("startTime", date);
        //结束时间大于现在时间
        criteria.andGreaterThan("endTime", date);
        Set<Long> keys = redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).keys();
        //排除redis已存在的商品
        if (keys != null && keys.size() > 0) {
            criteria.andNotIn("id", keys);
        }
        List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
        //存储到redis中
        for (SeckillGoods seckillGoods : seckillGoodsList) {
            redisTemplate.boundHashOps(SysConstants.SEC_KILL_GOODS).put(seckillGoods.getId(), seckillGoods);
            pushGoodsList(seckillGoods);
        }

    }

    private void pushGoodsList(SeckillGoods goods) {
        //向同一个队列中压入商品数据
        for (Integer i = 0; i < goods.getStockCount(); i++) {
            //库存为多少就是多少个SIZE 值就是id即可
            redisTemplate.boundListOps(SysConstants.SEC_KILL_GOODS_PREFIX + goods.getId()).leftPush(goods.getId());
        }
    }

}
