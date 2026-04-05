package com.iikun.anisocial.feign.fallback;

import com.iikun.anisocial.dto.UserDTO;
import com.iikun.anisocial.feign.client.UserFeignClient;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
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
    public Result<UserDTO> find(String uid) {
        if (uid == null) {
            throw new ServiceException("用户id不能为空!");
        }
        return Result.failed("获取用户信息失败!");
    }
}
