package com.iikun.userservice.mapper;

import com.iikun.userservice.domain.model.AdminUserListItem;
import com.iikun.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    long countUsersByFilter(@Param("keyword") String keyword, @Param("status") Integer status, @Param("role") Integer role);

    List<AdminUserListItem> selectUserPageByFilter(
            @Param("offset") Integer offset,
            @Param("size") Integer size,
            @Param("keyword") String keyword,
            @Param("status") Integer status,
            @Param("role") Integer role
    );

    int updateUserStatus(@Param("userId") String userId, @Param("status") Integer status);

    int updateUserRole(@Param("userId") String userId, @Param("role") Integer role);

    int updateUserPassword(@Param("userId") String userId, @Param("password") String password);

    int updateUser(User user);

    /**
     *
     * 登录-管理员端认证
     * role: 权限(0: 管理员，1: 普通用户...)
     *
     * @param username 账号
     * @return 返回状态
     */
    @Select("select count(1) from ani_sphere.user where username = #{username} and role = 0")
    int isAdmin(@Param("username") String username);
}
