package com.iikun.anisocial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anisocial.entity.FriendGroup;
import com.iikun.anisocial.entity.FriendGroupMapping;
import com.iikun.anisocial.entity.FriendRelation;
import com.iikun.anisocial.mapper.FriendGroupMappingMapper;
import com.iikun.anisocial.service.FriendGroupMappingService;
import com.iikun.anisocial.service.FriendGroupService;
import com.iikun.anisocial.service.FriendRelationService;
import com.iikun.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 好友-分组映射业务逻辑实现类
 */
@Slf4j // Lombok 注解：开启日志
@Service // Spring 服务类注解
public class FriendGroupMappingServiceImpl extends ServiceImpl<FriendGroupMappingMapper, FriendGroupMapping> implements FriendGroupMappingService {

    @Resource // 注入依赖
    @Lazy // 懒加载以避免循环依赖
    private FriendGroupService friendGroupService; // 好友分组服务

    @Resource // 注入依赖
    @Lazy // 懒加载
    private FriendRelationService friendRelationService; // 好友关系服务

    /**
     * 将好友添加到分组
     *
     * @param userId   当前用户 ID
     * @param groupId  分组 ID
     * @param friendId 好友 ID
     * @return 结果
     */
    @Override
    public Result<Void> addFriendToGroup(String userId, Long groupId, String friendId) {
        // 1. 校验分组归属权
        FriendGroup group = friendGroupService.getById(groupId); // 获取分组信息
        if (group == null || !group.getUserId().equals(userId)) {
            return Result.failed("分组不存在或无操作权限"); // 分组不属于该用户
        }

        // 2. 校验好友关系是否存在
        LambdaQueryWrapper<FriendRelation> relationWrapper = new LambdaQueryWrapper<>(); // 构造好友关系查询
        relationWrapper.eq(FriendRelation::getUserId, userId) // 当前用户
                       .eq(FriendRelation::getFriendId, friendId) // 指定好友
                       .eq(FriendRelation::getStatus, 1); // 必须是好友状态
        FriendRelation relation = friendRelationService.getOne(relationWrapper); // 执行查询

        if (relation == null) {
            return Result.failed("你们还不是好友，无法添加进分组"); // 关系不满足
        }

        // 3. 校验映射记录是否已存在
        LambdaQueryWrapper<FriendGroupMapping> mappingWrapper = new LambdaQueryWrapper<>(); // 构造映射查询
        mappingWrapper.eq(FriendGroupMapping::getGroupId, groupId) // 指定分组
                      .eq(FriendGroupMapping::getFriendRelationId, relation.getId()); // 指定好友关系 ID
        FriendGroupMapping existing = this.getOne(mappingWrapper); // 执行查询

        if (existing != null) {
            return Result.failed("该好友已在当前分组中"); // 如果已存在则返回提示
        }

        // 4. 创建映射关系并保存
        FriendGroupMapping mapping = new FriendGroupMapping(); // 实例化实体类
        mapping.setGroupId(groupId); // 设置分组 ID
        mapping.setFriendRelationId(relation.getId()); // 设置好友关系 ID
        boolean saved = this.save(mapping); // 执行保存操作

        return saved ? Result.success() : Result.failed("添加好友到分组失败"); // 返回结果
    }

    /**
     * 将好友从分组中移除
     *
     * @param userId   当前用户 ID
     * @param groupId  分组 ID
     * @param friendId 好友 ID
     * @return 结果
     */
    @Override
    public Result<Void> removeFriendFromGroup(String userId, Long groupId, String friendId) {
        // 1. 校验分组归属权
        FriendGroup group = friendGroupService.getById(groupId); // 获取分组
        if (group == null || !group.getUserId().equals(userId)) {
            return Result.failed("分组不存在或无操作权限"); // 权限不足
        }

        // 2. 查找好友关系 ID
        LambdaQueryWrapper<FriendRelation> relationWrapper = new LambdaQueryWrapper<>(); // 构造好友关系查询
        relationWrapper.eq(FriendRelation::getUserId, userId) // 当前用户
                       .eq(FriendRelation::getFriendId, friendId); // 指定好友
        FriendRelation relation = friendRelationService.getOne(relationWrapper); // 执行查询

        if (relation == null) {
            return Result.failed("未找到该好友关系"); // 关系不存在
        }

        // 3. 构造删除条件并执行删除
        LambdaQueryWrapper<FriendGroupMapping> mappingWrapper = new LambdaQueryWrapper<>(); // 构造映射删除查询
        mappingWrapper.eq(FriendGroupMapping::getGroupId, groupId) // 指定分组 ID
                      .eq(FriendGroupMapping::getFriendRelationId, relation.getId()); // 指定关系 ID
        boolean removed = this.remove(mappingWrapper); // 执行删除操作

        return removed ? Result.success() : Result.failed("从分组中移除好友失败"); // 返回结果
    }

    /**
     * 获取指定分组下的所有好友映射记录
     *
     * @param groupId 分组 ID
     * @return 映射记录列表
     */
    @Override
    public Result<List<FriendGroupMapping>> getGroupFriends(Long groupId) {
        // 1. 根据分组 ID 查询所有映射记录
        LambdaQueryWrapper<FriendGroupMapping> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(FriendGroupMapping::getGroupId, groupId); // 匹配指定分组
        List<FriendGroupMapping> list = this.list(queryWrapper); // 执行列表查询
        return Result.success(list); // 返回结果
    }
}
