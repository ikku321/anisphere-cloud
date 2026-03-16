package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.VideoReportEntity;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
public interface VideoReportService {

    /**
     * 举报视频
     */
    boolean reportVideo(VideoReportEntity report);

    /**
     * 查询待处理举报
     */
    List<VideoReportEntity> getPendingReports();

    /**
     * 处理举报
     */
    boolean handleReport(Long id);
}
