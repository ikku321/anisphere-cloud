package com.iikun.anivideo.service;

import java.util.Map;

/**
 * 视频举报服务接口
 * <p>
 * 处理用户对视频的违规举报相关业务逻辑
 * </p>
 */
public interface VideoReportService {

    void submit(String videoId, String userId, String reason);

    Map<String, Object> page(Integer pageNum, Integer pageSize, Integer status, String videoId, String userId);

    void markHandled(Long reportId);
}
