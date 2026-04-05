package com.iikun.animessage.feign.fallback;

import com.iikun.animessage.feign.client.UserFeignClient;
import com.iikun.animessage.feign.entity.dto.UserDTO;
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
