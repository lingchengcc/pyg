package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_provinces")
@Data
public class Provinces implements Serializable {
    /**
     * 唯一ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 省份ID
     */
    @Column(name = "provinceid")
    private String provinceid;

    /**
     * 省份名称
     */
    @Column(name = "province")
    private String province;

    private static final long serialVersionUID = 1L;
}