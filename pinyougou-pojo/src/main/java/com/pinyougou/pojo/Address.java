package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "tb_address")
@Data
public class Address implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 省
     */
    @Column(name = "province_id")
    private String provinceId;

    /**
     * 市
     */
    @Column(name = "city_id")
    private String cityId;

    /**
     * 县/区
     */
    @Column(name = "town_id")
    private String townId;

    /**
     * 手机
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 详细地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 联系人
     */
    @Column(name = "contact")
    private String contact;

    /**
     * 是否是默认 1默认 0否
     */
    @Column(name = "is_default")
    private String isDefault;

    /**
     * 备注
     */
    @Column(name = "notes")
    private String notes;

    /**
     * 创建日期
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 别名
     */
    @Column(name = "alias")
    private String alias;

    private static final long serialVersionUID = 1L;
}