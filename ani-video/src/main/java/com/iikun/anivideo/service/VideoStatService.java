package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.VideoStatEntity;

import java.util.Map;

/**
 * 视频统计服务接口
 * <p>
 * 处理视频播放量、点赞数、分享数、评论数等统计业务逻辑
 * </p>
 */
public interface VideoStatService {

    VideoStatEntity getOrInit(String videoId);

    void incrPlay(String videoId, Long delta);

    void incrLike(String videoId, Long delta);

    void incrShare(String videoId, Long delta);

    void incrComment(String videoId, Long delta);

    Map<String, Object> topPlay(Integer limit);
}
