package com.iikun.anichat.fegin.fallback;

import com.iikun.anichat.entity.dto.UserDTO;
import com.iikun.anichat.fegin.client.UserFeignClient;
import com.iikun.common.base.Result;
import org.springframework.stereotype.Component;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Component
public class UserFeignFallback implements UserFeignClient {
    @Override
    public Result<UserDTO> getUserById(String id) {
        return Result.failed("未知用户");
    }

    @Override
    public Result<UserDTO> getUserByToken() {
        return Result.failed("获取用户信息失败!");
    }
}
