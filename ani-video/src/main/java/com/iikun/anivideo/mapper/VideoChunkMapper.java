package com.iikun.anivideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anivideo.entity.VideoChunkEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 视频分片上传数据访问层
 * <p>
 * 处理video_chunk表的数据操作
 * </p>
 */
@Mapper
public interface VideoChunkMapper extends BaseMapper<VideoChunkEntity> {
}
