package com.iikun.animessage.feign.client;

import com.iikun.animessage.feign.entity.dto.UserDTO;
import com.iikun.animessage.feign.fallback.UserFeignFallback;
import com.iikun.common.base.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@FeignClient(
        name = "user-service",
        url = "http://localhost:9099",
        fallback = UserFeignFallback.class
)
public interface UserFeignClient {

    @GetMapping("/user/find")
    Result<UserDTO> getUserById(@RequestParam String uid);

    /**
     * 根据token信息获取用户个人信息
     *
     * @return 返回用户实体数据
     */
    @GetMapping("/user/found-token")
    Result<UserDTO> getUserByToken();
}
