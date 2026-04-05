package com.iikun.aniaudit.controller;

import com.iikun.aniaudit.entity.AuditTask;
import com.iikun.aniaudit.service.AuditTaskService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Tag(name = "审核管理", description = "处理视频上传审核")
@RestController
@RequestMapping("/audit-task")
@RequiredArgsConstructor
public class AuditTaskController {

    private final AuditTaskService auditTaskService;

    @Operation(summary = "新增审核任务")
    @PostMapping("/new")
    public Result<?> newAuditTask(@RequestParam String videoId) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        auditTaskService.newAuditTask(videoId);
        return Result.success();
    }

    @Operation(summary = "获取待审核列表")
    @GetMapping("/list")
    public Result<?> auditList() {
        return Result.success(auditTaskService.getAuditList());
    }

    @Operation(summary = "领取审核任务")
    @PostMapping("/claim")
    public Result<?> claim(@RequestParam String videoId) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        auditTaskService.claimTask(videoId);
        return Result.success();
    }

    @Operation(summary = "完成审核任务（已领取后标记完成）")
    @PostMapping("/complete")
    public Result<?> complete(@RequestParam String videoId) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        auditTaskService.completeTask(videoId);
        return Result.success();
    }

}




















