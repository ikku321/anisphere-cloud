package com.iikun.aniaudit.service;

import com.iikun.aniaudit.entity.dto.UserDTO;
import com.iikun.aniaudit.feign.client.UserFeignClient;
import com.iikun.common.base.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
     * 根据token获取用户信息
     *
     * @return 返回数据
     */
    public Result<UserDTO> getByTokenUserInfo() {
        return userFeignClient.getUserByToken();
    }

}
