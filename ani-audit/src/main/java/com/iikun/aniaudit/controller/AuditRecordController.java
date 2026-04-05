package com.iikun.aniaudit.controller;

import com.iikun.aniaudit.service.AuditRecordService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审核记录表
 * <p>
 * 记录审核员对视频的具体审核结果
 * 一条记录代表一次审核操作，可存在多条（多轮/多审）
 * <p>
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Tag(name = "审核记录表", description = "录审核员对视频的具体审核结果")
@RestController
@RequestMapping("/audit-record")
@RequiredArgsConstructor
public class AuditRecordController {

    private final AuditRecordService auditRecordService;

    @Operation(summary = "提交审核结果")
    @PostMapping("/submit")
    public Result<?> submit(@RequestParam String videoId,
                            @RequestParam Integer result,
                            @RequestParam(required = false) String comment) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        auditRecordService.submit(videoId, result, comment);
        return Result.success();
    }

    @Operation(summary = "根据视频ID查询审核记录")
    @GetMapping("/list")
    public Result<?> list(@RequestParam String videoId) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        return Result.success(auditRecordService.listByVideoId(videoId));
    }



}
