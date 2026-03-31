package com.iikun.anicomment.handle;

import com.iikun.anicomment.Feign.UserFeignClient;
import com.iikun.anicomment.entity.DTO.UserDTO;
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
}
