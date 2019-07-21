package com.pinyougou.common.entity;

/**
 * @author silent
 */
public class SysConstants {
    /**
     * 秒杀中的某商品的队列名前缀
     * 一个商品就是一个队列  队列中的数据便是商品本身 队列的长度便是 商品的库存
     */
    public static final String SEC_KILL_GOODS_PREFIX = "SEC_KILL_GOODS_ID_";

    /**
     * 用于表示用户抢购的下单的排队
     */
    public static final String SEC_KILL_USER_ORDER_LIST = "SEC_KILL_USER_ORDER_LIST";

    /**
     * 用于标识某商品被抢购的人数队列的名字前缀  一个商品就是一个队列
     */
    public static final String SEC_KILL_LIMIT_PREFIX = "SEC_KILL_LIMIT_SEC_ID_";

    /**
     * 用于标识用户已秒杀下单排队中的key
     */
    public static final String SEC_USER_QUEUE_FLAG_KEY = "SEC_USER_QUEUE_FLAG_KEY";

    /**
     * 所有商品的集合数据的KEY
     */
    public static final String SEC_KILL_GOODS = "SEC_KILL_GOODS";

    /**
     * 秒杀商品的订单的KEY
     */
    public static final String SEC_KILL_ORDER = "SEC_KILL_ORDER";

    /**
     * 某广告的列表redis的KEY
     */
    public static final String CONTENT_REDIS_KEY = "CONTENT_REDIS_KEY";
    /**
     * 商品分类的列表redis的KEY
     */
    public static final String ITEM_CAT_REDIS_KEY = "ITEM_CAT_REDIS_KEY";
    /**
     * 品牌的redis的KEY
     */
    public static final String BRAND_REDIS_KEY = "BRAND_REDIS_KEY";
    /**
     * 规格的redis的KEY
     */
    public static final String SPECIFICATION_REDIS_KEY = "SPECIFICATION_REDIS_KEY";
    /**
     * 商品状态:未提交审核
     */
    public static final String GOODS_STATUS_UNSUBMIT_REVIEW = "0";
    /**
     * 商品状态:已提交审核
     */
    public static final String GOODS_STATUS_SUBMIT_REVIEW = "1";
    /**
     * 商品状态:通过审核
     */
    public static final String GOODS_STATUS_PASS_REVIEW = "2";
    /**
     * 商品状态:未通过审核
     */
    public static final String GOODS_STATUS_FAILED_REVIEW = "3";
    /**
     * 匿名用户
     */
    public static final String ANONYMOUS_USER = "anonymousUser";

}
