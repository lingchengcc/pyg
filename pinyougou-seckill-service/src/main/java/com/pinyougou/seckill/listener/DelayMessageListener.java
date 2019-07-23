package com.pinyougou.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.MessageInfo;
import com.pinyougou.mapper.SeckillOrderMapper;
import com.pinyougou.pojo.SeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/23 22:22
 **/
public class DelayMessageListener implements MessageListenerConcurrently {
    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private SeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            if (msgs != null) {
                for (MessageExt msg : msgs) {
                    System.out.println("延时消息接收");
                    byte[] body = msg.getBody();
                    String s = new String(body);
                    MessageInfo messageInfo = JSON.parseObject(s, MessageInfo.class);
                    //订单更新 标识即可
                    if (messageInfo.getMethod() == MessageInfo.METHOD_UPDATE) {
                        //获取Redis中的未支付订单的信息
                        SeckillOrder tbSeckillOrder = JSON.parseObject(messageInfo.getContext().toString(), SeckillOrder.class);
                        //获取数据库中的订单的信息
                        SeckillOrder seckillOrder = seckillOrderMapper.selectByPrimaryKey(tbSeckillOrder.getId());
                        if (seckillOrder == null) {
                            //关闭微信订单  如果 关闭微信订单的时候出现该订单已经关闭 则说明不需要 再恢复库存
                            //删除订单
                            seckillOrderService.deleteOrder(tbSeckillOrder.getUserId());
                        }
                        //有订单 无需关心
                    }
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}