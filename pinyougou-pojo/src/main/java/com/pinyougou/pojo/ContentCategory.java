package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_content_category")
@Data
public class ContentCategory implements Serializable {
    /**
     * 类目ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分类名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 分组
     */
    @Column(name = "content_group")
    private String contentGroup;

    /**
     * 分类key
     */
    @Column(name = "content_key")
    private String contentKey;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    private static final long serialVersionUID = 1L;
}