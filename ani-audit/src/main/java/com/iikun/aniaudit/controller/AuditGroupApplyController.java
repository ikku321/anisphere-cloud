package com.iikun.aniaudit.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class AuditGroupApplyController {



}
