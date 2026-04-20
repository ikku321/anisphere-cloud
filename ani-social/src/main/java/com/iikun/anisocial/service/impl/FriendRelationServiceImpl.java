package com.iikun.anisocial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anisocial.dto.UserDTO;
import com.iikun.anisocial.entity.FriendRelation;
import com.iikun.anisocial.feign.client.UserFeignClient;
import com.iikun.anisocial.mapper.FriendRelationMapper;
import com.iikun.anisocial.service.FriendRelationService;
import com.iikun.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 好友关系业务逻辑实现类
 */
@Slf4j // Lombok 注解：日志记录
@Service // Spring 服务类注解
public class FriendRelationServiceImpl extends ServiceImpl<FriendRelationMapper, FriendRelation> implements FriendRelationService {

    @Resource // 注入依赖
    private UserFeignClient userFeignClient; // 用于调用用户服务

    /**
     * 发送好友申请
     *
     * @param userId   当前用户 ID
     * @param friendId 目标好友 ID
     * @return 发送结果
     */
    @Override
    @Transactional // 开启事务处理
    public Result<Void> sendFriendRequest(String userId, String friendId) {
        // 1. 基本校验：不能加自己为好友
        if (userId.equals(friendId)) {
            return Result.failed("不能添加自己为好友"); // 返回失败结果
        }

        // 2. 调用用户服务校验目标用户是否存在
        Result<UserDTO> userResult = userFeignClient.find(friendId); // 调用 Feign 客户端
        if (userResult == null || userResult.getData() == null) {
            return Result.failed("目标用户不存在"); // 目标用户不存在则返回失败
        }

        // 3. 检查是否已经是好友或已有待处理申请
        LambdaQueryWrapper<FriendRelation> queryWrapper = new LambdaQueryWrapper<>(); // MyBatis-Plus 查询条件构造器
        queryWrapper.eq(FriendRelation::getUserId, userId) // 查询条件：拥有者 ID
                    .eq(FriendRelation::getFriendId, friendId); // 查询条件：好友 ID
        FriendRelation existing = this.getOne(queryWrapper); // 执行查询

        if (existing != null) {
            // 如果已存在关系，根据状态返回不同提示
            if (existing.getStatus() == 0) {
                return Result.failed("已发送过好友申请，请耐心等待对方确认"); // 申请中
            } else if (existing.getStatus() == 1) {
                return Result.failed("你们已经是好友了"); // 已是好友
            }
        }

        // 4. 创建新的好友申请记录
        FriendRelation relation = new FriendRelation(); // 实例化实体类
        relation.setUserId(userId); // 设置发起者 ID
        relation.setFriendId(friendId); // 设置目标好友 ID
        relation.setStatus(0); // 状态设为 0（申请中）
        
        boolean saved = this.save(relation); // 执行插入操作
        return saved ? Result.success() : Result.failed("发送申请失败"); // 返回结果
    }

    /**
     * 处理好友申请（接受、拒绝、拉黑）
     *
     * @param userId   当前用户 ID（即被申请人）
     * @param friendId 申请人 ID
     * @param status   目标状态：1=接受，2=拒绝/拉黑
     * @return 处理结果
     */
    @Override
    @Transactional // 开启事务处理
    public Result<Void> processFriendRequest(String userId, String friendId, Integer status) {
        // 1. 查询待处理的好友申请
        LambdaQueryWrapper<FriendRelation> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询条件
        queryWrapper.eq(FriendRelation::getUserId, userId) // 发起人是 friendId
                    .eq(FriendRelation::getFriendId, friendId) // 目标是当前用户 userId
                    .eq(FriendRelation::getStatus, 0); // 状态必须是申请中
        FriendRelation relation = this.getOne(queryWrapper); // 执行查询

        if (relation == null) {
            return Result.failed("未找到该好友申请记录"); // 记录不存在返回错误
        }

        // 2. 更新申请记录状态
        relation.setStatus(status); // 设置新状态
        this.updateById(relation); // 执行更新操作

        // 3. 如果接受申请，则需要双向建立好友关系
        if (status == 1) {
            // 检查反向关系是否已存在
            LambdaQueryWrapper<FriendRelation> reverseWrapper = new LambdaQueryWrapper<>(); // 构造反向查询
            reverseWrapper.eq(FriendRelation::getUserId, userId) // 拥有者是当前用户
                          .eq(FriendRelation::getFriendId, friendId); // 好友是发起人
            FriendRelation reverseRelation = this.getOne(reverseWrapper); // 执行查询

            if (reverseRelation == null) {
                // 如果反向记录不存在，则创建一条新的已通过关系
                FriendRelation newReverse = new FriendRelation(); // 实例化反向记录
                newReverse.setUserId(userId); // 设置当前用户为拥有者
                newReverse.setFriendId(friendId); // 设置原发起人为好友
                newReverse.setStatus(1); // 状态直接设为已通过
                this.save(newReverse); // 执行插入
            } else {
                // 如果已存在反向记录，则更新其状态为已通过
                reverseRelation.setStatus(1); // 更新状态
                this.updateById(reverseRelation); // 执行更新
            }
        }

        return Result.success(); // 返回成功
    }

