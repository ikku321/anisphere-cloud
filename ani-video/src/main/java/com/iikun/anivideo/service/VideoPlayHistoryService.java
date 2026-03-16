package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.VideoPlayHistoryEntity;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
public interface VideoPlayHistoryService {
    /**
     * 更新播放进度（断点续播）
     */
    boolean updateProgress(String userId, String videoId, Integer position);

    /**
     * 查询播放进度
     */
    VideoPlayHistoryEntity getProgress(String userId, String videoId);
}
