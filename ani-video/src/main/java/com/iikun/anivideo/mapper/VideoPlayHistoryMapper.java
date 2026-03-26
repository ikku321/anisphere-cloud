package com.iikun.anivideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anivideo.entity.VideoPlayHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 视频播放历史数据访问层
 * <p>
 * 处理video_play_history表的数据操作
 * </p>
 */
@Mapper
public interface VideoPlayHistoryMapper extends BaseMapper<VideoPlayHistoryEntity> {
}
