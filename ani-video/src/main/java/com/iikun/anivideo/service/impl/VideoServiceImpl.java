package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.entity.VideoEntity;
import com.iikun.anivideo.mapper.VideoMapper;
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

    @Override
    public void save(VideoEntity videoEntity) {
        try {
            // log.info(videoEntity.toString());
            int insert = videoMapper.insert(videoEntity);
            if (insert <= 0) {
                throw new ServiceException("添加失败!");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
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
}













