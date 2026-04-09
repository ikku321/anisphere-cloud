package com.iikun.aniaudit.mapper;

import com.iikun.aniaudit.entity.AuditRecordEntity;
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
public interface AuditRecordMapper {

    @Insert("insert into ani_sphere.audit_record(video_id, auditor_id, result, comment) values(#{videoId}, #{auditorId}, #{result}, #{comment})")
    int add(@Param("videoId") String videoId,
            @Param("auditorId") String auditorId,
            @Param("result") Integer result,
            @Param("comment") String comment);

    @Select("select * from ani_sphere.audit_record where video_id = #{videoId} order by create_time desc")
    List<AuditRecordEntity> listByVideoId(@Param("videoId") String videoId);

    @Select("<script>" +
            "select count(1) from ani_sphere.audit_record" +
            "<where>" +
            "  <if test='videoId != null and videoId != \"\"'> and video_id = #{videoId} </if>" +
            "  <if test='auditorId != null and auditorId != \"\"'> and auditor_id = #{auditorId} </if>" +
            "  <if test='result != null'> and result = #{result} </if>" +
            "</where>" +
            "</script>")
    long countByFilter(@Param("videoId") String videoId,
                       @Param("auditorId") String auditorId,
                       @Param("result") Integer result);

    @Select("<script>" +
            "select * from ani_sphere.audit_record" +
            "<where>" +
            "  <if test='videoId != null and videoId != \"\"'> and video_id = #{videoId} </if>" +
            "  <if test='auditorId != null and auditorId != \"\"'> and auditor_id = #{auditorId} </if>" +
            "  <if test='result != null'> and result = #{result} </if>" +
            "</where>" +
            " order by create_time desc" +
            " limit #{size} offset #{offset}" +
            "</script>")
    List<AuditRecordEntity> selectPageByFilter(@Param("offset") Integer offset,
                                               @Param("size") Integer size,
                                               @Param("videoId") String videoId,
                                               @Param("auditorId") String auditorId,
                                               @Param("result") Integer result);
}
