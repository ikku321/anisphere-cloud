package com.iikun.aniaudit.mapper;

import com.iikun.aniaudit.entity.AuditTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    @Select("select * from ani_sphere.audit_task")
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
}
