package com.iikun.userservice.controller;

import com.iikun.common.annotation.Admin;
import com.iikun.common.base.Result;
import com.iikun.userservice.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Admin
    @GetMapping("/user-all")
    @Operation(summary = "查询所有用户列表", description = "分页查询所有用户列表信息（仅管理员）")
    public Result<?> getAllUser(@RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size) {
        return Result.success(adminService.pageUsers(page, size));
    }


}




















