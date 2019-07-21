package com.pinyougou.sms.listener;

import com.alibaba.fastjson.JSON;
import com.pinyougou.sms.utils.SmsUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/10 14:37
 **/
@Component
public class SMSMessageListener implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>sms接收到了信息<<<<<<<<<<<<<<<<<<<");

        try {
            if (list != null) {
                for (MessageExt msg : list) {
                    byte[] body = msg.getBody();
                    String context = new String(body);
                    Map<String, String> map = JSON.parseObject(context, Map.class);
                    SmsUtil.sendSms(map);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
