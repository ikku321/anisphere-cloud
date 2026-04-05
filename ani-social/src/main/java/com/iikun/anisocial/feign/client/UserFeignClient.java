package com.iikun.anisocial.feign.client;

import com.iikun.anisocial.feign.fallback.UserFeignFallback;
import com.iikun.common.base.Result;
import com.iikun.anisocial.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务 Feign 客户端
 */
@FeignClient(
        name = "user-service",
        url = "http://localhost:9090",
        fallback = UserFeignFallback.class
)
public interface UserFeignClient {

    /**
     * 根据用户 ID 查询用户基本信息
     *
     * @param uid 用户 ID
     * @return 返回封装好的用户信息 Result
     */
    @GetMapping("/user/find")
    // 对应 user-service 中的接口路径
    Result<UserDTO> find(@RequestParam("uid") String uid);
}
