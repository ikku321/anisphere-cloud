package com.iikun.aniaudit.controller;

import com.iikun.aniaudit.service.AuditGroupApplyService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审核组申请表
 * <p>
 * 记录普通用户申请加入审核组的请求
 * 用于构建社区共治/志愿审核员体系
 *
 * <p>
 *
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Tag(name = "审核组申请表", description = "记录普通用户申请加入审核组的请求")
@RestController
@RequestMapping("/audit-group/apply")
@RequiredArgsConstructor
public class AuditGroupApplyController {

    private final AuditGroupApplyService auditGroupApplyService;

    @Operation(summary = "提交加入审核组申请")
    @PostMapping("/submit")
    public Result<?> submit(@RequestParam(required = false) String reason) {
        auditGroupApplyService.submit(reason);
        return Result.success();
    }

    @Operation(summary = "查看本人申请记录")
    @GetMapping("/mine")
    public Result<?> mine() {
        return Result.success(auditGroupApplyService.listMine());
    }

    @Operation(summary = "管理员：待审核申请列表")
    @GetMapping("/pending")
    public Result<?> pending() {
        return Result.success(auditGroupApplyService.listPendingForAdmin());
    }

    @Operation(summary = "管理员：审批申请（1通过 2拒绝）")
    @PostMapping("/review")
    public Result<?> review(@RequestParam Long id, @RequestParam Integer status) {
        if (id == null) throw new ServiceException("申请ID不能为空!");
        auditGroupApplyService.review(id, status);
        return Result.success();
    }

}
