package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.VideoPlayHistoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 视频播放历史服务接口
 * <p>
 * 处理用户视频播放进度和历史记录相关业务逻辑
 * </p>
 */
public interface VideoPlayHistoryService {

    void record(String userId, String videoId, Integer lastPosition);

    VideoPlayHistoryEntity detail(String userId, String videoId);

    Map<String, Object> pageByUser(String userId, Integer pageNum, Integer pageSize);

    Map<String, Object> adminPage(Integer pageNum, Integer pageSize, String userId, String videoId);

    void deleteOne(String userId, String videoId);

    void clearByUser(String userId);

}
