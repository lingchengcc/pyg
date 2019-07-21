package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeChatPayService;
import com.pinyougou.pojo.PayLog;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/19 20:01
 **/
@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private WeChatPayService weChatPayService;
    @Reference
    private OrderService orderService;

    @RequestMapping("/createNative")
    public Map<String, String> createNative() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        PayLog payLog = orderService.searchPayLogFromRedis(userId);
        if (payLog != null) {
            return weChatPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
        }
        return null;
    }

    @RequestMapping("/queryPayStatus/{out_trade_no}")
    private Result queryPayStatus(@PathVariable String out_trade_no) {
        Result result;
        int count = 0;
        while (true) {
            Map<String, String> payStatus = weChatPayService.queryPayStatus(out_trade_no);
            if (payStatus == null) {
                result = new Result(false, "支付出错!");
                break;
            }
            if (payStatus.get("trade_state").equals("SUCCESS")) {
                orderService.updateOrderStatus(out_trade_no, payStatus.get("transaction_id"));
                result = new Result(true, "支付成功!");
                break;
            }
            count++;
            if (count >= 100) {
                result = new Result(false, "超时");
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
