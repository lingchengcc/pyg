package com.pinyougou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_specification_option")
@Data
public class SpecificationOption implements Serializable {
    /**
     * 规格项ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 规格项名称
     */
    @Column(name = "option_name")
    private String optionName;

    /**
     * 规格ID
     */
    @Column(name = "spec_id")
    private Long specId;

    /**
     * 排序值
     */
    @Column(name = "orders")
    private Integer orders;

    private static final long serialVersionUID = 1L;
}