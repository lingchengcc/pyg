package com.pinyougou.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.MessageInfo;
import com.pinyougou.pojo.SeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/10 14:37
 **/
@Component
public class PageMessageListener implements MessageListenerConcurrently {
    @Resource
    private SeckillGoodsService seckillGoodsService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>seckill-page接收到了信息<<<<<<<<<<<<<<<<<<<");

        try {
            if (list != null) {
                for (MessageExt msg : list) {
                    String body = new String(msg.getBody());
                    MessageInfo info = JSON.parseObject(body, MessageInfo.class);
                    switch (info.getMethod()) {
                        case MessageInfo.METHOD_ADD:
                        case MessageInfo.METHOD_UPDATE: {
                            updatePageHtml(info);
                            break;
                        }
                        case MessageInfo.METHOD_DELETE: {
                            String context = info.getContext().toString();
                            Long[] ids = JSON.parseObject(context, Long[].class);
                            seckillGoodsService.deleteById(ids);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

    private void updatePageHtml(MessageInfo info) throws IOException {
        String context = info.getContext().toString();
        List<SeckillGoods> seckillGoodsList = JSON.parseArray(context, SeckillGoods.class);
        if (seckillGoodsList != null && seckillGoodsList.size() != 0) {
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                seckillGoodsService.genItemHtml(seckillGoods);
            }
        }
    }
}
