package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.VideoEntity;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
public interface VideoService {
    /**
     * 上传视频信息， 保存视频内容信息
     *
     * @param videoEntity 视频数据实体类
     */
    void save(VideoEntity videoEntity);
}
