package com.iikun.userservice.mapper;

import com.iikun.userservice.domain.model.AdminUserListItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author iikun
 * time 2025/9/21 0:29
 * version 1.0.0
 * msg: 用户管理 Mapper
 */
@Mapper
public interface AdminMapper {

    /**
     * 统计用户总数。
     */
    long countUsers();

    /**
     * 分页查询用户列表（不返回 password）。
     *
     * @param offset 偏移量
     * @param size   条数
     */
    List<AdminUserListItem> selectUserPage(@Param("offset") Integer offset, @Param("size") Integer size);
}
