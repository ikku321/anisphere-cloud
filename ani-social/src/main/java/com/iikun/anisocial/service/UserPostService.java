package com.iikun.anisocial.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anisocial.entity.UserPost;
import com.iikun.common.base.Result;
import java.util.List;
import java.util.Map;

/**
 * 用户动态/说说业务逻辑接口
 */
public interface UserPostService extends IService<UserPost> {

    /**
     * 发布动态
     *
     * @param userId  用户 ID
     * @param content 文本内容
     * @param media   媒体附件列表
     * @return 发布的动态信息
     */
    Result<UserPost> createPost(String userId, String content, List<Map<String, Object>> media);

    /**
     * 删除动态
     *
     * @param userId 用户 ID
     * @param postId 业务动态 ID
     * @return 结果
     */
    Result<Void> deletePost(String userId, String postId);

    /**
     * 分页获取用户动态列表
     *
     * @param userId 用户 ID
     * @param page   当前页
     * @param size   每页大小
     * @return 动态分页数据
     */
    Result<Page<UserPost>> getUserPosts(String userId, int page, int size);

    /**
     * 分页获取所有公开动态列表
     *
     * @param page 当前页
     * @param size 每页大小
     * @return 动态分页数据
     */
    Result<Page<UserPost>> getGlobalPosts(int page, int size);

    /**
     * 更新点赞数
     *
     * @param postId 业务动态 ID
     * @param delta  增量（+1 或 -1）
     * @return 结果
     */
    Result<Void> updateLikeCount(String postId, Integer delta);

    /**
     * 更新评论数
     *
     * @param postId 业务动态 ID
     * @param delta  增量（+1 或 -1）
     * @return 结果
     */
    Result<Void> updateCommentCount(String postId, Integer delta);
}
