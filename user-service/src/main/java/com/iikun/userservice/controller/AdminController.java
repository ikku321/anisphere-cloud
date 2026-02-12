package com.iikun.userservice.controller;

import com.iikun.common.base.Result;
import com.iikun.common.config.SecurityConfig;
import com.iikun.userservice.entity.User;
import com.iikun.userservice.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/user-all")
    @Operation(summary = "查询所有用户列表", description = "查询所有用户列表信息")
    public Result getAllUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null || authentication.getPrincipal() != null) {
            return Result.failed("");
        }
        return Result.success(null);
    }


}




















