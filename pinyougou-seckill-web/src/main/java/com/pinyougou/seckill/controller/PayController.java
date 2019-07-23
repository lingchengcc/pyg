package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.pay.service.WeChatPayService;
import com.pinyougou.pojo.SeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
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
    private SeckillOrderService orderService;

    @RequestMapping("/createNative")
    public Map<String, String> createNative() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        SeckillOrder order = orderService.getUserOrderStatus(userId);
        if (order != null) {
            return weChatPayService.createNative(order.getId().toString(), (long)(order.getMoney().doubleValue() * 100) + "");
        }
        return null;
    }

    @RequestMapping("/queryPayStatus/{out_trade_no}")
    public Result queryStatus(@PathVariable String out_trade_no) {
        Result result;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            int count = 0;
            //1.调用支付的服务 不停的查询 状态
            while (true) {
                Map<String, String> resultMap = weChatPayService.queryPayStatus(out_trade_no);
                count++;

                if (count >= 100) {
                    result = new Result(false, "支付超时");
                    //关闭微信订单
                    Map map = weChatPayService.closePay(out_trade_no);
                    if ("ORDERPAID".equals(map.get("err_code"))) {
                        //已经支付则更新入库
                        orderService.updateOrderStatus(resultMap.get("transaction_id"), userId);
                    } else if ("SUCCESS".equals(resultMap.get("result_code")) || "ORDERCLOSED".equals(resultMap.get("err_code"))) {
                        //删除预订单
                        orderService.deleteOrder(userId);
                    } else {
                        System.out.println("由于微信端错误");
                    }
                    break;
                }
                Thread.sleep(3000);
                //如果超时5分钟就直接退出。
                if ("SUCCESS".equals(resultMap.get("trade_state"))) {
                    result = new Result(true, "支付成功");
                    orderService.updateOrderStatus(resultMap.get("transaction_id"), userId);
                    break;
                }
            }
            //2.返回结果
            return result;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Result(false, "支付失败");
        }
    }
}
