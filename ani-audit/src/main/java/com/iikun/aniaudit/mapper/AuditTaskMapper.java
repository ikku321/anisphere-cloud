package com.iikun.aniaudit.mapper;

import com.iikun.aniaudit.entity.AuditTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Mapper
public interface AuditTaskMapper {

    /**
     * 获取所有待审核视频列表
     *
     * @return 待审核视频列表数据
     */
    @Select("select * from ani_sphere.audit_task where status = 0")
    List<AuditTask> all();

    /**
     * 加入待审核
     * · 审核状态默认为0
     * 0: 待审核
     * 1: 进行中
     * 2: 完成
     *
     * @param videoId 视频id
     */
    @Insert("insert into ani_sphere.audit_task(video_id, status) VALUE(#{videoId}, 0)")
    int add(@Param("videoId") String videoId);


    /**
     * 根据视频id查询是否已经存在待审核任务
     *
     * @param videoId 视频id
     * @return 返回状态
     */
    @Select("select count(1) from audit_task where video_id = #{videoId}")
    int selectByVideoId(@Param("videoId") String videoId);

    @Select("select * from ani_sphere.audit_task where video_id = #{videoId} limit 1")
    AuditTask selectOneByVideoId(@Param("videoId") String videoId);

    @Update("update ani_sphere.audit_task set status = 1, auditor_id = #{auditorId} where video_id = #{videoId} and status = 0")
    int claim(@Param("videoId") String videoId, @Param("auditorId") String auditorId);

    @Update("update ani_sphere.audit_task set status = 2 where video_id = #{videoId} and status = 1 and auditor_id = #{auditorId}")
    int complete(@Param("videoId") String videoId, @Param("auditorId") String auditorId);

    @Update("update ani_sphere.audit_task set status = 2 where video_id = #{videoId} and status in (0, 1)")
    int forceComplete(@Param("videoId") String videoId);

    @Select("<script>" +
            "select count(1) from ani_sphere.audit_task" +
            "<where>" +
            "  <if test='status != null'> and status = #{status} </if>" +
            "  <if test='videoId != null and videoId != \"\"'> and video_id = #{videoId} </if>" +
            "  <if test='auditorId != null and auditorId != \"\"'> and auditor_id = #{auditorId} </if>" +
            "</where>" +
            "</script>")
    long countByFilter(@Param("status") Integer status,
                       @Param("videoId") String videoId,
                       @Param("auditorId") String auditorId);

    @Select("<script>" +
            "select * from ani_sphere.audit_task" +
            "<where>" +
            "  <if test='status != null'> and status = #{status} </if>" +
            "  <if test='videoId != null and videoId != \"\"'> and video_id = #{videoId} </if>" +
            "  <if test='auditorId != null and auditorId != \"\"'> and auditor_id = #{auditorId} </if>" +
            "</where>" +
            " order by id desc" +
            " limit #{size} offset #{offset}" +
            "</script>")
    List<AuditTask> selectPageByFilter(@Param("offset") Integer offset,
                                       @Param("size") Integer size,
                                       @Param("status") Integer status,
                                       @Param("videoId") String videoId,
                                       @Param("auditorId") String auditorId);

    @Select("select count(1) from ani_sphere.audit_task where status = #{status}")
    long countByStatus(@Param("status") Integer status);
}
