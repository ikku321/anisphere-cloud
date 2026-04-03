package com.iikun.aniaudit.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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
public class AuditRecordController {



}
