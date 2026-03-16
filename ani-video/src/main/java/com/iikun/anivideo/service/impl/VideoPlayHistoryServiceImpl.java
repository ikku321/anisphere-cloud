package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.entity.VideoPlayHistoryEntity;
import com.iikun.anivideo.service.VideoPlayHistoryService;
import org.springframework.stereotype.Service;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 视频播放历史服务实现
 */
@Service
public class VideoPlayHistoryServiceImpl implements VideoPlayHistoryService {
    @Override
    public boolean updateProgress(String userId, String videoId, Integer position) {
        return false;
    }

    @Override
    public VideoPlayHistoryEntity getProgress(String userId, String videoId) {
        return null;
    }
}
