package com.lyh.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -5092375798638063020L;

    //用户登录账号
    private String userAccount;
    //登录密码
    private String userPassword;
}
