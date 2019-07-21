package com.pinyougou.pojo;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

@Table(name = "tb_brand_img")
@Data
public class BrandImg implements Serializable {
    /**
     * 品牌图标id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 品牌图标的url
     */
    @Column(name = "pic")
    private String pic;

    /**
     * 品牌名称(冗余字段)
     */
    @Column(name = "brand")
    private String brand;

    /**
     * 品牌id
     */
    @Column(name = "brand_id")
    private Long brandId;

    private static final long serialVersionUID = 1L;
}