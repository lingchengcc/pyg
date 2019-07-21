package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tb_order")
@Data
public class Order implements Serializable {
    /**
     * 订单id
     */
    @Id
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @Column(name = "payment")
    private BigDecimal payment;

    /**
     * 支付类型，1、在线支付，2、货到付款
     */
    @Column(name = "payment_type")
    private String paymentType;

    /**
     * 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @Column(name = "post_fee")
    private String postFee;

    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
     */
    @Column(name = "status")
    private String status;

    /**
     * 订单创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 订单更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 付款时间
     */
    @Column(name = "payment_time")
    private Date paymentTime;

    /**
     * 发货时间
     */
    @Column(name = "consign_time")
    private Date consignTime;

    /**
     * 交易完成时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 交易关闭时间
     */
    @Column(name = "close_time")
    private Date closeTime;

    /**
     * 物流名称
     */
    @Column(name = "shipping_name")
    private String shippingName;

    /**
     * 物流单号
     */
    @Column(name = "shipping_code")
    private String shippingCode;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 买家留言
     */
    @Column(name = "buyer_message")
    private String buyerMessage;

    /**
     * 买家昵称
     */
    @Column(name = "buyer_nick")
    private String buyerNick;

    /**
     * 买家是否已经评价
     */
    @Column(name = "buyer_rate")
    private String buyerRate;

    /**
     * 收货人地区名称(省，市，县)街道
     */
    @Column(name = "receiver_area_name")
    private String receiverAreaName;

    /**
     * 收货人手机
     */
    @Column(name = "receiver_mobile")
    private String receiverMobile;

    /**
     * 收货人邮编
     */
    @Column(name = "receiver_zip_code")
    private String receiverZipCode;

    /**
     * 收货人
     */
    @Column(name = "receiver")
    private String receiver;

    /**
     * 过期时间，定期清理
     */
    @Column(name = "expire")
    private Date expire;

    /**
     * 发票类型(普通发票，电子发票，增值税发票)
     */
    @Column(name = "invoice_type")
    private String invoiceType;

    /**
     * 订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
     */
    @Column(name = "source_type")
    private String sourceType;

    /**
     * 商家ID
     */
    @Column(name = "seller_id")
    private String sellerId;

    private static final long serialVersionUID = 1L;
}