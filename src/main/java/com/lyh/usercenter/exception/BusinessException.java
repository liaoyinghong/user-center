package com.lyh.usercenter.exception;

import com.lyh.usercenter.common.ErrorCode;

/**
 * 全局异常类
 */

public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 8316324712329181897L;

    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode code) {
        super(code.getMessage());
        this.code = code.getCode();
        this.description = code.getDescription();
    }

    public BusinessException(ErrorCode code,String description) {
        super(code.getMessage());
        this.code = code.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
