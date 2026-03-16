package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.entity.VideoReportEntity;
import com.iikun.anivideo.service.VideoReportService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 * 视频举报服务实现
 */
@Service
public class VideoReportServiceImpl implements VideoReportService {
    @Override
    public boolean reportVideo(VideoReportEntity report) {
        return false;
    }

    @Override
    public List<VideoReportEntity> getPendingReports() {
        return List.of();
    }

    @Override
    public boolean handleReport(Long id) {
        return false;
    }
}
