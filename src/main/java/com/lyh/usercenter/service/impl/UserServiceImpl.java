package com.lyh.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.usercenter.common.BaseResponse;
import com.lyh.usercenter.common.ErrorCode;
import com.lyh.usercenter.common.ResultUtil;
import com.lyh.usercenter.exception.BusinessException;
import com.lyh.usercenter.mapper.UserMapper;
import com.lyh.usercenter.model.domain.User;
import com.lyh.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static com.lyh.usercenter.contant.UserContant.USER_LOGIN_STATE;


/**
 * 用户服务实现类
 *
 * @author lyh
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值
     */
    private static final String SALT = "abc";

    @Resource
    UserMapper userMapper;

    /**
     * 注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 状态码
     */
    @Override
    public BaseResponse<Long> userRegister(String userAccount, String userPassword, String checkPassword) {
        //效验账号密码
        verificationLoginAndRegister(userAccount,userPassword);
        //两次密码是否输入一致
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"两次密码输入不一致");
        }
        //账号不能重复
        long count = this.count(new QueryWrapper<User>().eq("userAccount", userAccount));
        if (count > 0){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"该账号已被注册");
        }
        //密码加密(随机数+盐)
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //插入注册数据
        User user = new User();
        //生成随机用户名,抛出异常
        String username = null;
        try {
            username = "用户" + SecureRandom.getInstance("SHA1PRNG").nextInt(1000000000);
        } catch (NoSuchAlgorithmException e) {
            log.error("用户名自动生成失败",e);
            e.printStackTrace();
        }
        user.setUsername(username);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setAvatarUrl("src/images/瓜寅.png");
        //保存失败返回-1
        boolean saveResult = this.save(user);
        if (!saveResult){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"网络异常请重试");
        }

        //注册成功返回id
        return ResultUtil.success(user.getId(),"注册成功");
    }

    /**
     * 登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param userRequest 请求
     * @return 用户信息
     */
    @Override
    public BaseResponse<User> userLogin(String userAccount, String userPassword, HttpServletRequest userRequest) {

        //效验账号密码
        verificationLoginAndRegister(userAccount,userPassword);

        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查询用户是否存在，密码是否正确
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(userQueryWrapper);

        //判断用户是否为空
        if (user == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号或密码有误");
        }if (user.getIsDelete() == 1){
            throw new BusinessException(ErrorCode.NULL_ERROR,"该用户已注销");
        }

        //用户信息脱敏
        User desensitizationUser = getSafetyUser(user);

        //记录用户的登录态
        userRequest.getSession().setAttribute(USER_LOGIN_STATE,desensitizationUser);

        return ResultUtil.success(desensitizationUser,"登录成功");
    }
    /**
     * 账号和密码效验
     */
    public void verificationLoginAndRegister(String userAccount, String userPassword){
        //1. 校验非空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号或密码为空");
        }

        //2.账号长度大于等于4或账号长度小于12
        if (userAccount.length() < 4 || userAccount.length() > 12){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"账号超出范围");
        }
        //3.密码长度大于等于8且密码长度小于等于16
        if (userPassword.length() < 8 || userPassword.length() > 16){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"密码超出范围");
        }

        //4.账号可以为4-12位大小写英文加数字，特殊符号组成
        String accountPattern = "[0-9A-Za-z\\W]{4,12}$";
        boolean accountMatch = userAccount.matches(accountPattern);
        if (!accountMatch){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"账号含特殊符号");
        }

        //5.密码可以为8-16位大小写英文加数字，特殊符号组成
        String passwordPattern = "[0-9A-Za-z\\W]{8,16}$";
        boolean passwordMatch = userPassword.matches(passwordPattern);
        if (!passwordMatch){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"密码含特殊符号");
        }
    }

    /**
     * 用户脱敏
     * @param user 用户信息
     * @return 用户脱敏信息
     */
    @Override
    public User getSafetyUser(User user){
        if(user == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"该用户不存在");
        }
        User desensitizationUser = new User();
        desensitizationUser.setId(user.getId());
        desensitizationUser.setUsername(user.getUsername());
        desensitizationUser.setUserAccount(user.getUserAccount());
        desensitizationUser.setAvatarUrl(user.getAvatarUrl());
        desensitizationUser.setGender(user.getGender());
        desensitizationUser.setPhone(user.getPhone());
        desensitizationUser.setEmail(user.getEmail());
        desensitizationUser.setUserStatus(user.getUserStatus());
        desensitizationUser.setCreateTime(user.getCreateTime());
        desensitizationUser.setUpdateTime(user.getUpdateTime());
        desensitizationUser.setIsDelete(0);
        desensitizationUser.setUserRole(user.getUserRole());
        return desensitizationUser;
    }

    /**
     * 退出登录
     * @param userRequest 退出登录请求
     * @return 响应是否成功
     */
    @Override
    public BaseResponse<String> userLoginOut(HttpServletRequest userRequest) {
        userRequest.getSession().removeAttribute(USER_LOGIN_STATE);
        return ResultUtil.success(null,"退出登录");
    }


}
