package com.pinyougou.pay.service;

import java.util.Map;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/19 12:26
 **/
public interface WeChatPayService {
    /**
     * 生成微信支付二维码
     *
     * @param out_trade_no 订单号
     * @param total_fee    金额(分)
     * @return
     */
    public Map<String, String> createNative(String out_trade_no, String total_fee);

    /**
     * 查询支付状态
     *
     * @param out_trade_no
     */
    public Map<String, String> queryPayStatus(String out_trade_no);

    Map<String, String> closePay(String out_trade_no);
}
