package com.iikun.userservice.controller;

import com.iikun.common.annotation.Admin;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.userservice.domain.dto.UserInfoDTO;
import com.iikun.userservice.entity.User;
import com.iikun.userservice.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * author iikun
 * time 2025/9/20 23:49
 * version 1.0.0
 * msg: 用户服务管理控制层
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "用户管理", description = "该功能只有管理员才能使用, 管理所有用户信息")
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("/admin-login")
    @Operation(summary = "管理员专属登录Api", description = "用于管理员登录")
    public Result<?> adminLogin(@RequestParam(required = true) String username,
                                @RequestParam(required = true) String password
    ) {
        String token = adminService.adminLogin(username, password);
        HashMap<String, Object> loginToken = new HashMap<>();
        loginToken.put("token", token);
        return Result.success(loginToken);
    }

    @Admin
    @GetMapping("/user-all")
    @Operation(summary = "查询所有用户列表", description = "分页查询所有用户列表信息（仅管理员）")
    public Result<?> getAllUser(@RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size) {
        return Result.success(adminService.pageUsers(page, size));
    }

    @Admin
    @GetMapping("/user-page")
    @Operation(summary = "分页查询用户列表(支持筛选)", description = "支持按关键字、状态、角色筛选（仅管理员）")
    public Result<?> pageUsers(@RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer size,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Integer status,
                               @RequestParam(required = false) Integer role) {
        return Result.success(adminService.pageUsers(page, size, keyword, status, role));
    }

    @Admin
    @GetMapping("/user-detail")
    @Operation(summary = "查询用户详情", description = "根据 userId 查询用户详情（不返回 password）（仅管理员）")
    public Result<?> userDetail(@RequestParam String userId) {
        UserInfoDTO dto = adminService.getUserInfo(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("user", dto);
        return Result.success(data);
    }

    @Admin
    @PutMapping("/user/status")
    @Operation(summary = "修改用户状态", description = "0正常 1禁言 2封禁 3注销中（仅管理员）")
    public Result<?> updateUserStatus(@RequestParam String userId, @RequestParam Integer status) {
        adminService.updateUserStatus(userId, status);
        return Result.success();
    }

    @Admin
    @PutMapping("/user/role")
    @Operation(summary = "修改用户角色", description = "0管理员 1普通用户 2UP主 3审核员（仅管理员）")
    public Result<?> updateUserRole(@RequestParam String userId, @RequestParam Integer role) {
        adminService.updateUserRole(userId, role);
        return Result.success();
    }

    @Admin
    @PostMapping("/user/reset-password")
    @Operation(summary = "重置用户密码", description = "管理员为指定用户重置密码（仅管理员）")
    public Result<?> resetPassword(@RequestParam String userId, @RequestParam String newPassword) {
        adminService.resetPassword(userId, newPassword);
        return Result.success();
    }

    @Admin
    @PostMapping("/user/create")
    @Operation(summary = "创建用户", description = "管理员创建新用户（仅管理员）")
    public Result<?> createUser(@RequestBody CreateUserRequest request) {
        if (request == null) {
            throw new ServiceException("请求体不能为空");
        }
        String userId = adminService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getNickname(),
                request.getPhone(),
                request.getEmail(),
                request.getRole(),
                request.getStatus()
        );
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        return Result.success(data);
    }

    @Admin
    @PutMapping("/user/update")
    @Operation(summary = "更新用户信息", description = "支持更新基本字段（不包含角色/状态/密码）（仅管理员）")
    public Result<?> updateUser(@RequestParam String userId, @RequestBody UpdateUserRequest request) {
        if (request == null) {
            throw new ServiceException("请求体不能为空");
        }
        User update = new User();
        update.setUsername(request.getUsername());
        update.setNickname(request.getNickname());
        update.setPhone(request.getPhone());
        update.setEmail(request.getEmail());
        update.setAvatarUrl(request.getAvatarUrl());
        update.setGender(request.getGender());
        update.setBio(request.getBio());
        update.setBirthday(request.getBirthday());
        update.setCoins(request.getCoins());
        update.setLevel(request.getLevel());
        update.setExp(request.getExp());
        update.setVipId(request.getVipId());
        update.setOnlineStatus(request.getOnlineStatus());

        adminService.updateUser(userId, update);
        return Result.success();
    }

    @Data
    public static class CreateUserRequest {
        private String username;
        private String password;
        private String nickname;
        private String phone;
        private String email;
        private Integer role;
        private Integer status;
    }

    @Data
    public static class UpdateUserRequest {
        private String username;
        private String nickname;
        private String phone;
        private String email;
        private String avatarUrl;
        private Integer gender;
        private String bio;
        private LocalDate birthday;
        private Integer coins;
        private Integer level;
        private Integer exp;
        private Long vipId;
        private Integer onlineStatus;
    }

}






















