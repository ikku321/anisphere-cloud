package com.iikun.anivideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anivideo.entity.VideoTagEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 视频标签关系 Mapper
 */
@Mapper
public interface VideoTagMapper extends BaseMapper<VideoTagEntity> {

}
