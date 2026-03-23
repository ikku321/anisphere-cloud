package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.TagEntity;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 视频标签表接口定义类
 */
public interface TagService {

    /**
     * 新增标签内容
     *
     * @param tag 标签名称
     */
    void insertTag(String tag);

    /**
     * 删除视频标签
     *
     * @param tagId 视频标签id
     */
    void deleteTag(String tagId);

    /**
     * 查询所有标签
     *
     * @return 标签列表
     */
    List<TagEntity> allTag();

    /**
     * 根据标签名称查询标签
     *
     * @param name 标签名称
     * @return 返回数据
     */
    List<TagEntity> selectByTagName(String name);

    /**
     * 根据标签id查询
     *
     * @param tagId 标签id
     * @return 返回内容
     */
    TagEntity selectByTagId(String tagId);
}
