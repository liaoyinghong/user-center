package com.lyh.usercenter.exception;

import com.lyh.usercenter.common.BaseResponse;
import com.lyh.usercenter.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 校验异常捕捉
     * @param b
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Object> BusinessExceptionHandler(BusinessException b) {
        log.error(b.getMessage(),b);
        return ResultUtil.error(b.getCode(),null,b.getMessage(), b.getDescription());
    }

    /**
     * 用户名生成异常捕捉
     * @param e
     * @return
     */
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public BaseResponse<Object> NoSuchAlgorithmExceptionHandler(NoSuchAlgorithmException e) {
        log.error("用户名自动生成失败",e);
        return ResultUtil.error(404,null,"用户名自动生成失败", "");
    }

}
