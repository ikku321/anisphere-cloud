package com.iikun.userservice.controller;

import com.iikun.common.base.Result;
import com.iikun.userservice.service.UserFollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @Operation(summary = "获取用户关注列表",
            description = "返回某用户「关注的人」列表。userId 为空时默认查当前登录用户。"
                    + "返回的每一行带 isMyFollowing 字段，标识当前登录用户是否也关注该行用户。")
    @GetMapping("/follow-list")
    public Result<?> getUserFollow(
            HttpServletRequest request,
            @RequestParam(name = "userId", required = false) String userId
    ) {
        String viewer = (String) request.getAttribute("uid");
        String target = (userId != null && !userId.isBlank()) ? userId : viewer;
        return Result.success(userFollowService.selectAllFollow(target, viewer));
    }


    @Operation(summary = "获取用户粉丝列表",
            description = "返回某用户的粉丝列表。userId 为空时默认查当前登录用户。"
                    + "返回的每一行带 isMyFollowing 字段，便于前端展示「回关 / 互相关注」。")
    @GetMapping("/fans-list")
    public Result<?> getUserFans(
            HttpServletRequest request,
            @RequestParam(name = "userId", required = false) String userId
    ) {
        String viewer = (String) request.getAttribute("uid");
        String target = (userId != null && !userId.isBlank()) ? userId : viewer;
        return Result.success(userFollowService.selectAllFans(target, viewer));
    }


    @Operation(summary = "查询是否已关注", description = "判断当前用户是否已关注指定 followId 用户")
    @GetMapping("/is-following")
    public Result<Boolean> isFollowing(
            HttpServletRequest request,
            @RequestParam(name = "followId") String followId
    ) {
        String uid = (String) request.getAttribute("uid");
        return Result.success(userFollowService.isFollowing(uid, followId));
    }
}




















