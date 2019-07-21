package com.pinyougou.pojo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Table(name = "tb_user")
@Data
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username")
    @NotBlank(message = "用户名不能为空!")
    @Pattern(regexp = "^[a-zA-Z0-9\\u4E00-\\u9FA5]+$",message = "用户名只能为数字或者字母!")
    private String username;

    /**
     * 密码，加密存储
     */
    @Column(name = "password")
    @NotBlank(message = "密码不能为空!")
    private String password;

    /**
     * 注册手机号
     */
    @Column(name = "phone")
    @NotBlank(message = "手机号不允许为空!")
    @Pattern(regexp = "[1][3|4|5|7|8][0-9]{9}",message = "手机号格式不正确！")
    private String phone;

    /**
     * 注册邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 创建时间
     */
    @Column(name = "created")
    private Date created;

    @Column(name = "updated")
    private Date updated;

    /**
     * 会员来源：1:PC，2：H5，3：Android，4：IOS，5：WeChat
     */
    @Column(name = "source_type")
    private String sourceType;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 真实姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 使用状态（Y正常 N非正常）
     */
    @Column(name = "status")
    private String status;

    /**
     * 头像地址
     */
    @Column(name = "head_pic")
    private String headPic;

    /**
     * QQ号码
     */
    @Column(name = "qq")
    private String qq;

    /**
     * 账户余额
     */
    @Column(name = "account_balance")
    private Long accountBalance;

    /**
     * 手机是否验证 （0否  1是）
     */
    @Column(name = "is_mobile_check")
    private String isMobileCheck;

    /**
     * 邮箱是否检测（0否  1是）
     */
    @Column(name = "is_email_check")
    private String isEmailCheck;

    /**
     * 性别，1男，2女
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 会员等级
     */
    @Column(name = "user_level")
    private Integer userLevel;

    /**
     * 积分
     */
    @Column(name = "points")
    private Integer points;

    /**
     * 经验值
     */
    @Column(name = "experience_value")
    private Integer experienceValue;

    /**
     * 生日
     */
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    private static final long serialVersionUID = 1L;
}