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

    /**
     * 审核员：分页获取待审核任务列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 分页数据
     */
    Map<String, Object> auditorPagePending(Integer pageNum, Integer pageSize);

    /**
     * 审核员：分页获取我领取的任务列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param status   状态（1进行中，2已完成）
     * @return 分页数据
     */
    Map<String, Object> auditorPageMyTasks(Integer pageNum, Integer pageSize, Integer status);
}
