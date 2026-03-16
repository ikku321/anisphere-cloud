package com.iikun.anivideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anivideo.entity.VideoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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


}
