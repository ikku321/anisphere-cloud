package com.iikun.userservice.controller;

import com.iikun.common.base.Result;
import com.iikun.userservice.service.UserFollowService;
import com.iikun.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * author iikun
 * time 2025/9/19 23:28
 * version 1.0.0
 * msg: 用户关注/粉丝关系表控制层
 */
@Slf4j
@Tag(name = "关注/粉丝", description = "用户关注-粉丝操作")
@RestController
@RequestMapping("/user-follow")
@RequiredArgsConstructor
public class UserFollowController {

    private final UserFollowService userFollowService;

    /**
     * 关注用户
     *
     * @param followId 关注人id
     * @return 1
     */
    @Operation(summary = "关注用户/up主", description = "关注心意的用户，或视频号up主")
    @PostMapping("/attention")
    public Result<?> attention(
            HttpServletRequest request,
            @RequestParam(name = "followId") String followId
    ) {
        // 从token获取到操作者uid
        String uid = (String) request.getAttribute("uid");
        log.info("test found uid {}", uid);

        // 关注
        userFollowService.attention(uid, followId);
        return Result.success();
    }


    @Operation(summary = "取消关注", description = "取消关注指定得up主/创作者, 需要传入取消关注者id")
    @PostMapping("/cancel-attention")
    public Result<?> cancelAttention(
            HttpServletRequest request,
            @RequestParam(name = "followId") String followId
    ) {
        // 获取操作者uid
        String uid = (String) request.getAttribute("uid");
        log.info("取消关注 获取操作者uid: {}", uid);

        // 取消关注
        userFollowService.cancelAttention(uid, followId);
        return Result.success();
    }


    @Operation(summary = "获取用户关注列表", description = "获取当前操作者的关注列表，展示基本的被关注用户数据显示")
    @GetMapping("/follow-list")
    public Result<?> getUserFollow(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        return Result.success(userFollowService.selectAllFollow(uid));
    }
}




















