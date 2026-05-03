package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.VideoEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    /**
     * <p>删除视频</p>
     *
     * @param videoId 删除视频目标id
     */
    void deleteVideo(String videoId);

    /**
     * 查询所有视频列表
     *
     * @return 返回查询到的所有视频信息
     */
    List<VideoEntity> getVideoAll();


    /**
     * 根据标题查询视频信息
     *
     * @return 返回视频信息
     */
    List<VideoEntity> foundVideoInfo(String videoTitle);

    /**
     * 根据视频ID查询视频详情
     *
     * @param videoId 视频ID
     * @return 视频详情
     */
    VideoEntity getVideoById(String videoId);

    /**
     * 根据用户ID查询视频列表
     *
     * @param userId 用户ID
     * @return 视频列表
     */
    List<VideoEntity> getVideosByUserId(String userId);

    /**
     * 分页查询视频列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param keyword  关键词（可选）
     * @return 分页结果
     */
    Map<String, Object> getVideoPage(Integer pageNum, Integer pageSize, String keyword);

    Map<String, Object> adminGetVideoPage(Integer pageNum,
                                          Integer pageSize,
                                          String keyword,
                                          String userId,
                                          Integer status,
                                          Integer visible,
                                          Integer auditStatus);

    /**
     * 修改视频状态
     *
     * @param status  状态
     * @param videoId 视频ID
     */
    void updateVideoStatus(Integer status, String videoId);

    /**
     * 修改视频审核状态
     *
     * @param auditStatus 审核状态
     * @param videoId     视频ID
     */
    void updateAuditStatus(Integer auditStatus, String videoId);

    /**
     * 修改视频价格
     *
     * @param price   价格
     * @param videoId 视频ID
     */
    void updateVideoPrice(BigDecimal price, String videoId);

    /**
     * 批量删除视频
     *
     * @param videoIds 视频ID列表
     */
    void batchDeleteVideos(List<String> videoIds);

    /**
     * 获取热门视频列表
     *
     * @param limit 限制数量
     * @return 热门视频列表
     */
    List<VideoEntity> getHotVideos(Integer limit);

    /**
     * 获取推荐视频列表
     *
     * @param userId 用户ID
     * @param limit  限制数量
     * @return 推荐视频列表
     */
    List<VideoEntity> getRecommendVideos(String userId, Integer limit);

    /**
     * 根据视频id获取视频详情
     *
     * @param videoId 视频id
     * @return 返回视频详情
     */
    VideoEntity getVideoInfo(String videoId);

    /**
     * 查询指定用户正在审核中的视频列表。
     *
     * <p>判定条件：<code>user_id = uid AND status = 0</code>，按创建时间倒序。</p>
     * <p>用途：「我的」页面入口 —— 用户可以看到刚提交、尚未发布的视频进度。</p>
     *
     * @param uid 用户 ID
     * @return 审核中视频列表（可能为空列表）
     */
    List<VideoEntity> getMyAuditingVideos(String uid);
}
