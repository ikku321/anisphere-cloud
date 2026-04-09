package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iikun.anivideo.entity.VideoReportEntity;
import com.iikun.anivideo.mapper.VideoReportMapper;
import com.iikun.anivideo.service.VideoReportService;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 视频举报服务实现
 * <p>
 * 处理用户对视频的违规举报相关业务逻辑
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoReportServiceImpl implements VideoReportService {

    private final VideoReportMapper videoReportMapper;

    @Override
    public void submit(String videoId, String userId, String reason) {
        if (videoId == null || videoId.isEmpty()) {
            throw new ServiceException("videoId不能为空");
        }
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("userId不能为空");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new ServiceException("reason不能为空");
        }

        VideoReportEntity entity = new VideoReportEntity();
        entity.setVideoId(videoId);
        entity.setUserId(userId);
        entity.setReason(reason.trim());
        entity.setStatus(0);
        entity.setCreateTime(LocalDateTime.now());

        try {
            if (videoReportMapper.insert(entity) <= 0) {
                throw new ServiceException("提交举报失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public Map<String, Object> page(Integer pageNum, Integer pageSize, Integer status, String videoId, String userId) {
        int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 100);

        try {
            Page<VideoReportEntity> page = new Page<>(safePageNum, safePageSize);
            QueryWrapper<VideoReportEntity> wrapper = new QueryWrapper<>();
            if (status != null) {
                wrapper.eq("status", status);
            }
            if (videoId != null && !videoId.trim().isEmpty()) {
                wrapper.eq("video_id", videoId);
            }
            if (userId != null && !userId.trim().isEmpty()) {
                wrapper.eq("user_id", userId);
            }
            wrapper.orderByDesc("create_time");

            IPage<VideoReportEntity> resultPage = videoReportMapper.selectPage(page, wrapper);

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

    @Override
    public void markHandled(Long reportId) {
        if (reportId == null) {
            throw new ServiceException("reportId不能为空");
        }
        try {
            VideoReportEntity entity = videoReportMapper.selectById(reportId);
            if (entity == null) {
                throw new ServiceException("举报记录不存在");
            }
            if (Objects.equals(entity.getStatus(), 1)) {
                return;
            }
            entity.setStatus(1);
            if (videoReportMapper.updateById(entity) <= 0) {
                throw new ServiceException("更新举报状态失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }
}
