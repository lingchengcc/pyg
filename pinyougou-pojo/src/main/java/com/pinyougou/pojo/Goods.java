package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "tb_goods")
@Data
public class Goods implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商家ID
     */
    @Column(name = "seller_id")
    private String sellerId;

    /**
     * SPU名
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 默认SKU
     */
    @Column(name = "default_item_id")
    private Long defaultItemId;

    /**
     * 状态 表示商品是否被审计 0 未审核 1 已审核 2 审核未通过 3 关闭
     */
    @Column(name = "audit_status")
    private String auditStatus;

    /**
     * 是否上架
     */
    @Column(name = "is_marketable")
    private String isMarketable;

    /**
     * 品牌
     */
    @Column(name = "brand_id")
    private Long brandId;

    /**
     * 副标题
     */
    @Column(name = "caption")
    private String caption;

    /**
     * 一级类目
     */
    @Column(name = "category1_id")
    private Long category1Id;

    /**
     * 二级类目
     */
    @Column(name = "category2_id")
    private Long category2Id;

    /**
     * 三级类目
     */
    @Column(name = "category3_id")
    private Long category3Id;

    /**
     * 小图
     */
    @Column(name = "small_pic")
    private String smallPic;

    /**
     * 商城价
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 分类模板ID
     */
    @Column(name = "type_template_id")
    private Long typeTemplateId;

    /**
     * 是否启用规格
     */
    @Column(name = "is_enable_spec")
    private String isEnableSpec;

    /**
     * 是否删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete;

    private static final long serialVersionUID = 1L;
}