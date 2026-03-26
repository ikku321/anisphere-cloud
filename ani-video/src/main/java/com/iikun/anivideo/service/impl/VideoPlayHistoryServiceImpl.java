package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iikun.anivideo.entity.VideoPlayHistoryEntity;
import com.iikun.anivideo.mapper.VideoPlayHistoryMapper;
import com.iikun.anivideo.service.VideoPlayHistoryService;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 视频播放历史服务实现
 * <p>
 * 处理用户视频播放进度和历史记录相关业务逻辑
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoPlayHistoryServiceImpl implements VideoPlayHistoryService {

    private final VideoPlayHistoryMapper videoPlayHistoryMapper;

}
