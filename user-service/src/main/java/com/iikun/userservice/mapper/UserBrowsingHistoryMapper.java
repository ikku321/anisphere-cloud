package com.iikun.userservice.mapper;

import com.iikun.userservice.entity.UserBrowsingHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:10
 * version 1.0.0
 * msg: 用户浏览记录 Mapper
 */
@Mapper
public interface UserBrowsingHistoryMapper {

    /**
     * 记录一次浏览行为。
     *
     * @param userId     user.id 自增主键
     * @param targetType 内容类型
     * @param targetId   目标内容ID
     * @return 影响行数
     */
    int insert(@Param("userId") Long userId,
               @Param("targetType") Integer targetType,
               @Param("targetId") Long targetId);

    /**
     * 删除一条浏览记录（仅允许删除自己的）。
     *
     * @param userId   user.id 自增主键
     * @param recordId 浏览记录自增ID
     * @return 影响行数
     */
    int deleteById(@Param("userId") Long userId, @Param("recordId") Long recordId);

    /**
     * 统计当前用户浏览记录总数。
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 分页查询浏览记录。
     */
    List<UserBrowsingHistory> selectPageByUserId(@Param("userId") Long userId,
                                                 @Param("offset") Integer offset,
                                                 @Param("size") Integer size);
}
