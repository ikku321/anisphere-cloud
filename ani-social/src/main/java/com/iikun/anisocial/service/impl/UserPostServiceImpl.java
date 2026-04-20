package com.iikun.anisocial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anisocial.entity.UserPost;
import com.iikun.anisocial.mapper.UserPostMapper;
import com.iikun.anisocial.service.UserPostService;
import com.iikun.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户动态/说说业务逻辑实现类
 */
@Slf4j // Lombok 注解：开启日志
@Service // Spring 服务类注解
public class UserPostServiceImpl extends ServiceImpl<UserPostMapper, UserPost> implements UserPostService {

    /**
     * 发布动态
     *
     * @param userId  用户 ID
     * @param content 文本内容
     * @param media   媒体附件列表
     * @return 发布的动态信息
     */
    @Override
    @Transactional // 开启事务处理
    public Result<UserPost> createPost(String userId, String content, List<Map<String, Object>> media) {
        // 1. 实例化动态实体
        UserPost post = new UserPost(); // 创建对象
        post.setPostId(UUID.randomUUID().toString().replace("-", "")); // 生成业务唯一 ID
        post.setUserId(userId); // 设置作者 ID
        post.setContent(content); // 设置文本内容
        post.setMedia(media); // 设置媒体 JSON 列表
        post.setLikeCount(0); // 初始化点赞数
        post.setCommentCount(0); // 初始化评论数
        post.setStatus(1); // 默认状态为正常（1）

        // 2. 保存到数据库
        boolean saved = this.save(post); // 执行插入操作
        return saved ? Result.success(post) : Result.failed("发布动态失败"); // 返回结果
    }

    /**
     * 删除动态
     *
     * @param userId 用户 ID
     * @param postId 业务动态 ID
     * @return 结果
     */
    @Override
    @Transactional // 开启事务
    public Result<Void> deletePost(String userId, String postId) {
        // 1. 构造查询条件
        LambdaQueryWrapper<UserPost> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(UserPost::getPostId, postId) // 匹配业务 ID
                    .eq(UserPost::getUserId, userId); // 匹配作者 ID（权限检查）
        
        // 2. 执行删除操作
        boolean removed = this.remove(queryWrapper); // 执行删除
        return removed ? Result.success() : Result.failed("删除动态失败，可能不存在或无权操作"); // 返回结果
    }

    /**
     * 分页获取指定用户的动态列表
     *
     * @param userId 用户 ID
     * @param page   当前页码
     * @param size   每页记录数
     * @return 包含分页数据的动态结果
     */
    @Override
    public Result<Page<UserPost>> getUserPosts(String userId, int page, int size) {
        // 1. 实例化分页对象
        Page<UserPost> postPage = new Page<>(page, size); // 创建分页请求对象
        
        // 2. 构造查询条件
        LambdaQueryWrapper<UserPost> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(UserPost::getUserId, userId) // 匹配作者 ID
                    .eq(UserPost::getStatus, 1) // 状态必须是正常
                    .orderByDesc(UserPost::getCreateTime); // 按创建时间倒序排列
        
        // 3. 执行分页查询
        Page<UserPost> result = this.page(postPage, queryWrapper); // 执行分页查询
        return Result.success(result); // 返回结果
    }

    @Override
    public Result<List<UserPost>> getUserPosts(String userId) {
        LambdaQueryWrapper<UserPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPost::getUserId, userId)
                    .eq(UserPost::getStatus, 1)
                    .orderByDesc(UserPost::getCreateTime);
        return Result.success(this.list(queryWrapper));
    }

    @Override
    @Transactional
    public Result<Void> updatePostStatus(String postId, String userId, Integer status) {
        LambdaUpdateWrapper<UserPost> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserPost::getPostId, postId)
                     .eq(UserPost::getUserId, userId)
                     .set(UserPost::getStatus, status);
        boolean updated = this.update(updateWrapper);
        return updated ? Result.success() : Result.failed("修改状态失败");
    }

    /**
     * 分页获取全站公开动态列表
     *
     * @param page 当前页码
     * @param size 每页记录数
     * @return 包含分页数据的动态结果
     */
    @Override
    public Result<Page<UserPost>> getGlobalPosts(int page, int size) {
        // 1. 实例化分页对象
        Page<UserPost> postPage = new Page<>(page, size); // 创建分页请求
        
        // 2. 构造查询条件
        LambdaQueryWrapper<UserPost> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(UserPost::getStatus, 1) // 状态必须是正常
                    .orderByDesc(UserPost::getCreateTime); // 按时间倒序排列
        
        // 3. 执行分页查询
        Page<UserPost> result = this.page(postPage, queryWrapper); // 执行分页查询
        return Result.success(result); // 返回结果
    }

    /**
     * 更新点赞数
     *
     * @param postId 业务动态 ID
     * @param delta  增量（+1 或 -1）
     * @return 结果
     */
    @Override
    @Transactional // 开启事务
    public Result<Void> updateLikeCount(String postId, Integer delta) {
        // 1. 构造更新操作
        LambdaUpdateWrapper<UserPost> updateWrapper = new LambdaUpdateWrapper<>(); // 构造更新
        updateWrapper.eq(UserPost::getPostId, postId) // 匹配业务 ID
                     .setSql("like_count = like_count + " + delta); // 手写 SQL 增量更新以保证原子性
        
        // 2. 执行更新
        boolean updated = this.update(updateWrapper); // 执行更新操作
        return updated ? Result.success() : Result.failed("更新点赞数失败"); // 返回结果
    }

    /**
     * 更新评论数
     *
     * @param postId 业务动态 ID
     * @param delta  增量（+1 或 -1）
     * @return 结果
     */
    @Override
    @Transactional // 开启事务
    public Result<Void> updateCommentCount(String postId, Integer delta) {
        // 1. 构造更新操作
        LambdaUpdateWrapper<UserPost> updateWrapper = new LambdaUpdateWrapper<>(); // 构造更新
        updateWrapper.eq(UserPost::getPostId, postId) // 匹配业务 ID
                     .setSql("comment_count = comment_count + " + delta); // 手写 SQL 增量更新以保证原子性
        
        // 2. 执行更新
        boolean updated = this.update(updateWrapper); // 执行更新操作
        return updated ? Result.success() : Result.failed("更新评论数失败"); // 返回结果
    }
}
