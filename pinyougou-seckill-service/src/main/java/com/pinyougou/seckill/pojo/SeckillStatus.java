package com.pinyougou.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/23 18:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillStatus implements Serializable {
    /**
     * 排队中
     */
    public static final Integer SECKILL_queuing = 1;
    /**
     * 待支付
     */
    public static final Integer SECKILL_to_be_paid = 2;
    /**
     * 支付超时
     */
    public static final Integer SECKILL_be_paid_timeout = 3;
    /**
     * 支付失败
     */
    public static final Integer SECKILL_fail = 4;
    /**
     * 已支付
     */
    public static final Integer SECKILL_paid = 5;
    /**
     * 用户的ID
     */
    private String userId;
    /**
     * 秒杀商品的ID
     */
    private Long goodsId;
    /**
     * 秒杀的状态 1.排队中 2.待支付 3 支付超时 4 秒杀失败 5 已支付
     */
    private Integer status;

    public SeckillStatus(String userId, Long goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }
}
