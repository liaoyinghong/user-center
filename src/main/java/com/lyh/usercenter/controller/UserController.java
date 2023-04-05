package com.lyh.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.usercenter.common.BaseResponse;
import com.lyh.usercenter.common.ErrorCode;
import com.lyh.usercenter.common.ResultUtil;
import com.lyh.usercenter.exception.BusinessException;
import com.lyh.usercenter.model.domain.User;
import com.lyh.usercenter.model.domain.request.UserDeleteRequest;
import com.lyh.usercenter.model.domain.request.UserLoginRequest;
import com.lyh.usercenter.model.domain.request.UserRegisterRequest;
import com.lyh.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import static com.lyh.usercenter.contant.UserContant.ADMIN_ROLE;
import static com.lyh.usercenter.contant.UserContant.USER_LOGIN_STATE;


/**
 * 用户接口
 *
 * @author lyh
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    /**
     * 用户注册接口
     * @param userRegisterRequest 注册信息
     * @return 用户id
     */
    @PostMapping("/userRegister")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) throws NoSuchAlgorithmException {

        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号或密码为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号或密码为空");
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录接口
     * @param userLoginRequest 登录输入信息
     * @param userRequest 请求登录信息
     * @return 用户id
     */
    @PostMapping("/userLogin")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest,HttpServletRequest userRequest){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号或密码为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号或密码为空");
        }
        return userService.userLogin(userAccount, userPassword,userRequest);
    }

    /**
     * 获取登录用户信息
     * @param userRequest 请求登录信息
     * @return 用户id
     */
    @GetMapping("/currentUser")
    public BaseResponse<User> currentUser(HttpServletRequest userRequest){
        //获取用户信息
        Object userObj = userRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if(null == userObj){
            throw new BusinessException(ErrorCode.NOT_ERROR,"未登录");
        }
        User user = (User) userObj;
        //根据用户id查询用户最新信息,并脱敏
        User safetyUser = userService.getSafetyUser(userService.getById(user.getId()));
        return ResultUtil.success(safetyUser,"登录成功");
    }

    /**
     * 退出用户
     * @param userRequest 请求登录信息
     * @return 用户id
     */
    @GetMapping("/userLoginOut")
    public BaseResponse<String> userLoginOut(HttpServletRequest userRequest){
        return userService.userLoginOut(userRequest);
    }

    /**
     * 查询用户接口
     * @param username 用户名
     * @param request 用户信息
     * @return 用户集合
     */
    @GetMapping("searchUsers")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if (isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_ERROR,"无管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> list = userService.list(queryWrapper);
        List<User> userList = list.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtil.success(userList,"查询成功");
    }

    /**
     * 删除用户接口
     * @param userDeleteRequest 用户id
     * @param request 用户信息
     * @return true:删除成功 false:删除失败
     */
    @PostMapping("deleteUser")
    public boolean deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request){
        if (isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_ERROR,"无管理员权限");
        }
        if (userDeleteRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.NULL_ERROR,"该用户不存在");
        }
        return userService.removeById(userDeleteRequest.getId());
    }

    /**
     * 用户鉴权
     * @param request 用户信息
     * @return true:无权限 false:有权限
     */
    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user == null || user.getUserRole() < ADMIN_ROLE;
    }

}
