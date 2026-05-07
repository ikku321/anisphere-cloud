package com.iikun.anisocial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anisocial.dto.NotificationRequestDTO;
import com.iikun.anisocial.dto.UserDTO;
import com.iikun.anisocial.entity.Follow;
import com.iikun.anisocial.feign.client.NotificationFeignClient;
import com.iikun.anisocial.feign.client.UserFeignClient;
import com.iikun.anisocial.mapper.FollowMapper;
import com.iikun.anisocial.service.FollowService;
import com.iikun.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 关注/粉丝业务逻辑实现类
 */
@Slf4j // Lombok 注解：开启日志
@Service // Spring 服务类注解
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Resource // 注入依赖
    private UserFeignClient userFeignClient; // 用于校验目标用户是否存在

    @Resource
    private NotificationFeignClient notificationFeignClient; // 发关注通知用

    /**
     * 关注用户
     *
     * @param userId     当前用户 ID
     * @param targetUser 被关注的目标用户 ID
     * @return 结果
     */
    @Override
    @Transactional // 开启事务
    public Result<Void> follow(String userId, String targetUser) {
        // 1. 校验：不能关注自己
        if (userId.equals(targetUser)) {
            return Result.failed("不能关注自己"); // 返回错误
        }

        // 2. 调用用户服务校验目标用户是否存在
        Result<UserDTO> userResult = userFeignClient.find(targetUser); // 调用 Feign 客户端
        if (userResult == null || userResult.getData() == null) {
            return Result.failed("目标用户不存在"); // 目标用户不存在
        }

        // 3. 检查是否已经关注过
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(Follow::getUserId, userId) // 当前用户 ID
                    .eq(Follow::getTargetUser, targetUser); // 目标用户 ID
        Follow existing = this.getOne(queryWrapper); // 执行查询

        if (existing != null) {
            return Result.failed("你已经关注过该用户了"); // 如果已存在则返回失败
        }

        // 4. 创建关注记录并保存
        Follow follow = new Follow(); // 实例化实体类
        follow.setUserId(userId); // 设置关注者 ID
        follow.setTargetUser(targetUser); // 设置被关注者 ID
        boolean saved = this.save(follow); // 执行保存操作

        // 关注成功后给被关注者发一条「新粉丝」通知 (category=system).
        // 使用 try-catch 隔离 — 通知服务崩、网络抖动都不应该影响关注本身的成功返回.
        if (saved) {
            sendFollowNotification(userId, targetUser);
        }

        return saved ? Result.success() : Result.failed("关注失败"); // 返回操作结果
    }

    /**
     * 发「新粉丝」通知: 告诉被关注者有人关注了他.
     * content 里带上关注者 userId, 前端点击详情时可以跳到关注者资料页.
     */
    private void sendFollowNotification(String followerId, String targetUser) {
        try {
            NotificationRequestDTO dto = new NotificationRequestDTO();
            dto.setTargetUser(targetUser);
            dto.setCategory("system");
            dto.setTitle("你多了一个新粉丝");
            dto.setContent("用户 " + followerId + " 关注了你");
            notificationFeignClient.sendNotification(dto);
        } catch (Exception e) {
            log.warn("[FollowServiceImpl] 发送关注通知失败, follower={}, target={}, err={}",
                    followerId, targetUser, e.getMessage());
        }
    }

    /**
     * 取消关注
     *
     * @param userId     当前用户 ID
     * @param targetUser 被取消关注的目标用户 ID
     * @return 结果
     */
    @Override
    @Transactional // 开启事务
    public Result<Void> unfollow(String userId, String targetUser) {
        // 1. 构造删除条件
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>(); // 构造删除查询
        queryWrapper.eq(Follow::getUserId, userId) // 匹配关注者 ID
                    .eq(Follow::getTargetUser, targetUser); // 匹配被关注者 ID
        
        // 2. 执行删除操作
        boolean removed = this.remove(queryWrapper); // 执行删除
        return removed ? Result.success() : Result.failed("取消关注失败，可能原本就未关注"); // 返回结果
    }

    /**
     * 获取关注列表（我关注了谁）
     *
     * @param userId 指定用户的 ID
     * @return 关注列表
     */
    @Override
    public Result<List<Follow>> getFollowingList(String userId) {
        // 1. 查询当前用户发起的关注记录
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(Follow::getUserId, userId); // 匹配关注者 ID
        List<Follow> list = this.list(queryWrapper); // 执行查询
        return Result.success(list); // 返回结果
    }

    /**
     * 获取粉丝列表（谁关注了我）
     *
     * @param userId 指定用户的 ID
     * @return 粉丝列表
     */
    @Override
    public Result<List<Follow>> getFollowersList(String userId) {
        // 1. 查询被关注目标为当前用户的记录
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(Follow::getTargetUser, userId); // 匹配被关注者 ID
        List<Follow> list = this.list(queryWrapper); // 执行查询
        return Result.success(list); // 返回结果
    }
}