    /**
     * 获取当前用户的好友列表
     *
     * @param userId 当前用户 ID
     * @return 包含好友关系列表的结果
     */
    @Override
    public Result<List<FriendRelation>> getFriendList(String userId) {
        // 1. 查询所有状态为已通过（1）的好友关系
        LambdaQueryWrapper<FriendRelation> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(FriendRelation::getUserId, userId) // 拥有者为当前用户
                    .eq(FriendRelation::getStatus, 1); // 状态为已通过
        List<FriendRelation> list = this.list(queryWrapper); // 执行列表查询
        return Result.success(list); // 返回查询结果
    }

    /**
     * 修改好友备注
     *
     * @param userId   当前用户 ID
     * @param friendId 好友 ID
     * @param remark   备注内容
     * @return 修改结果
     */
    @Override
    public Result<Void> updateRemark(String userId, String friendId, String remark) {
        // 1. 查询对应的好友关系
        LambdaQueryWrapper<FriendRelation> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(FriendRelation::getUserId, userId) // 匹配当前用户
                    .eq(FriendRelation::getFriendId, friendId); // 匹配指定好友
        FriendRelation relation = this.getOne(queryWrapper); // 执行查询

        if (relation == null) {
            return Result.failed("未找到该好友关系"); // 关系不存在
        }

        // 2. 更新备注字段
        relation.setRemark(remark); // 设置新备注
        boolean updated = this.updateById(relation); // 执行更新
        return updated ? Result.success() : Result.failed("修改备注失败"); // 返回结果
    }

    /**
     * 删除好友（双向解除）
     *
     * @param userId   当前用户 ID
     * @param friendId 好友 ID
     * @return 删除结果
     */
    @Override
    @Transactional // 开启事务处理
    public Result<Void> deleteFriend(String userId, String friendId) {
        // 1. 构造删除条件：同时匹配正向和反向关系
        LambdaQueryWrapper<FriendRelation> queryWrapper = new LambdaQueryWrapper<>(); // 构造正向关系查询
        queryWrapper.eq(FriendRelation::getUserId, userId) // 用户 ID
                    .eq(FriendRelation::getFriendId, friendId); // 好友 ID
        this.remove(queryWrapper); // 执行正向关系删除

        LambdaQueryWrapper<FriendRelation> reverseWrapper = new LambdaQueryWrapper<>(); // 构造反向关系查询
        reverseWrapper.eq(FriendRelation::getUserId, friendId) // 好友 ID
                      .eq(FriendRelation::getFriendId, userId); // 用户 ID
        this.remove(reverseWrapper); // 执行反向关系删除

        return Result.success(); // 返回成功
    }

    @Override
    public Result<List<String>> getMutualFriends(String userId, String otherUserId) {
        // 1. 获取当前用户的好友 ID 集合
        LambdaQueryWrapper<FriendRelation> myFriendsWrapper = new LambdaQueryWrapper<>();
        myFriendsWrapper.eq(FriendRelation::getUserId, userId)
                        .eq(FriendRelation::getStatus, 1)
                        .select(FriendRelation::getFriendId);
        List<String> myFriendIds = this.listObjs(myFriendsWrapper, Object::toString);

        if (myFriendIds.isEmpty()) {
            return Result.success(List.of());
        }

        // 2. 获取另一个用户的好友 ID 集合
        LambdaQueryWrapper<FriendRelation> otherFriendsWrapper = new LambdaQueryWrapper<>();
        otherFriendsWrapper.eq(FriendRelation::getUserId, otherUserId)
                           .eq(FriendRelation::getStatus, 1)
                           .select(FriendRelation::getFriendId);
        List<String> otherFriendIds = this.listObjs(otherFriendsWrapper, Object::toString);

        if (otherFriendIds.isEmpty()) {
            return Result.success(List.of());
        }

        // 3. 计算交集
        myFriendIds.retainAll(otherFriendIds);
        return Result.success(myFriendIds);
    }

    @Override
    @Transactional
    public Result<Void> blockUser(String userId, String targetUser) {
        if (userId.equals(targetUser)) {
            return Result.failed("不能拉黑自己");
        }
        
        // 查找或创建关系
        LambdaQueryWrapper<FriendRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendRelation::getUserId, userId).eq(FriendRelation::getFriendId, targetUser);
        FriendRelation relation = this.getOne(wrapper);
        
        if (relation == null) {
            relation = new FriendRelation();
            relation.setUserId(userId);
            relation.setFriendId(targetUser);
            relation.setStatus(2); // 拉黑
            this.save(relation);
        } else {
            relation.setStatus(2);
            this.updateById(relation);
        }
        
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Void> unblockUser(String userId, String targetUser) {
        LambdaQueryWrapper<FriendRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendRelation::getUserId, userId)
               .eq(FriendRelation::getFriendId, targetUser)
               .eq(FriendRelation::getStatus, 2);
        
        FriendRelation relation = this.getOne(wrapper);
        if (relation == null) {
            return Result.failed("该用户不在黑名单中");
        }
        
        // 取消拉黑：这里逻辑上可以选择删除记录，或者设为 0 (待验证) 或 1 (如果是原好友)
        // 简单起见，取消拉黑即删除该“拉黑”状态的关系记录
        this.removeById(relation.getId());
        
        return Result.success();
    }
}
