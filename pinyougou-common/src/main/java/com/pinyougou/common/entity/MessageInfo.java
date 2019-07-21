package com.pinyougou.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author silent
 * @version 1.0
 * @date 2019/7/10 15:16
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageInfo {
    /**
     * 用于新增操作
     */
    public static final int METHOD_ADD = 1;
    /**
     * 用于更新操作
     */
    public static final int METHOD_UPDATE = 2;
    /**
     * 用于删除操作
     */
    public static final int METHOD_DELETE = 3;
    /**
     * 要发送的内容
     */
    private Object context;

    private String topic;

    private String tags;

    private String keys;

    private int method;

}
