package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_areas")
@Data
public class Areas implements Serializable {
    /**
     * 唯一ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 区域ID
     */
    @Column(name = "areaid")
    private String areaid;

    /**
     * 区域名称
     */
    @Column(name = "area")
    private String area;

    /**
     * 城市ID
     */
    @Column(name = "cityid")
    private String cityid;

    private static final long serialVersionUID = 1L;
}