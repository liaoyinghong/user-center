package com.lyh.usercenter.common;

/**
 * 异常处理枚举类
 */
public enum ErrorCode {

    REQUEST_ERROR(40101,"请求参数错误",""),
    NULL_ERROR(40102,"请求数据为空",""),
    NO_ERROR(40103,"无权限",""),
    NOT_ERROR(40104,"未登录","");

    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

}
