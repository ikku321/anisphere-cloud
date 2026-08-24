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

    @Override
    public void record(String userId, String videoId, Integer lastPosition) {
        if (userId == null || userId.isBlank()) {
            throw new ServiceException("userId不能为空");
        }
        if (videoId == null || videoId.isBlank()) {
            throw new ServiceException("videoId不能为空");
        }
        int safePosition = Math.max(lastPosition == null ? 0 : lastPosition, 0);

        try {
            VideoPlayHistoryEntity existed = findOne(userId, videoId);
            if (existed == null) {
                VideoPlayHistoryEntity entity = new VideoPlayHistoryEntity();
                entity.setUserId(userId);
                entity.setVideoId(videoId);
                entity.setLastPosition(safePosition);
                entity.setUpdateTime(LocalDateTime.now());
                if (videoPlayHistoryMapper.insert(entity) <= 0) {
                    throw new ServiceException("记录播放历史失败");
                }
                return;
            }

            existed.setLastPosition(safePosition);
            existed.setUpdateTime(LocalDateTime.now());
            if (videoPlayHistoryMapper.updateById(existed) <= 0) {
                throw new ServiceException("更新播放历史失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public VideoPlayHistoryEntity detail(String userId, String videoId) {
        if (userId == null || userId.isBlank()) {
            throw new ServiceException("userId不能为空");
        }
        if (videoId == null || videoId.isBlank()) {
            throw new ServiceException("videoId不能为空");
        }
        try {
            return findOne(userId, videoId);
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public Map<String, Object> pageByUser(String userId, Integer pageNum, Integer pageSize) {
        if (userId == null || userId.isBlank()) {
            throw new ServiceException("userId不能为空");
        }
        return page(pageNum, pageSize, userId, null);
    }

    @Override
    public Map<String, Object> adminPage(Integer pageNum, Integer pageSize, String userId, String videoId) {
        return page(pageNum, pageSize, userId, videoId);
    }

    @Override
    public void deleteOne(String userId, String videoId) {
        if (userId == null || userId.isBlank()) {
            throw new ServiceException("userId不能为空");
        }
        if (videoId == null || videoId.isBlank()) {
            throw new ServiceException("videoId不能为空");
        }
        try {
            QueryWrapper<VideoPlayHistoryEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId).eq("video_id", videoId);
            if (videoPlayHistoryMapper.delete(wrapper) <= 0) {
                throw new ServiceException("播放历史不存在或已删除");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void clearByUser(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ServiceException("userId不能为空");
        }
        try {
            QueryWrapper<VideoPlayHistoryEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            videoPlayHistoryMapper.delete(wrapper);
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    private Map<String, Object> page(Integer pageNum, Integer pageSize, String userId, String videoId) {
        int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 100);

        try {
            Page<VideoPlayHistoryEntity> page = new Page<>(safePageNum, safePageSize);
            QueryWrapper<VideoPlayHistoryEntity> wrapper = new QueryWrapper<>();
            if (userId != null && !userId.isBlank()) {
                wrapper.eq("user_id", userId);
            }
            if (videoId != null && !videoId.isBlank()) {
                wrapper.eq("video_id", videoId);
            }
            wrapper.orderByDesc("update_time");

            IPage<VideoPlayHistoryEntity> resultPage = videoPlayHistoryMapper.selectPage(page, wrapper);
            Map<String, Object> result = new HashMap<>();
            result.put("records", resultPage.getRecords());
            result.put("total", resultPage.getTotal());
            result.put("current", resultPage.getCurrent());
            result.put("pages", resultPage.getPages());
            result.put("size", resultPage.getSize());
            return result;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    private VideoPlayHistoryEntity findOne(String userId, String videoId) {
        QueryWrapper<VideoPlayHistoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("video_id", videoId)
                .last("limit 1");
        return videoPlayHistoryMapper.selectOne(wrapper);
    }
}
