package com.iikun.anivideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anivideo.entity.VideoTagEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * 视频标签关系 Mapper
 */
@Mapper
public interface VideoTagMapper {


    /**
     * 新增视频标签关联
     *
     * @param videoId 视频id
     * @param tagId   标签id
     * @return 返回新增成功状态
     */
    @Insert("insert ani_sphere.video_tag(video_id, tag_id) VALUE(#{videoId}, #{tagId})")
    Integer add(@Param("videoId") String videoId, @Param("tagId") Integer tagId);

    /**
     * 删除视频标签关联
     *
     * @param videoTagId 视频标签关联id
     * @return 返回删除状态
     */
    @Delete("delete from ani_sphere.video_tag where id = #{videoTagId}")
    boolean delete(@Param("videoTagId") Integer videoTagId);

    /**
     * 查询所有视频标签关联
     *
     * @return 返回视频标签关联表列表
     */
    @Select("select * from ani_sphere.video_tag")
    List<VideoTagEntity> findAll();

    /**
     * 根据视频id查询视频所关联的标签
     *
     * @param videoId 视频id
     * @return 返回列表内容
     */
    @Select("select * from ani_sphere.video_tag where video_id = #{videoId}")
    List<VideoTagEntity> findByVideoId(@Param("videoId") String videoId);

    /**
     * 根据id查询视频标签关联信息
     *
     * @param id 视频标签关联id
     * @return 返回信息
     */
    @Select("select * from ani_sphere.video_tag where id = #{id}")
    VideoTagEntity findById(@Param("id") String id);

    /**
     * 根据标签id查询所有视频标签关联列表
     *
     * @param tagId 标签id
     * @return 返回列表信息
     */
    @Select("select * from ani_sphere.video_tag where tag_id = #{tagId}")
    List<VideoTagEntity> findByTagId(@Param("tagId") Integer tagId);
}
