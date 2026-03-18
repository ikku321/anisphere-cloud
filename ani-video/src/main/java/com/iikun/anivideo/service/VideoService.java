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

    /**
     * 修改视频信息的可见状态
     *
     * @param visible 数值（0：可见，1：隐藏）
     */
    void modifiVideoVisible(Integer visible, String videoId);

    /**
     * 修改视频简介
     *
     * @param description 视频简介
     * @param videoId     视频id
     */
    void updateVideoDescription(String description, String videoId);

    /**
     * 修改视频标题
     *
     * @param videoTitle 视频标题
     * @param videoId    视频id
     */
    void modifiVideoVideoTitle(String videoTitle, String videoId);
}
