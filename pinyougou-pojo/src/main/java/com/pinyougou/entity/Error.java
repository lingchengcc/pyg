package com.pinyougou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/14 20:46
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error implements Serializable {
    /**
     * 发生错误的属性字段名 （必须要填）
     */
    private String field;
    /**
     * 发生错误的属性的ID 可选值
     */
    private Integer id;
    /**
     * 错误的信息（必须要要写）
     */
    private String msg;

    public Error(String field, String msg) {
        this.field = field;
        this.msg = msg;
    }
}
