package com.iikun.aniaudit.service;

import com.iikun.aniaudit.entity.AuditRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 视频审核记录表逻辑接口定义类
 */
public interface AuditRecordService {

    void submit(String videoId, Integer result, String comment);

    List<AuditRecordEntity> listByVideoId(String videoId);

    Map<String, Object> adminPage(Integer pageNum, Integer pageSize, String videoId, String auditorId, Integer result);
}
