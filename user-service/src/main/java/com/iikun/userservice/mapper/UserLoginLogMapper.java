package com.iikun.userservice.mapper;

import com.iikun.userservice.entity.UserLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:19
 * version 1.0.0
 * msg: 用户登录日志Mapper
 */
@Mapper
public interface UserLoginLogMapper {

    /**
     * 写入一条登录日志。
     *
     * @param userId  user.id 自增主键
     * @param loginIp 登录IP
     * @param device  设备信息
     * @param status  状态：0失败 1成功
     * @return 影响行数
     */
    int insert(@Param("userId") Long userId,
               @Param("loginIp") String loginIp,
               @Param("device") String device,
               @Param("status") Integer status);

    /**
     * 统计指定用户的登录日志数量。
     *
     * @param userId user.id 自增主键
     * @return 总数
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 分页查询指定用户的登录日志。
     *
     * @param userId user.id 自增主键
     * @param offset 偏移量
     * @param size   条数
     * @return 登录日志列表
     */
    List<UserLoginLog> selectPageByUserId(@Param("userId") Long userId,
                                          @Param("offset") Integer offset,
                                          @Param("size") Integer size);
}
