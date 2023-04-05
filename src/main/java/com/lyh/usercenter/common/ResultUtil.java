package com.lyh.usercenter.common;

/**
 * 返回工具类
 */
public class ResultUtil {

    /**
     * 成功
     * @param code
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(int code,T data,String description){
        return new BaseResponse<>(code,data,"OK",description);
    }

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data,String description){
        return new BaseResponse<>(0,data,"OK",description);
    }

    /**
     * 失败
     * @param code
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode code){
        return new BaseResponse<>(code);
    }

    /**
     * 失败
     * @param code
     * @param data
     * @param message
     * @param description
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(int code,T data,String message,String description){
        return new BaseResponse<>(code,data,message,description);
    }
}
