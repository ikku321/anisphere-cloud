package com.iikun.anivideo.service;

import com.iikun.anivideo.feign.client.AuditTaskFeignClient;
import com.iikun.common.base.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 审核服务业务接口定义类
 */
public interface AuditTaskService {

    // 新增审核任务
    void newAuditTask(String videoId);
}
