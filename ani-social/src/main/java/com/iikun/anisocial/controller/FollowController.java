package com.iikun.anisocial.controller;

import com.iikun.anisocial.entity.Follow;
import com.iikun.anisocial.service.FollowService;
import com.iikun.common.base.Result;
import com.iikun.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 关注/粉丝控制器
 */
@RestController // Spring MVC 控制器注解
@RequestMapping("/social/follow") // 指定接口根路径
@Tag(name = "关注管理", description = "提供关注用户、取消关注、获取粉丝列表等功能") // Swagger 文档标签
public class FollowController {

    @Resource // 注入依赖
    private FollowService followService; // 关注/粉丝服务

    /**
     * 关注用户
     *
     * @param targetUser 被关注的目标用户 ID
     * @return 关注结果
     */
    @PostMapping("/add") // HTTP POST 方法映射
    @Operation(summary = "关注用户", description = "关注指定的用户") // Swagger 文档说明
    public Result<Void> follow(@RequestParam String targetUser) {
        String userId = UserContext.getUser().getUid(); // 获取当前登录用户 ID
        return followService.follow(userId, targetUser); // 执行关注操作
    }

    /**
     * 取消关注
     *
     * @param targetUser 被取消关注的目标用户 ID
     * @return 取消关注结果
     */
    @DeleteMapping("/remove") // HTTP DELETE 方法映射
    @Operation(summary = "取消关注", description = "取消对指定用户的关注") // Swagger 文档说明
    public Result<Void> unfollow(@RequestParam String targetUser) {
        String userId = UserContext.getUser().getUid(); // 获取当前登录用户 ID
        return followService.unfollow(userId, targetUser); // 执行取消关注操作
    }

    /**
     * 获取当前用户的关注列表
     *
     * @return 关注列表
     */
    @GetMapping("/following") // HTTP GET 方法映射
    @Operation(summary = "获取关注列表", description = "获取当前用户关注的所有用户") // Swagger 文档说明
    public Result<List<Follow>> getFollowingList() {
        String userId = UserContext.getUser().getUid(); // 获取当前登录用户 ID
        return followService.getFollowingList(userId); // 获取并返回列表
    }

    /**
     * 获取当前用户的粉丝列表
     *
     * @return 粉丝列表
     */
    @GetMapping("/followers") // HTTP GET 方法映射
    @Operation(summary = "获取粉丝列表", description = "获取所有关注了当前用户的用户") // Swagger 文档说明
    public Result<List<Follow>> getFollowersList() {
        String userId = UserContext.getUser().getUid(); // 获取当前登录用户 ID
        return followService.getFollowersList(userId); // 获取并返回列表
    }
}
