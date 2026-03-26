package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.TagEntity;

import java.util.List;
import java.util.Map;

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
     * @param name 标签名称
     * @param type 标签类型（可选）
     */
    void insertTag(String name, String type);

    /**
     * 更新标签
     *
     * @param tagId 标签ID
     * @param name  标签名称
     * @param type  标签类型（可选）
     */
    void updateTag(Long tagId, String name, String type);

    /**
     * 删除视频标签
     *
     * @param tagId 视频标签ID
     */
    void deleteTag(Long tagId);

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
     * 根据标签ID查询
     *
     * @param tagId 标签ID
     * @return 返回内容
     */
    TagEntity selectByTagId(Long tagId);

    /**
     * 根据标签类型查询标签列表
     *
     * @param type 标签类型
     * @return 标签列表
     */
    List<TagEntity> getTagsByType(String type);

    /**
     * 获取热门标签
     *
     * @param limit 限制数量
     * @return 热门标签列表（包含使用次数）
     */
    List<Map<String, Object>> getHotTags(Integer limit);
}
