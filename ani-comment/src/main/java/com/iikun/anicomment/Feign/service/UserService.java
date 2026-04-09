package com.iikun.anicomment.Feign.service;

import com.iikun.anicomment.Feign.client.UserFeignClient;
import com.iikun.anicomment.entity.DTO.UserDTO;
import com.iikun.common.base.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Service
public class UserService {

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 获取用户数据
     *
     * @param uid 用户id
     * @return 用户信息
     */
    public Result<UserDTO> getUserById(@RequestParam String uid) {
        return userFeignClient.getUserById(uid);
    }

    /**
     * 根据token获取用户信息
     *
     * @return 返回数据
     */
    public Result<UserDTO> getByTokenUserInfo() {
        return userFeignClient.getUserByToken();
    }

    /**
     * 验证当前账号权限是否属于管理员权限
     */
    public Result<?> isAdmin() {
        return userFeignClient.isAdmin();
    }
}
