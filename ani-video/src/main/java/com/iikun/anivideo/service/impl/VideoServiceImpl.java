package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iikun.anivideo.entity.VideoEntity;
import com.iikun.anivideo.mapper.VideoMapper;
import com.iikun.anivideo.service.AuditTaskService;
import com.iikun.anivideo.service.VideoService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.DateTimeUtil;
import com.iikun.common.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * author iikun
 * time 2026/2/13 0:36
 * version 1.0.0
 * msg:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoMapper videoMapper;

    private final AuditTaskService auditTaskService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(VideoEntity videoEntity) {
        try {
            int insert = videoMapper.insert(videoEntity);
            if (insert <= 0) {
                throw new ServiceException("添加失败!");
            }

            // 如果失败 → 抛异常 → 整体回滚
            auditTaskService.newAuditTask(videoEntity.getVideoId());
        } catch (DataAccessException e) {
            log.error("数据库异常", e);
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void modifiVideoVisible(Integer visible, String videoId) {
        try {
            val integer = videoMapper.updateVideoVisible(visible, videoId);
            if (integer <= 0) {
                throw new ServiceException("修改失败!");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void updateVideoDescription(String description, String videoId) {
        try {
            // 验证视频id是否存在
            val integer = videoMapper.foundByVideoId(videoId);
            if (integer <= 0) {
                throw new ServiceException("视频id不存在？");
            }

            // 执行修改
            val updateByVideoDescription = videoMapper.updateByVideoDescription(description, videoId);
            if (updateByVideoDescription <= 0) {
                throw new ServiceException("修改失败!");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void modifiVideoVideoTitle(String videoTitle, String videoId) {
        try {
            if (videoMapper.foundByVideoId(videoId) <= 0) {
                throw new ServiceException("视频id不存在");
            }
            int updated = videoMapper.updateVideoTitle(videoTitle, videoId);
            if (updated <= 0) {
                throw new ServiceException("修改失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void deleteVideo(String videoId) {
        try {
            if (videoMapper.foundByVideoId(videoId) <= 0) {
                throw new ServiceException("视频id不存在");
            }
            // 执行删除视频操作
            val deleted = videoMapper.delete(videoId);
            if (deleted <= 0) {
                throw new ServiceException("删除视频失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }


    @Override
    public List<VideoEntity> getVideoAll() {
        try {
            val videoEntities = videoMapper.selectByVideoAll();
            if (videoEntities == null || videoEntities.isEmpty()) {
                throw new ServiceException("查询所有视频数据为空?");
            }
            return videoEntities;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常?");
        }
    }


    @Override
    public List<VideoEntity> foundVideoInfo(String videoTitle) {
        try {
            val videoEntity = videoMapper.selectByVideoTitle(videoTitle);
            if (videoEntity == null) {
                throw new ServiceException("未查询到该视频信息?");
            }
            return videoEntity;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常?");
        }
    }

    @Override
    public VideoEntity getVideoById(String videoId) {
        try {
            if (videoMapper.foundByVideoId(videoId) <= 0) {
                throw new ServiceException("视频不存在");
            }
            return videoMapper.selectByVideoId(videoId);
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public List<VideoEntity> getVideosByUserId(String userId) {
        try {
            val videos = videoMapper.selectByUserId(userId);
            if (videos == null || videos.isEmpty()) {
                return new ArrayList<>();
            }
            return videos;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public Map<String, Object> getVideoPage(Integer pageNum, Integer pageSize, String keyword) {
        try {
            Page<VideoEntity> page = new Page<>(pageNum, pageSize);
            QueryWrapper<VideoEntity> queryWrapper = new QueryWrapper<>();

            // 只查询可见且审核通过的视频
            queryWrapper.eq("visible", 1)
                    .eq("audit_status", 1);

            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                        .like("title", keyword)
                        .or()
                        .like("description", keyword));
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("create_time");

            IPage<VideoEntity> videoPage = videoMapper.selectPage(page, queryWrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("records", videoPage.getRecords());
            result.put("total", videoPage.getTotal());
            result.put("current", videoPage.getCurrent());
            result.put("pages", videoPage.getPages());
            result.put("size", videoPage.getSize());

            return result;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public Map<String, Object> adminGetVideoPage(Integer pageNum,
                                                 Integer pageSize,
                                                 String keyword,
                                                 String userId,
                                                 Integer status,
                                                 Integer visible,
                                                 Integer auditStatus) {
        try {
            int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
            int safePageSize = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 100);

            Page<VideoEntity> page = new Page<>(safePageNum, safePageSize);
            QueryWrapper<VideoEntity> queryWrapper = new QueryWrapper<>();

            if (userId != null && !userId.trim().isEmpty()) {
                queryWrapper.eq("user_id", userId);
            }
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            if (visible != null) {
                queryWrapper.eq("visible", visible);
            }
            if (auditStatus != null) {
                queryWrapper.eq("audit_status", auditStatus);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                        .like("video_id", keyword)
                        .or()
                        .like("title", keyword)
                        .or()
                        .like("description", keyword));
            }

            queryWrapper.orderByDesc("create_time");
            IPage<VideoEntity> videoPage = videoMapper.selectPage(page, queryWrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("records", videoPage.getRecords());
            result.put("total", videoPage.getTotal());
            result.put("current", videoPage.getCurrent());
            result.put("pages", videoPage.getPages());
            result.put("size", videoPage.getSize());

            return result;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void updateVideoStatus(Integer status, String videoId) {
        try {
            if (videoMapper.foundByVideoId(videoId) <= 0) {
                throw new ServiceException("视频不存在");
            }
            int updated = videoMapper.updateVideoStatus(status, videoId);
            if (updated <= 0) {
                throw new ServiceException("修改失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void updateAuditStatus(Integer auditStatus, String videoId) {
        try {
            if (videoMapper.foundByVideoId(videoId) <= 0) {
                throw new ServiceException("视频不存在");
            }
            int updated = videoMapper.updateAuditStatus(auditStatus, videoId);
            if (updated <= 0) {
                throw new ServiceException("修改失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void updateVideoPrice(BigDecimal price, String videoId) {
        try {
            if (videoMapper.foundByVideoId(videoId) <= 0) {
                throw new ServiceException("视频不存在");
            }
            int updated = videoMapper.updateVideoPrice(price, videoId);
            if (updated <= 0) {
                throw new ServiceException("修改失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void batchDeleteVideos(List<String> videoIds) {
        try {
            for (String videoId : videoIds) {
                if (videoMapper.foundByVideoId(videoId) <= 0) {
                    throw new ServiceException("视频不存在: " + videoId);
                }
            }

            int deleted = videoMapper.batchDeleteVideos(videoIds);
            if (deleted <= 0) {
                throw new ServiceException("批量删除失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public List<VideoEntity> getHotVideos(Integer limit) {
        try {
            // 这里应该根据播放量、点赞数等排序，暂时按创建时间排序
            QueryWrapper<VideoEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("visible", 1)
                    .eq("audit_status", 1)
                    .eq("status", 1)
                    .orderByDesc("create_time")
                    .last("LIMIT " + limit);

            return videoMapper.selectList(queryWrapper);
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public List<VideoEntity> getRecommendVideos(String userId, Integer limit) {
        try {
            // 简单推荐算法：排除用户自己的视频，返回热门视频
            QueryWrapper<VideoEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("user_id", userId)  // 排除用户自己的视频
                    .eq("visible", 1)
                    .eq("audit_status", 1)
                    .eq("status", 1)
                    .orderByDesc("create_time")
                    .last("LIMIT " + limit);

            return videoMapper.selectList(queryWrapper);
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public VideoEntity getVideoInfo(String videoId) {
        try {
            val videoEntity = videoMapper.selectVideoInfoById(videoId);
            if (videoEntity == null) {
                throw new ServiceException("查询到的视频为空!");
            }
            return videoEntity;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常!");
        }
    }

    /**
     * 查询指定用户「审核中」的视频列表。
     *
     * <p>实现细节：</p>
     * <ul>
     *     <li>匹配条件：<code>user_id = uid</code> 且 <code>status = 0</code>（0 = 审核中，见 VideoEntity 注释）；</li>
     *     <li>排序：按 <code>create_time DESC</code>，让最新提交的排在前面；</li>
     *     <li>空结果场景返回 <code>new ArrayList&lt;&gt;()</code>，避免调用方处理 null。</li>
     * </ul>
     */
    @Override
    public List<VideoEntity> getMyAuditingVideos(String uid) {
        if (uid == null || uid.isBlank()) {
            throw new ServiceException("用户ID不能为空");
        }
        try {
            QueryWrapper<VideoEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", uid)
                    .eq("status", 0)
                    .orderByDesc("create_time");
            List<VideoEntity> list = videoMapper.selectList(wrapper);
            return list == null ? new ArrayList<>() : list;
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常!");
        }
    }
}













