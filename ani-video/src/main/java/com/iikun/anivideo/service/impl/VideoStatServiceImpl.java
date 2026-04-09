package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

    @Override
    public VideoStatEntity getOrInit(String videoId) {
        if (videoId == null || videoId.isEmpty()) {
            throw new ServiceException("videoId不能为空");
        }
        try {
            VideoStatEntity entity = videoStatMapper.selectById(videoId);
            if (entity != null) {
                return entity;
            }
            VideoStatEntity init = new VideoStatEntity();
            init.setVideoId(videoId);
            init.setPlayCount(0L);
            init.setLikeCount(0L);
            init.setShareCount(0L);
            init.setCommentCount(0L);
            init.setUpdateTime(LocalDateTime.now());
            videoStatMapper.insert(init);
            return init;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void incrPlay(String videoId, Long delta) {
        incr(videoId, "play_count", delta);
    }

    @Override
    public void incrLike(String videoId, Long delta) {
        incr(videoId, "like_count", delta);
    }

    @Override
    public void incrShare(String videoId, Long delta) {
        incr(videoId, "share_count", delta);
    }

    @Override
    public void incrComment(String videoId, Long delta) {
        incr(videoId, "comment_count", delta);
    }

    @Override
    public Map<String, Object> topPlay(Integer limit) {
        int safeLimit = limit == null || limit < 1 ? 10 : Math.min(limit, 100);
        try {
            QueryWrapper<VideoStatEntity> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("play_count").last("LIMIT " + safeLimit);
            List<VideoStatEntity> list = videoStatMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("limit", safeLimit);
            return result;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    private void incr(String videoId, String field, Long delta) {
        if (videoId == null || videoId.isEmpty()) {
            throw new ServiceException("videoId不能为空");
        }
        long safeDelta = delta == null ? 1L : delta;
        if (safeDelta == 0) {
            return;
        }
        try {
            getOrInit(videoId);
            UpdateWrapper<VideoStatEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("video_id", videoId);
            wrapper.setSql(field + " = " + field + " + (" + safeDelta + ")");
            wrapper.set("update_time", LocalDateTime.now());
            if (videoStatMapper.update(null, wrapper) <= 0) {
                throw new ServiceException("更新统计失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }
}
