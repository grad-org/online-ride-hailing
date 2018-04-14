package com.gd.orh.utils;

import com.gd.orh.entity.RestResult;
import com.gd.orh.entity.ResultCode;

public class RestResultFactory {

    private static final String SUCCESS = "success";

    // 成功
    public static RestResult getSuccessResult() {
        return new RestResult()
                .setStatus(ResultCode.SUCCESS.getCode())
                .setMessage(SUCCESS);
    }

    // 成功，附带额外数据
    public static RestResult getSuccessResult(Object data) {
        return new RestResult()
                .setStatus(ResultCode.SUCCESS.getCode())
                .setMessage(SUCCESS)
                .setData(data);
    }

    // 成功，自定义消息及数据
    public static RestResult getSuccessResult(String message,Object data) {
        return new RestResult()
                .setStatus(ResultCode.SUCCESS.getCode())
                .setMessage(message)
                .setData(data);
    }

    // 失败，附带消息
    public static RestResult getFailResult(String message) {
        return new RestResult()
                .setStatus(ResultCode.FAIL.getCode())
                .setMessage(message);
    }

    // 失败，自定义消息及数据
    public static RestResult getFailResult(String message, Object data) {
        return new RestResult()
                .setStatus(ResultCode.FAIL.getCode())
                .setMessage(message)
                .setData(data);
    }

    // 未认证，附带消息
    public static RestResult getUnauthorizedResult(String message) {
        return new RestResult()
                .setStatus(ResultCode.UNAUTHORIZED.getCode())
                .setMessage(message);
    }

    // 自定义创建
    public static RestResult getFreeResult(ResultCode code, String message, Object data) {
        return new RestResult()
                .setStatus(code.getCode())
                .setMessage(message)
                .setData(data);
    }
}
