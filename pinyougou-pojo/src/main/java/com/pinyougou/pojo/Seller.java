package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "tb_seller")
@Data
public class Seller implements Serializable {
    /**
     * 用户ID 用户名
     */
    @Id
    @Column(name = "seller_id")
    private String sellerId;

    /**
     * 公司名
     */
    @Column(name = "name")
    private String name;

    /**
     * 店铺名称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * EMAIL
     */
    @Column(name = "email")
    private String email;

    /**
     * 公司手机
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 公司电话
     */
    @Column(name = "telephone")
    private String telephone;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 详细地址
     */
    @Column(name = "address_detail")
    private String addressDetail;

    /**
     * 联系人姓名
     */
    @Column(name = "linkman_name")
    private String linkmanName;

    /**
     * 联系人QQ
     */
    @Column(name = "linkman_qq")
    private String linkmanQq;

    /**
     * 联系人电话
     */
    @Column(name = "linkman_mobile")
    private String linkmanMobile;

    /**
     * 联系人EMAIL
     */
    @Column(name = "linkman_email")
    private String linkmanEmail;

    /**
     * 营业执照号
     */
    @Column(name = "license_number")
    private String licenseNumber;

    /**
     * 税务登记证号
     */
    @Column(name = "tax_number")
    private String taxNumber;

    /**
     * 组织机构代码
     */
    @Column(name = "org_number")
    private String orgNumber;

    /**
     * 公司地址
     */
    @Column(name = "address")
    private Long address;

    /**
     * 公司LOGO图
     */
    @Column(name = "logo_pic")
    private String logoPic;

    /**
     * 简介
     */
    @Column(name = "brief")
    private String brief;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 法定代表人
     */
    @Column(name = "legal_person")
    private String legalPerson;

    /**
     * 法定代表人身份证
     */
    @Column(name = "legal_person_card_id")
    private String legalPersonCardId;

    /**
     * 开户行账号名称
     */
    @Column(name = "bank_user")
    private String bankUser;

    /**
     * 开户行
     */
    @Column(name = "bank_name")
    private String bankName;

    private static final long serialVersionUID = 1L;
}