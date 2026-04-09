package com.iikun.userservice.service.impl;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.JwtUtil;
import com.iikun.common.utils.PasswordUtil;
import com.iikun.common.utils.Utils;
import com.iikun.userservice.domain.dto.RegisterDTO;
import com.iikun.userservice.domain.dto.UserInfoDTO;
import com.iikun.userservice.entity.User;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.UserLoginLogService;
import com.iikun.userservice.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * author iikun
 * time 2025/9/17 14:28
 * version 1.0.0
 * msg: 用户业务操作接口实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserLoginLogService userLoginLogService;

    /**
     * 新用户注册
     *
     * @param registerDTO 注册信息实体
     * @return 1
     */
    @Override
    public Result register(RegisterDTO registerDTO) {
        // 生成用户id
        String userId = Utils.shortUUID();

        User user = new User();
        user.setNickname("新用户" + Utils.shortUUID());
        user.setUserId(userId);
        user.setUsername(registerDTO.getUsername());
        user.setPassword(PasswordUtil.encode(registerDTO.getPassword()));
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        // 一般用户注册都是普通用户，更改权限为《1》
        user.setRole(1);

        try {
            int register = userMapper.register(user);
            if (register > 0) {
                // 生成token

                // 常用的信息
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", String.valueOf(user.getUserId()));
                claims.put("username", String.valueOf(user.getUsername()));
                claims.put("role", String.valueOf(user.getRole()));
                log.info("register-> calims存值: userId = {}", user.getUserId());
                log.info("register-> calims存值: username = {}", user.getUsername());
                log.info("register-> calims存值: role = {}", user.getRole());


                String token = jwtUtil.generateToken(user.getUserId(), claims);
                log.info("用户token: {}", token);
                HashMap<String, Object> tokenMap = new HashMap<>();
                tokenMap.put("token", token);
                // 返回注册后的用户信息（只返回必要字段）
                return Result.success(tokenMap, "注册成功!");
            }
        } catch (DuplicateKeyException e) {
            // 判断是邮箱还是手机号冲突
            if (e.getMessage().contains("user.email")) {
                return Result.failed("该邮箱已经绑定其他账号!");
            } else if (e.getMessage().contains("user.phone")) {
                return Result.failed("该号码已经绑定其他账号!");
            } else if (e.getMessage().contains("user.uk_user_name")) {
                return Result.failed("该用户名称已经存在?");
            } else if (e.getMessage().contains("uk_user_id")) {
                return Result.failed("用户唯一ID冲突，请重试!");
            }
            return Result.failed(e.getMessage());
        } catch (Exception e) {
            return Result.failed("注册失败，原因: " + e.getMessage());
        }
        return Result.failed("未知错误!");
    }


    /**
     * 登录
     *
     * @param username 用户名称/手机号码
     * @param password 密码
     * @param ip       登录IP
     * @param device   设备信息
     * @return 1
     */
    @Override
    public Result login(String username, String password, String ip, String device) {
        // 登录日志：0失败 1成功（无论成功/失败，都尽量写一条日志，便于审计与排查）
        try {
            // 根据用户名或手机号查用户
            User user = userMapper.findByUsername(username);
            log.info("登录查询的用户信息: {}", user);
            if (user == null) {
                throw new ServiceException("用户不存在");
            }

            // 校验密码
            if (!PasswordUtil.matches(password, user.getPassword())) {
                throw new ServiceException("密码不正确");
            }

            // 生成 token
            // 常用的信息
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", String.valueOf(user.getUserId()));
            claims.put("username", String.valueOf(user.getUsername()));
            claims.put("role", String.valueOf(user.getRole()));
            log.info("login-> calims存值: userId = {}", user.getUserId());
            log.info("login-> calims存值: username = {}", user.getUsername());
            log.info("login-> calims存值: role = {}", user.getRole());

            String token = jwtUtil.generateToken(user.getUserId(), claims);
            log.info("登录生成的token: ${}", token);
            HashMap<String, Object> loginToken = new HashMap<>();
            loginToken.put("token", token);

            // 写入登录成功日志（uid 使用 user.user_id 业务ID）
            try {
                userLoginLogService.record(user.getUserId(), ip, device, 1);
            } catch (Exception e) {
                // 登录成功本身不应因为日志失败而失败，这里仅记录日志
                log.warn("写入登录成功日志失败: {}", e.getMessage());
            }

            return Result.success(loginToken, "登录成功");
        } catch (ServiceException e) {
            // 写入登录失败日志：如果能根据 username 查到 uid 则记录，否则忽略
            try {
                User user = userMapper.findByUsername(username);
                if (user != null && user.getUserId() != null) {
                    userLoginLogService.record(user.getUserId(), ip, device, 0);
                }
            } catch (Exception ex) {
                log.warn("写入登录失败日志失败: {}", ex.getMessage());
            }
            throw e;
        }
    }


    /**
     * 查询用户基本信息
     *
     * @param userid 用户id
     * @return 用户基本信息
     */
    @Override
    public Result info(String userid) {
        try {
            UserInfoDTO user = userMapper.foundUserInfo(userid);
            if (user == null) {
                return Result.failed("用户不存在?");
            } else {
                HashMap<String, Object> userInfo = new HashMap<>();
                userInfo.put("user", user);
                log.info("获取到的用户数据: {}", userInfo);
                return Result.success(userInfo);
            }
        } catch (DataAccessException e) {
            log.debug("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }


    /**
     * 修改当前用户邮箱
     *
     * @param email 邮箱
     * @return 返回状态
     */
    @Override
    public Result updataEmail(String email, String userid) {
        int updateUserEmail = userMapper.updateUserEmail(email, userid);
        log.info("updateUserEmail: {}, userid: {}, \n email: {}", updateUserEmail, userid, email);
        if (updateUserEmail > 0) {
            return Result.success(null);
        } else {
            return Result.failed("修改失败?");
        }
    }

    /**
     * 修改当前用户的手机号
     *
     * @param newPhone 新手机号
     * @return 1
     */
    @Override
    public Result updatePhone(String newPhone, String userid) {
        int updateUserPhone = userMapper.updateUserPhone(newPhone, userid);
        if (updateUserPhone > 0) {
            return Result.success(null);
        } else {
            return Result.failed("修改失败!");
        }
    }

    /**
     * 修改当前用户昵称
     *
     * @param newNickname 新昵称
     * @return 1
     */
    @Override
    public Result updateNickName(String newNickname, String userid) {
        int updateUserNickName = userMapper.updateUserNickName(newNickname, userid);
        if (updateUserNickName > 0) {
            return Result.success(null);
        } else {
            return Result.failed("修改失败?");
        }
    }

    /**
     * 修改当前用户的密码
     *
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @param userId 用户id
     * @return 1
     */
    @Override
    public Result updatePwd(String oldPwd, String newPwd, String userId) {
        // 查询用户是否存在
        User userById = userMapper.getUserById(userId);
        log.info("update pwd: {}", userById);
        if (userById == null) {
            return Result.failed("用户未登录或token失效?");
        }
        // 判断旧密码是否相同?
        if (!PasswordUtil.matches(oldPwd, userById.getPassword())) {
            return Result.failed("旧密码错误?");
        }

        // 修改密码,将密码加密好
        String encodePwd = PasswordUtil.encode(newPwd);
        // 执行修改
        int updateUserPwd = userMapper.updateUserPwd(encodePwd, userId);
        if (updateUserPwd > 0) {
            return Result.success(null);
        } else {
            return Result.failed("修改密码失败?");
        }
    }

    /**
     * 根据uid查询用户信息
     *
     * @param uid 用户uid
     * @return 返回用户信息
     */
    @Override
    public UserInfoDTO findUidInfo(String uid) {
        try {
            if (userMapper.selectByUserId(uid) <= 0) {
                throw new ServiceException("查询失败! 该用户不存在?");
            }
            // 查询
            UserInfoDTO userByUidInfo = userMapper.findUserByUidInfo(uid);
            if (userByUidInfo == null) {
                throw new ServiceException("查询用户数据失败!");
            }
            return userByUidInfo;
        } catch (DataAccessException e) {
            log.debug("数据库异常: " + e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    /**
     * 根据用户uid获取用户信息
     *
     * @param uid 用户uid
     * @return 返回用户信息
     */
    @Override
    public UserInfoDTO foundByTokenUserInfo(String uid) {
        try {
            UserInfoDTO userInfoDTO = userMapper.foundUserInfo(uid);
            if (userInfoDTO == null) {
                throw new ServiceException("查询数据为空");
            }
            return userInfoDTO;
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }
}























