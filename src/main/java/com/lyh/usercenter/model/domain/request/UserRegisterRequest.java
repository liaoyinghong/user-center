package com.lyh.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -5092375798638063020L;

    //用户注册账号
    private String userAccount;
    //注册密码
    private String userPassword;
    //二次输入密码
    private String checkPassword;
}
