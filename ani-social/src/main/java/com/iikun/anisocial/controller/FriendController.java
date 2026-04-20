package com.iikun.anisocial.controller;

import com.iikun.anisocial.entity.FriendGroup;
import com.iikun.anisocial.entity.FriendRelation;
import com.iikun.anisocial.service.FriendGroupMappingService;
import com.iikun.anisocial.service.FriendGroupService;
import com.iikun.anisocial.service.FriendRelationService;
import com.iikun.common.base.Result;
import com.iikun.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 好友关系控制器
 */
@RestController // Spring MVC 控制器注解
@RequestMapping("/social/friend") // 指定接口根路径
@Tag(name = "好友管理", description = "提供好友申请、分组、备注等社交功能") // Swagger 文档标签
public class FriendController {

    @Resource // 注入依赖
    private FriendRelationService friendRelationService; // 好友关系服务

    @Resource // 注入依赖
    private FriendGroupService friendGroupService; // 好友分组服务

    @Resource // 注入依赖
    private FriendGroupMappingService friendGroupMappingService; // 好友分组映射服务

    /**
     * 发送好友申请
     *
     * @param friendId 目标好友 ID
     * @return 操作结果
     */
    @PostMapping("/request") // HTTP POST 方法映射
    @Operation(summary = "发送好友申请", description = "向指定用户发送好友申请") // Swagger 文档说明
    public Result<Void> sendFriendRequest(@RequestParam String friendId) {
        String userId = UserContext.getUser().getUid(); // 从上下文获取当前登录用户 ID
        return friendRelationService.sendFriendRequest(userId, friendId); // 调用服务执行逻辑
    }

    /**
     * 处理好友申请
     *
     * @param friendId 申请人 ID
     * @param status   状态：1=接受，2=拒绝/拉黑
     * @return 处理结果
     */
    @PostMapping("/process") // HTTP POST 方法映射
    @Operation(summary = "处理好友申请", description = "接受、拒绝或拉黑来自他人的好友申请") // Swagger 文档说明
    public Result<Void> processFriendRequest(@RequestParam String friendId, @RequestParam Integer status) {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendRelationService.processFriendRequest(userId, friendId, status); // 执行处理逻辑
    }

    /**
     * 获取好友列表
     *
     * @return 当前用户的所有好友关系列表
     */
    @GetMapping("/list") // HTTP GET 方法映射
    @Operation(summary = "获取好友列表", description = "获取当前用户的所有已通过的好友") // Swagger 文档说明
    public Result<List<FriendRelation>> getFriendList() {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendRelationService.getFriendList(userId); // 获取并返回好友列表
    }

    /**
     * 修改好友备注
     *
     * @param friendId 好友 ID
     * @param remark   新的备注名
     * @return 修改结果
     */
    @PostMapping("/remark") // HTTP POST 方法映射
    @Operation(summary = "修改好友备注", description = "为指定的好友设置备注名") // Swagger 文档说明
    public Result<Void> updateRemark(@RequestParam String friendId, @RequestParam String remark) {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendRelationService.updateRemark(userId, friendId, remark); // 执行备注修改
    }

    /**
     * 删除好友
     *
     * @param friendId 好友 ID
     * @return 删除结果
     */
    @DeleteMapping("/delete") // HTTP DELETE 方法映射
    @Operation(summary = "删除好友", description = "解除与指定用户的好友关系（双向解除）") // Swagger 文档说明
    public Result<Void> deleteFriend(@RequestParam String friendId) {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendRelationService.deleteFriend(userId, friendId); // 执行删除操作
    }

    /**
     * 获取共同好友
     *
     * @param otherUserId 另一个用户的 ID
     * @return 共同好友的 userId 列表
     */
    @GetMapping("/mutual")
    @Operation(summary = "获取共同好友", description = "获取当前用户与指定用户的共同好友列表")
    public Result<List<String>> getMutualFriends(@RequestParam String otherUserId) {
        String userId = UserContext.getUser().getUid();
        return friendRelationService.getMutualFriends(userId, otherUserId);
    }

    /**
     * 创建好友分组
     *
     * @param groupName 分组名称
     * @return 新创建的分组信息
     */
    @PostMapping("/group/create") // HTTP POST 方法映射
    @Operation(summary = "创建好友分组", description = "创建一个新的好友分类分组") // Swagger 文档说明
    public Result<FriendGroup> createGroup(@RequestParam String groupName) {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendGroupService.createGroup(userId, groupName); // 执行创建操作
    }

    /**
     * 删除好友分组
     *
     * @param groupId 分组 ID
     * @return 删除结果
     */
    @DeleteMapping("/group/delete") // HTTP DELETE 方法映射
    @Operation(summary = "删除好友分组", description = "删除指定的分组，并清空该分组下的映射关系") // Swagger 文档说明
    public Result<Void> deleteGroup(@RequestParam Long groupId) {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendGroupService.deleteGroup(userId, groupId); // 执行删除操作
    }

    /**
     * 获取用户所有分组
     *
     * @return 分组列表
     */
    @GetMapping("/group/list") // HTTP GET 方法映射
    @Operation(summary = "获取好友分组列表", description = "获取当前用户创建的所有好友分组") // Swagger 文档说明
    public Result<List<FriendGroup>> getUserGroups() {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendGroupService.getUserGroups(userId); // 获取并返回分组列表
    }

    /**
     * 将好友添加到分组
     *
     * @param groupId  分组 ID
     * @param friendId 好友 ID
     * @return 添加结果
     */
    @PostMapping("/group/add-friend") // HTTP POST 方法映射
    @Operation(summary = "添加好友到分组", description = "将指定好友移入到某个分组中") // Swagger 文档说明
    public Result<Void> addFriendToGroup(@RequestParam Long groupId, @RequestParam String friendId) {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendGroupMappingService.addFriendToGroup(userId, groupId, friendId); // 执行添加操作
    }

    /**
     * 从分组移除好友
     *
     * @param groupId  分组 ID
     * @param friendId 好友 ID
     * @return 移除结果
     */
    @DeleteMapping("/group/remove-friend") // HTTP DELETE 方法映射
    @Operation(summary = "从分组移除好友", description = "将指定好友从某个分组中移除") // Swagger 文档说明
    public Result<Void> removeFriendFromGroup(@RequestParam Long groupId, @RequestParam String friendId) {
        String userId = UserContext.getUser().getUid(); // 获取当前用户 ID
        return friendGroupMappingService.removeFriendFromGroup(userId, groupId, friendId); // 执行移除操作
    }
}
