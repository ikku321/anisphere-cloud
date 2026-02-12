package com.iikun.userservice.controller;

import com.iikun.common.annotation.Admin;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.userservice.service.UserService;
import com.iikun.userservice.service.impl.UserBlacklistServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * author iikun
 * time 2025/9/19 23:21
 * version 1.0.0
 * msg: 用户黑名单控制层
 */
@RestController
@RequestMapping("/user-blacklist")
@Tag(name = "黑名单", description = "用户黑名单操作")
public class UserBlacklistController {

    private final UserBlacklistServiceImpl userBlacklistService;

    public UserBlacklistController(UserBlacklistServiceImpl userBlacklistService) {
        this.userBlacklistService = userBlacklistService;
    }

    @Operation(summary = "加入黑名单", description = "将指定的用户列入黑名单")
    @PostMapping("/join-black")
    public Result<?> joinBlackList(HttpServletRequest request, @RequestParam String blacklistId) {
        String uid = (String) request.getAttribute("uid");
        if (blacklistId == null) throw new ServiceException("加入黑名单用户id不能为空!");
        userBlacklistService.jionBlack(uid, blacklistId);
        return Result.success();
    }

    @Operation(summary = "取消拉黑")
    @PostMapping("/cancel-blacklist")
    public Result<?> cancelBlackList(HttpServletRequest request, @RequestParam String blacklistId) {
        String uid = (String) request.getAttribute("uid");
        if (blacklistId == null) throw new ServiceException("加入黑名单用户id不能为空!");
        userBlacklistService.cancelBlackList(uid, blacklistId);
        return Result.success();
    }

    @Operation(summary = "查询当前账号下的拉黑名单")
    @GetMapping("/find-blacklist")
    public Result<?> findBlacklist(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        Map<String, Object> newBlackList = new HashMap<>();
        newBlackList.put("UserBlack", userBlacklistService.findBlackList(uid));
        return Result.success(newBlackList);
    }

    @Operation(summary = "查询是否已经拉黑该用户")
    @GetMapping("/find-isBlackList")
    public Result<?> findIsBlackList(HttpServletRequest request, @RequestParam String blacklistId) {
        String uid = (String) request.getAttribute("uid");
        return Result.success(userBlacklistService.findIsBlackList(uid, blacklistId));
    }


    @Admin
    @Operation(summary = "查询所有黑名单列表")
    @GetMapping("/all")
    public Result<?> getAllBlacklist(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null) throw new ServiceException("token内容不能为空！");
        return Result.success(userBlacklistService.all());
    }
}

































