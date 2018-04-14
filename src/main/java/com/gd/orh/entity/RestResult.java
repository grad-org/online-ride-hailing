package com.gd.orh.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class RestResult {
    //状态码
    private int status;
    //消息
    private String message;
    //额外的内容
    private Object data;
}