package com.iikun.userservice.controller;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.userservice.service.UserBrowsingHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author iikun
 * time 2025/9/19 23:07
 * version 1.0.0
 * msg:
 */
@RestController
@RequestMapping("/user-browsing-history")
@Tag(name = "用户浏览控制层", description = "用于记录用户点击各种浏览")
@RequiredArgsConstructor
public class UserBrowsingHistoryController {

    /** 用户浏览记录业务 */
    private final UserBrowsingHistoryService userBrowsingHistoryService;

    @Operation(summary = "记录浏览", description = "记录一次浏览行为")
    @PostMapping("/record")
    public Result<?> record(HttpServletRequest request,
                            @RequestParam Integer targetType,
                            @RequestParam Long targetId) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("token内容不能为空");
        }
        userBrowsingHistoryService.record(uid, targetType, targetId);
        return Result.success();
    }

    @Operation(summary = "删除浏览记录", description = "删除当前用户的一条浏览记录")
    @DeleteMapping("/delete")
    public Result<?> delete(HttpServletRequest request, @RequestParam Long recordId) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("token内容不能为空");
        }
        userBrowsingHistoryService.delete(uid, recordId);
        return Result.success();
    }

    @Operation(summary = "我的浏览记录（分页）", description = "分页查询当前用户浏览记录")
    @GetMapping("/page")
    public Result<?> page(HttpServletRequest request,
                          @RequestParam(required = false) Integer page,
                          @RequestParam(required = false) Integer size) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("token内容不能为空");
        }
        return Result.success(userBrowsingHistoryService.pageMyHistory(uid, page, size));
    }
}
