package com.iikun.userservice.service;

import com.iikun.common.base.Result;
import com.iikun.userservice.domain.dto.RegisterDTO;
import com.iikun.userservice.domain.dto.UserInfoDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

/**
 * author iikun
 * time 2025/9/17 14:27
 * version 1.0.0
 * msg: 用户业务操作接口定义
 */
public interface UserService {

    /**
     * 新用户注册
     * @param registerDTO 注册信息实体
     */
    Result register(@Valid RegisterDTO registerDTO);

    /**
     * 登录
     * @param username 用户名称/手机号码
     * @param password 密码
     * @param ip 登录IP（可为空）
     * @param device 登录设备信息（可为空）
     * @return 1
     */
    Result login(String username, String password, String ip, String device);

    /**
     * 查询用户基本信息
     * @param userid 用户id
     * @return 用户基本信息
     */
    Result info(String userid);

    /**
     * 修改当前用户邮箱
     * @param email 邮箱
     * @return 返回状态
     */
    Result updataEmail(String email, String userid);

    /**
     * 修改当前用户的手机号
     * @param newPhone 新手机号
     * @return 1
     */
    Result updatePhone(String newPhone, String userid);

    /**
     * 修改当前用户昵称
     * @param newNickname 新昵称
     * @return 返回结果状态
     */
    Result updateNickName(@Valid String newNickname, String userid);

    /**
     * 修改当前用户的密码
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @param userId 用户id
     * @return 返回结果状态
     */
    Result updatePwd(String oldPwd, String newPwd, String userId);

    /**
     * 根据uid查询用户信息
     * @param uid 用户uid
     * @return 返回用户信息
     */
    UserInfoDTO findUidInfo(String uid);
}
