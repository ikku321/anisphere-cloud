package com.iikun.anivideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anivideo.entity.VideoEntity;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 视频数据访问层
 * <p>
 * 处理video表的数据操作
 * </p>
 */
@Mapper
public interface VideoMapper extends BaseMapper<VideoEntity> {

    /**
     * 添加视频信息
     *
     * @param videoEntity 视频信息实体
     * @return 返回添加状态是否成功
     */
    int insert(VideoEntity videoEntity);

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
     * 修改视频标题
     *
     * @param videoTitle 视频标题
     * @param videoId    修改视频标题目标id
     * @return 返回修改成功状态
     */
    @Update("update ani_sphere.video set title = #{videoTitle} where video_id = #{videoId}")
    Integer updateVideoTitle(@Param("videoTitle") String videoTitle, @Param("videoId") String videoId);

    /**
     * 删除视频
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

    /**
     * 根据视频ID查询视频信息
     *
     * @param videoId 视频ID
     * @return 视频信息
     */
    @Select("select * from ani_sphere.video where video_id = #{videoId}")
    VideoEntity selectByVideoId(@Param("videoId") String videoId);

    /**
     * 根据用户ID查询视频列表
     *
     * @param userId 用户ID
     * @return 视频列表
     */
    @Select("select * from ani_sphere.video where user_id = #{userId} order by create_time desc")
    List<VideoEntity> selectByUserId(@Param("userId") String userId);

    /**
     * 更新视频状态
     *
     * @param status  视频状态
     * @param videoId 视频ID
     * @return 更新结果
     */
    @Update("update ani_sphere.video set status = #{status} where video_id = #{videoId}")
    Integer updateVideoStatus(@Param("status") Integer status, @Param("videoId") String videoId);

    /**
     * 更新视频审核状态
     *
     * @param auditStatus 审核状态
     * @param videoId     视频ID
     * @return 更新结果
     */
    @Update("update ani_sphere.video set audit_status = #{auditStatus} where video_id = #{videoId}")
    Integer updateAuditStatus(@Param("auditStatus") Integer auditStatus, @Param("videoId") String videoId);

    /**
     * 更新视频价格
     *
     * @param price   视频价格
     * @param videoId 视频ID
     * @return 更新结果
     */
    @Update("update ani_sphere.video set price = #{price} where video_id = #{videoId}")
    Integer updateVideoPrice(@Param("price") BigDecimal price, @Param("videoId") String videoId);

    /**
     * 批量删除视频
     *
     * @param videoIds 视频ID列表
     * @return 删除数量
     */
    @Delete("<script>" +
            "delete from ani_sphere.video where video_id in " +
            "<foreach collection='videoIds' item='videoId' open='(' separator=',' close=')'>" +
            "#{videoId}" +
            "</foreach>" +
            "</script>")
    Integer batchDeleteVideos(@Param("videoIds") List<String> videoIds);

    @Select("select * from ani_sphere.video where video_id = #{videoId}")
    VideoEntity selectVideoInfoById(@Param("videoId") String videoId);
}














