package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_cities")
@Data
public class Cities implements Serializable {
    /**
     * 唯一ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 城市ID
     */
    @Column(name = "cityid")
    private String cityid;

    /**
     * 城市名称
     */
    @Column(name = "city")
    private String city;

    /**
     * 省份ID
     */
    @Column(name = "provinceid")
    private String provinceid;

    private static final long serialVersionUID = 1L;
}