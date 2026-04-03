package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.feign.client.AuditTaskFeignClient;
import com.iikun.anivideo.service.AuditTaskService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 审核业务逻辑接口实现类
 */
@Slf4j
@Service
public class AuditTaskServiceImpl implements AuditTaskService {

    @Resource
    private AuditTaskFeignClient auditTaskFeignClient;

    @Override
    public void newAuditTask(String videoId) {
        Result<?> result = auditTaskFeignClient.newAuditTask(videoId);

        if (result == null || result.getCode() != 200) {
            log.error("新增审核任务失败，videoId={}, result={}", videoId, result);
            throw new ServiceException("审核任务创建失败");
        }
    }
}
