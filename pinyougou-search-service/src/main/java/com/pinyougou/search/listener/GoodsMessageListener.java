package com.pinyougou.search.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.entity.MessageInfo;
import com.pinyougou.pojo.Item;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/10 14:37
 **/
@Component
public class GoodsMessageListener implements MessageListenerConcurrently {
    @Resource
    private ItemSearchService itemSearchService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>search接收到了信息<<<<<<<<<<<<<<<<<<<");

        try {
            if (list != null) {
                for (MessageExt msg : list) {
                    byte[] body = msg.getBody();
                    String msgBody = new String(body);
                    MessageInfo info = JSON.parseObject(msgBody, MessageInfo.class);
                    switch (info.getMethod()) {
                        case MessageInfo.METHOD_ADD:
                        case MessageInfo.METHOD_UPDATE: {
                            String context = info.getContext().toString();
                            List<Item> itemList = JSON.parseArray(context, Item.class);
                            itemSearchService.updateIndex(itemList);
                            break;
                        }
                        case MessageInfo.METHOD_DELETE: {
                            String context = info.getContext().toString();
                            Long[] ids = JSON.parseObject(context, Long[].class);
                            itemSearchService.deleteByIds(ids);
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
}
