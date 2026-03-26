package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.TagEntity;

import java.util.List;
import java.util.Map;

/**
 * 视频标签关系服务接口
 * <p>
 * 处理视频与标签的多对多关系管理
 * </p>
 */
public interface VideoTagService {

    /**
     * 为视频添加标签
     *
     * @param videoId 视频id
     * @param tagId   标签id
     */
    void addVideoTag(String videoId, Integer tagId);

    /**
     * 删除视频标签关联
     *
     * @param videoTagId 视频标签关联id
     */
    void deleteVideoTag(String videoTagId);
}
