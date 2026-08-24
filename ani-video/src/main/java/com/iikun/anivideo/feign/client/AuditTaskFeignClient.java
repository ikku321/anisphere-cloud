package com.iikun.anivideo.feign.client;

import com.iikun.anivideo.feign.fallback.AuditTaskFeignFallBack;
import com.iikun.common.base.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 审核
 */
@FeignClient(
        name = "ani-audit",
        fallback = AuditTaskFeignFallBack.class
)
public interface AuditTaskFeignClient {

    /**
     * 新增审核任务
     */
    @PostMapping("/audit-task/new")
    Result<?> newAuditTask(@RequestParam String videoId);
}
