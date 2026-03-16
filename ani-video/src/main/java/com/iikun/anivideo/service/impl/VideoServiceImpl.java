package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.entity.VideoEntity;
import com.iikun.anivideo.mapper.VideoMapper;
import com.iikun.anivideo.service.VideoService;
import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.DateTimeUtil;
import com.iikun.common.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

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
        // log.info(videoEntity.toString());
        int insert = videoMapper.insert(videoEntity);
        if (insert <= 0) {
            throw new ServiceException("添加失败!");
        }
    }
}

