package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.common.util.HttpClient;
import com.pinyougou.pay.service.WeChatPayService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/19 12:29
 **/
@Service
public class WeChatPayServiceImpl implements WeChatPayService {
    @Value("${appId}")
    private String appId;
    @Value("${partner}")
    private String partner;
    @Value("${partnerKey}")
    private String partnerKey;

    @Override
    public Map<String, String> createNative(String out_trade_no, String total_fee) {
        Map<String, String> param = new HashMap<>(9);
        //公众号
        param.put("appid", appId);
        //商户号
        param.put("mch_id", partner);
        //随机字符串
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述
        param.put("body", "品优购");
        //商户订单号
        param.put("out_trade_no", out_trade_no);
        //总金额（分）
        param.put("total_fee", total_fee);
        //IP
        param.put("spbill_create_ip", "127.0.0.1");
        //回调地址(随便写)
        param.put("notify_url", "http://test.itcast.cn");
        //交易类型
        param.put("trade_type", "NATIVE");
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerKey);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            final String result = client.getContent();
            final Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            final HashMap<String, String> map = new HashMap<>(3);
            //支付地址
            map.put("code_url", resultMap.get("code_url"));
            //总金额
            map.put("total_fee", total_fee);
            //订单号
            map.put("out_trade_no", out_trade_no);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        Map<String, String> param = new HashMap(4);
        //公众账号ID
        param.put("appid", appId);
        //商户号
        param.put("mch_id", partner);
        //订单号
        param.put("out_trade_no", out_trade_no);
        //随机字符串
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerKey);
            HttpClient client = new HttpClient(url);
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            String result = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public Map<String, String> closePay(String out_trade_no) {
        try {
            Map<String, String> param = new HashMap(4);
            //公众账号ID
            param.put("appid", appId);
            //商户号
            param.put("mch_id", partner);
            //订单号
            param.put("out_trade_no", out_trade_no);
            //随机字符串
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            //将Map数据转成XML字符
            String xmlParam = WXPayUtil.generateSignedXml(param,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb");
            //确定url
            String url = "https://api.mch.weixin.qq.com/pay/closeorder";
            //发送请求
            HttpClient httpClient = new HttpClient(url);
            //https
            httpClient.setHttps(true);
            //提交参数
            httpClient.setXmlParam(xmlParam);
            //提交
            httpClient.post();
            //获取返回数据
            String content = httpClient.getContent();
            //将返回数据解析成Map
            return  WXPayUtil.xmlToMap(content);

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }
}
