package com.lyh.usercenter.service;

import com.lyh.usercenter.contant.UserContant;
import com.lyh.usercenter.controller.UserController;
import com.lyh.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;

/**
 * 用户服务测试
 *
 * @author lyh
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("abc");
        user.setUserAccount("114514");
        user.setAvatarUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("12345678911");
        user.setEmail("1213154@qq.com");

        System.out.println(user.getId());

    }

 /*   @Test
    void testRegister() throws NoSuchAlgorithmException {
        String userAccount = "";
        String userPassword = "";
        String checkPassword = "";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        *//*userAccount = "123";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "3306";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userPassword = "12345!@";
        checkPassword = "12345!@";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "114514";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);*//*
        userAccount = "10010";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
    }

    @Test
    void testPassword(){
        String userAccount = "10010";
        String userPassword = "123456789";
        User user = userService.userLogin(userAccount, userPassword, null);
        System.out.println(user.toString());
    }*/

}