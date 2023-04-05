package com.lyh.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDeleteRequest implements Serializable {
    private static final long serialVersionUID = -5092375798638063020L;

    //用户id
    private long id;
}