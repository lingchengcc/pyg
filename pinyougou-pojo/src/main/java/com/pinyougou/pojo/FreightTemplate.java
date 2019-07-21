package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "tb_freight_template")
@Data
public class FreightTemplate implements Serializable {
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
     * 是否默认   （‘Y’是   'N'否）
     */
    @Column(name = "is_default")
    private String isDefault;

    /**
     * 模版名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 发货时间（1:12h  2:24h  3:48h  4:72h  5:7d 6:15d ）
     */
    @Column(name = "send_time_type")
    private String sendTimeType;

    /**
     * 统一价格
     */
    @Column(name = "price")
    private Long price;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}