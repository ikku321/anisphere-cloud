package com.iikun.userservice.controller;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.userservice.service.UserLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * author iikun
 * time 2025/9/19 23:17
 * version 1.0.0
 * msg: 用户登录日志控制层
 */
@RestController
@RequestMapping("/user-login-log")
@Tag(name = "登录日志", description = "用户登录日志")
@RequiredArgsConstructor
public class UserLoginLogController {

    /** 登录日志业务 */
    private final UserLoginLogService userLoginLogService;

    @Operation(summary = "我的登录日志（分页）", description = "分页查询当前用户的登录日志")
    @GetMapping("/page")
    public Result<?> page(HttpServletRequest request,
                          @RequestParam(required = false) Integer page,
                          @RequestParam(required = false) Integer size) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("token内容不能为空");
        }
        return Result.success(userLoginLogService.pageMyLogs(uid, page, size));
    }

}
