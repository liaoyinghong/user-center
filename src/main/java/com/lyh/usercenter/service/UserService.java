package com.lyh.usercenter.service;

import com.lyh.usercenter.common.BaseResponse;
import com.lyh.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

/**
* @author lyh
* @description 针对表【user】的数据库操作Service
* @createDate 2023-02-05 11:32:23
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    BaseResponse<Long> userRegister(String userAccount, String userPassword, String checkPassword) throws NoSuchAlgorithmException;

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    BaseResponse<User> userLogin(String userAccount, String userPassword, HttpServletRequest userRequest);

    /**
     * 用户脱敏
     * @param user 用户信息
     * @return 用户脱敏信息
     */
    User getSafetyUser(User user);

    /**
     * 退出用户
     * @return 返回状态
     */
    BaseResponse<String> userLoginOut(HttpServletRequest userRequest);
}
