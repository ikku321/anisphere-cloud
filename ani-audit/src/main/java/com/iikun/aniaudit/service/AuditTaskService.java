package com.iikun.aniaudit.service;

import com.iikun.aniaudit.entity.AuditTask;

import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 审核接口定义类
 */
public interface AuditTaskService {

    /**
     * 获取所有待审核的视频
     *
     * @return 返回列表
     */
    List<AuditTask> getAuditList();

    /**
     * 新增审核任务
     *
     * @param videoId 待审核视频id
     */
    void newAuditTask(String videoId);

    void claimTask(String videoId);

    void completeTask(String videoId);

    Map<String, Object> adminPage(Integer pageNum, Integer pageSize, Integer status, String videoId, String auditorId);

    AuditTask adminGetByVideoId(String videoId);

    void adminAssignAuditor(String videoId, String auditorId);

    void adminForceComplete(String videoId);

    Map<String, Object> adminSummary();
}
