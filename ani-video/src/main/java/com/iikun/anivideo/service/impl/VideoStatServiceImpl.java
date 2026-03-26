package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iikun.anivideo.entity.VideoStatEntity;
import com.iikun.anivideo.mapper.VideoStatMapper;
import com.iikun.anivideo.service.VideoStatService;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 视频统计服务实现
 * <p>
 * 处理视频播放量、点赞数、分享数、评论数等统计业务逻辑
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoStatServiceImpl implements VideoStatService {

    private final VideoStatMapper videoStatMapper;

}
