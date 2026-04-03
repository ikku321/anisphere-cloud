package com.iikun.anivideo.feign.fallback;

import com.iikun.anivideo.feign.client.AuditTaskFeignClient;
import com.iikun.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Slf4j
@Component
public class AuditTaskFeignFallBack implements AuditTaskFeignClient {

    @Override
    public Result<?> newAuditTask(String videoId) {
        log.error("调用 AuditTaskFeignClient.newAuditTask 失败，videoId={}", videoId);
        return Result.failed("审核任务创建失败（服务降级）");
    }
}
