package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iikun.anivideo.entity.TagEntity;
import com.iikun.anivideo.entity.VideoTagEntity;
import com.iikun.anivideo.mapper.VideoTagMapper;
import com.iikun.anivideo.service.VideoTagService;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 视频标签关系服务实现
 * <p>
 * 处理视频与标签的多对多关系管理
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoTagServiceImpl implements VideoTagService {

    private final VideoTagMapper videoTagMapper;

    @Override
    public void addVideoTag(String videoId, Integer tagId) {
        try {
            videoTagMapper.add(videoId, tagId);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("该视频已绑定该标签，请勿重复操作！");
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public void deleteVideoTag(String videoTagId) {
        try {
            val deleted = videoTagMapper.delete(Integer.parseInt(videoTagId));
            if (!deleted) {
                throw new ServiceException("删除视频标签关联失败!");
            }
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }


}











