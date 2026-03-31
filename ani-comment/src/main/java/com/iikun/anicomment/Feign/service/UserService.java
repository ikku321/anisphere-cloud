package com.iikun.anicomment.Feign.service;

import com.iikun.anicomment.Feign.UserFeignClient;
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

    public Result<UserDTO> getUserById(@RequestParam String uid) {
        return userFeignClient.getUserById(uid);
    }
}
