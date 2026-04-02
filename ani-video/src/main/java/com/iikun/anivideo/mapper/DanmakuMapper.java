package com.iikun.anivideo.mapper;

import com.iikun.anivideo.entity.Danmaku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 弹幕 Mapper 接口
 *
 * 功能说明：
 * 负责弹幕（danmaku）表的数据库操作，包括新增、查询、点赞、状态更新等。
 *
 * 设计说明：
 * - 使用 MyBatis 注解方式实现（部分方法可配合 XML）
 * - 所有方法返回值均为受影响行数或查询结果
 *
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 */
@Mapper
public interface DanmakuMapper {

    /**
     * 新增弹幕
     *
     * @param danmaku 弹幕实体对象（包含视频ID、用户ID、内容、时间戳等）
     * @return 影响行数（成功为1，失败为0）
     */
    int insert(Danmaku danmaku);

    /**
     * 根据视频ID查询弹幕列表
     *
     * 使用场景：
     * - 视频播放时加载弹幕
     *
     * 注意：
     * - 一般需按 position（时间轴）升序排序（建议在SQL中实现）
     * - 可扩展为“分段加载”（提高性能）
     *
     * @param videoId 视频ID
     * @return 弹幕列表（按时间顺序）
     */
    List<Danmaku> selectByVideoId(@Param("videoId") String videoId);

    /**
     * 弹幕点赞
     *
     * 实现逻辑：
     * - likes 字段 +1
     *
     * 注意：
     * - 当前为简单点赞（未限制重复点赞）
     * - 可扩展：用户点赞记录表（防止重复点赞）
     *
     * @param id 弹幕ID
     * @return 影响行数（成功为1，失败为0）
     */
    int likeDanmaku(@Param("id") Long id);

    /**
     * 更新弹幕状态（逻辑删除 / 举报 / 恢复）
     *
     * 状态说明：
     * - 1：正常
     * - 0：已删除
     * - 2：被举报（待审核）
     *
     * 设计要点：
     * - 使用 "status != #{status}" 避免重复更新
     * - 返回值可用于判断是否真正发生变更
     *
     * 使用场景：
     * - 用户删除弹幕
     * - 用户举报弹幕
     * - 管理员审核弹幕
     *
     * @param id 弹幕ID
     * @param status 目标状态值
     * @return 影响行数（0=未更新，1=更新成功）
     */
    @Update("""
            UPDATE danmaku
            SET status = #{status}
            WHERE id = #{id}
              AND status != #{status}
            """)
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
