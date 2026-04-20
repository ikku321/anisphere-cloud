package com.iikun.anisocial.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import com.iikun.anisocial.dto.PostCreateDTO;
import com.iikun.anisocial.entity.UserPost;
import com.iikun.anisocial.service.UserPostService;
import com.iikun.common.base.Result;
import com.iikun.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 用户动态/说说控制器
 */
@RestController // Spring MVC 控制器注解
@RequestMapping("/social/post") // 指定接口根路径
@Tag(name = "动态管理", description = "提供动态发布、删除、获取动态列表等功能") // Swagger 文档标签
public class UserPostController {

    @Resource // 注入依赖
    private UserPostService userPostService; // 用户动态服务

    /**
     * 发布动态
     *
     * @param createDTO 包含动态内容和媒体资源的 DTO
     * @return 发布成功的动态信息
     */
    @PostMapping("/create") // HTTP POST 方法映射
    @Operation(summary = "发布动态", description = "当前登录用户发布一条新动态") // Swagger 文档说明
    public Result<UserPost> createPost(@RequestBody PostCreateDTO createDTO) {
        String userId = UserContext.getUser().getUid(); // 获取当前登录用户 ID
        return userPostService.createPost(userId, createDTO.getContent(), createDTO.getMedia()); // 执行发布逻辑
    }

    /**
     * 删除动态
     *
     * @param postId 业务动态 ID
     * @return 删除结果
     */
    @DeleteMapping("/delete") // HTTP DELETE 方法映射
    @Operation(summary = "删除动态", description = "当前用户删除自己发布的某条动态") // Swagger 文档说明
    public Result<Void> deletePost(@RequestParam String postId) {
        String userId = UserContext.getUser().getUid(); // 获取当前登录用户 ID
        return userPostService.deletePost(userId, postId); // 执行删除操作
    }

    /**
     * 获取指定用户的动态列表
     *
     * @param userId 指定用户 ID
     * @param page   当前页码
     * @param size   每页记录数
     * @return 动态分页结果
     */
    @GetMapping("/user-list") // HTTP GET 方法映射
    @Operation(summary = "获取指定用户动态", description = "分页获取指定用户发布的公开动态") // Swagger 文档说明
    public Result<Page<UserPost>> getUserPosts(@RequestParam String userId,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return userPostService.getUserPosts(userId, page, size); // 执行分页查询
    }

    /**
     * 获取全站动态列表
     *
     * @param page 当前页码
     * @param size 每页记录数
     * @return 全站动态分页结果
     */
    @GetMapping("/global-list") // HTTP GET 方法映射
    @Operation(summary = "获取全站动态", description = "分页获取全站所有用户的公开动态") // Swagger 文档说明
    public Result<Page<UserPost>> getGlobalPosts(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return userPostService.getGlobalPosts(page, size); // 执行全站分页查询
    }

    /**
     * 获取当前登录用户的所有动态列表
     *
     * @return 动态列表
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的动态", description = "获取当前登录用户发布的动态列表")
    public Result<List<UserPost>> getMyPosts() {
        String userId = UserContext.getUser().getUid();
        return userPostService.getUserPosts(userId);
    }

    /**
     * 批量删除当前用户的动态
     *
     * @param postIds 动态业务 ID 列表
     * @return 操作结果
     */
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除动态", description = "批量删除当前用户发布的指定动态")
    public Result<Void> batchDeletePosts(@RequestBody List<String> postIds) {
        String userId = UserContext.getUser().getUid();
        for (String postId : postIds) {
            userPostService.deletePost(userId, postId);
        }
        return Result.success();
    }

    /**
     * 更新动态可见状态
     *
     * @param postId 动态 ID
     * @param status 状态：1-公开，0-隐藏
     * @return 操作结果
     */
    @PutMapping("/{postId}/visibility")
    @Operation(summary = "修改动态可见性", description = "修改指定动态的显示/隐藏状态")
    public Result<Void> updatePostVisibility(@PathVariable String postId, @RequestParam Integer status) {
        String userId = UserContext.getUser().getUid();
        return userPostService.updatePostStatus(postId, userId, status);
    }

    /**
     * 根据动态 ID 获取动态详情
     *
     * @param postId 动态业务 ID
     * @return 动态对象
     */

    /**
     * 手动更新点赞数（通常供其他服务或内部回调调用）
     *
     * @param postId 业务动态 ID
     * @param delta  增量（+1 或 -1）
     * @return 操作结果
     */
    @PostMapping("/update-like") // HTTP POST 方法映射
    @Operation(summary = "更新点赞数", description = "手动增减指定动态的点赞计数") // Swagger 文档说明
    public Result<Void> updateLikeCount(@RequestParam String postId, @RequestParam Integer delta) {
        return userPostService.updateLikeCount(postId, delta); // 执行更新操作
    }

    /**
     * 手动更新评论数（通常供评论服务调用）
     *
     * @param postId 业务动态 ID
     * @param delta  增量（+1 或 -1）
     * @return 操作结果
     */
    @PostMapping("/update-comment") // HTTP POST 方法映射
    @Operation(summary = "更新评论数", description = "手动增减指定动态的评论计数") // Swagger 文档说明
    public Result<Void> updateCommentCount(@RequestParam String postId, @RequestParam Integer delta) {
        return userPostService.updateCommentCount(postId, delta); // 执行更新操作
    }
}
