package com.iikun.aniaudit.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
