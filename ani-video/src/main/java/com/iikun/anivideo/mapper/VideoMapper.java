package com.iikun.anivideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anivideo.entity.VideoEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:36
 * version 1.0.0
 * msg:
 */
@Mapper
public interface VideoMapper {

    /**
     * 添加视频信息
     *
     * @param videoEntity 视频信息实体
     * @return 返回添加状态是否成功
     */
    Integer insert(VideoEntity videoEntity);


    /**
     * 修改视频可见状态
     *
     * @param visible 数值（0：可见，1：隐藏）
     * @param videoId 修改视频目标id
     * @return 返回成功状态
     */
    @Update("update ani_sphere.video set visible = #{visible} where video_id = #{videoId}")
    Integer updateVideoVisible(@Param("visible") Integer visible, @Param("videoId") String videoId);

    /**
     * 根据视频id查找视频是否存在
     *
     * @param videoId 视频id
     * @return 返回数据
     */
    @Select("select count(1) from ani_sphere.video where video_id = #{videoId}")
    Integer foundByVideoId(@Param("videoId") String videoId);


    /**
     * 修改视频简介
     *
     * @param description 视频简介内容
     * @param videoId     视频id
     * @return 返回修改结果
     */
    @Update("update ani_sphere.video set description = #{description} where video_id = #{videoId}")
    Integer updateByVideoDescription(@Param("description") String description, @Param("videoId") String videoId);

    /**
     * <p>修改视频标题</p>
     *
     * @param videoTitle 视频标题
     * @param videoId    修改视频标题目标id
     * @return 返回修改成功状态
     */
    @Update("update ani_sphere.video set title = #{videoTitle} where video_id = #{videoId}")
    Integer updateVideoTitle(@Param("videoTitle") String videoTitle, @Param("videoId") String videoId);

    /**
     * <p>删除视频</p>
     *
     * @param videoId 删除视频目标id
     * @return 返回删除成功状态
     */
    @Delete("delete from ani_sphere.video where video_id = #{videoId}")
    Integer delete(@Param("videoId") String videoId);

    /**
     * 查询所有视频信息
     *
     * @return 返回所有视频列表
     */
    @Select("select * from ani_sphere.video")
    List<VideoEntity> selectByVideoAll();

    /**
     * 根据标题查询视频信息
     *
     * @param title 视频标题
     * @return 返回视频信息
     */
    @Select("select * from ani_sphere.video where title like concat('%', #{title}, '%')")
    List<VideoEntity> selectByVideoTitle(@Param("title") String title);
}














