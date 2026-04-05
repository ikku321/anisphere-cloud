package com.iikun.anisocial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anisocial.entity.FriendGroup;
import com.iikun.anisocial.entity.FriendGroupMapping;
import com.iikun.anisocial.mapper.FriendGroupMapper;
import com.iikun.anisocial.service.FriendGroupMappingService;
import com.iikun.anisocial.service.FriendGroupService;
import com.iikun.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 好友分组业务逻辑实现类
 */
@Slf4j // Lombok 注解：开启日志记录
@Service // Spring 服务类注解
public class FriendGroupServiceImpl extends ServiceImpl<FriendGroupMapper, FriendGroup> implements FriendGroupService {

    @Resource // 注入依赖
    @Lazy // 使用懒加载以避免可能的循环依赖
    private FriendGroupMappingService friendGroupMappingService; // 好友分组映射服务

    /**
     * 创建好友分组
     *
     * @param userId    当前用户 ID
     * @param groupName 分组名称
     * @return 包含新创建的分组信息的结果
     */
    @Override
    public Result<FriendGroup> createGroup(String userId, String groupName) {
        // 1. 检查分组名是否已存在
        LambdaQueryWrapper<FriendGroup> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(FriendGroup::getUserId, userId) // 匹配当前用户
                    .eq(FriendGroup::getName, groupName); // 匹配指定分组名
        FriendGroup existing = this.getOne(queryWrapper); // 执行查询

        if (existing != null) {
            return Result.failed("分组名称已存在"); // 如果存在则返回失败
        }

        // 2. 创建并保存新分组
        FriendGroup group = new FriendGroup(); // 实例化实体类
        group.setUserId(userId); // 设置拥有者 ID
        group.setName(groupName); // 设置分组名
        boolean saved = this.save(group); // 执行保存操作

        return saved ? Result.success(group) : Result.failed("创建分组失败"); // 返回结果
    }

    /**
     * 删除好友分组
     *
     * @param userId  当前用户 ID
     * @param groupId 分组 ID
     * @return 删除结果
     */
    @Override
    @Transactional // 开启事务处理
    public Result<Void> deleteGroup(String userId, Long groupId) {
        // 1. 检查分组是否存在且属于当前用户
        FriendGroup group = this.getById(groupId); // 通过 ID 获取分组
        if (group == null || !group.getUserId().equals(userId)) {
            return Result.failed("分组不存在或无权限删除"); // 权限不足或分组不存在
        }

        // 2. 删除该分组下的所有好友映射记录
        LambdaQueryWrapper<FriendGroupMapping> mappingWrapper = new LambdaQueryWrapper<>(); // 构造映射关系查询
        mappingWrapper.eq(FriendGroupMapping::getGroupId, groupId); // 匹配指定分组 ID
        friendGroupMappingService.remove(mappingWrapper); // 删除所有映射关系

        // 3. 删除分组记录
        boolean removed = this.removeById(groupId); // 删除分组本身
        return removed ? Result.success() : Result.failed("删除分组失败"); // 返回结果
    }

    /**
     * 获取用户的所有分组
     *
     * @param userId 当前用户 ID
     * @return 包含分组列表的结果
     */
    @Override
    public Result<List<FriendGroup>> getUserGroups(String userId) {
        // 1. 查询当前用户拥有的所有分组
        LambdaQueryWrapper<FriendGroup> queryWrapper = new LambdaQueryWrapper<>(); // 构造查询
        queryWrapper.eq(FriendGroup::getUserId, userId); // 匹配拥有者 ID
        List<FriendGroup> groups = this.list(queryWrapper); // 执行列表查询
        return Result.success(groups); // 返回查询结果
    }

    /**
     * 修改分组名称
     *
     * @param userId  当前用户 ID
     * @param groupId 分组 ID
     * @param name    新名称
     * @return 修改结果
     */
    @Override
    public Result<Void> renameGroup(String userId, Long groupId, String name) {
        // 1. 检查分组是否存在且属于当前用户
        FriendGroup group = this.getById(groupId); // 通过 ID 获取分组
        if (group == null || !group.getUserId().equals(userId)) {
            return Result.failed("分组不存在或无权限修改"); // 权限不足或不存在
        }

        // 2. 更新分组名称
        group.setName(name); // 设置新名称
        boolean updated = this.updateById(group); // 执行更新操作
        return updated ? Result.success() : Result.failed("修改分组名失败"); // 返回结果
    }
}
