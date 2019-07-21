package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_goods_desc")
@Data
public class GoodsDesc implements Serializable {
    /**
     * SPU_ID
     */
    @Id
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 描述
     */
    @Column(name = "introduction")
    private String introduction;

    /**
     * 规格结果集，所有规格，包含isSelected
     */
    @Column(name = "specification_items")
    private String specificationItems;

    /**
     * 自定义属性（参数结果）
     */
    @Column(name = "custom_attribute_items")
    private String customAttributeItems;

    /**
     * 商品的图片信息和颜色信息存储
     */
    @Column(name = "item_images")
    private String itemImages;

    /**
     * 包装列表
     */
    @Column(name = "package_list")
    private String packageList;

    /**
     * 售后服务
     */
    @Column(name = "sale_service")
    private String saleService;

    private static final long serialVersionUID = 1L;
}