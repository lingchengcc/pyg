package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_type_template")
@Data
public class TypeTemplate implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 关联规格
     */
    @Column(name = "spec_ids")
    private String specIds;

    /**
     * 关联品牌
     */
    @Column(name = "brand_ids")
    private String brandIds;

    /**
     * 自定义属性
     */
    @Column(name = "custom_attribute_items")
    private String customAttributeItems;

    private static final long serialVersionUID = 1L;
}