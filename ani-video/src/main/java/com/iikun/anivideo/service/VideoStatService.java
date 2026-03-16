package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.VideoStatEntity;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
public interface VideoStatService {
    /**
     * 初始化视频统计数据
     */
    boolean initVideoStat(String videoId);

    /**
     * 增加播放量
     */
    void increasePlay(String videoId);

    /**
     * 增加点赞数
     */
    void increaseLike(String videoId);

    /**
     * 增加评论数
     */
    void increaseComment(String videoId);

    /**
     * 增加分享数
     */
    void increaseShare(String videoId);

    /**
     * 查询视频统计
     */
    VideoStatEntity getStat(String videoId);
}
