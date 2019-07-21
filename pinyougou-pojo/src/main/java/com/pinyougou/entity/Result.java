package com.pinyougou.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/23 19:39
 **/
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    private boolean success;
    private String message;
    private List<Error> errorList;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
