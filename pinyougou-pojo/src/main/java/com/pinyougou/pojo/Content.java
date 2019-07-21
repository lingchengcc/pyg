package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_content")
@Data
public class Content implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 内容类目ID
     */
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 内容标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 链接
     */
    @Column(name = "url")
    private String url;

    /**
     * 图片绝对路径
     */
    @Column(name = "pic")
    private String pic;

    /**
     * 内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    private static final long serialVersionUID = 1L;
}