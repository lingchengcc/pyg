package com.pinyougou.pojo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Table(name = "tb_item")
@Data
@Document(indexName = "pinyougou", type = "item")
public class Item implements Serializable {
    /**
     * 商品id，同时也是商品编号
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Field
    @org.springframework.data.annotation.Id
    private Long id;

    /**
     * 商品标题
     */
    @Column(name = "title")
    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text, copyTo = "keyword")
    private String title;

    /**
     * 商品卖点
     */
    @Column(name = "sell_point")
    private String sellPoint;

    /**
     * 商品价格，单位为：元
     */
    @Column(name = "price")
    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Column(name = "stock_count")
    private Integer stockCount;

    /**
     * 库存数量
     */
    @Column(name = "num")
    private Integer num;

    /**
     * 商品条形码
     */
    @Column(name = "barcode")
    private String barcode;

    /**
     * 商品图片
     */
    @Column(name = "image")
    @Field(index = false, type = FieldType.Text)
    private String image;

    /**
     * 所属类目，叶子类目
     */
    @Column(name = "categoryId")
    private Long categoryid;

    /**
     * 商品状态，1-正常，2-下架，3-删除
     */
    @Column(name = "status")
    private String status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "item_sn")
    private String itemSn;

    @Column(name = "cost_pirce")
    private BigDecimal costPirce;

    @Column(name = "market_price")
    private BigDecimal marketPrice;

    @Column(name = "is_default")
    private String isDefault;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "cart_thumbnail")
    private String cartThumbnail;

    /**
     * 冗余字段 存放分类名称
     */
    @Column(name = "category")
    @Field(type = FieldType.Keyword, copyTo = "keyword")
    private String category;

    /**
     * 冗余字段 存放品牌名称
     */
    @Column(name = "brand")
    @Field(type = FieldType.Keyword, copyTo = "keyword")
    private String brand;

    @Column(name = "spec")
    @Field(index = false, type = FieldType.Keyword)
    private String spec;

    @Column(name = "seller")
    @Field(type = FieldType.Keyword, copyTo = "keyword")
    private String seller;
    /**
     * 要索引 嵌套类型?对象类型？  我使用对象类型 这里没有必要使用嵌套类型 只有数组的情况下再使用
     * 注意要使用这个字段来标识不需要从数据库进行映射
     */
    @Field(type = FieldType.Object)
    @Transient
    private Map<String, String> specMap;

    private static final long serialVersionUID = 1L;
}