package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.entity.VideoStatEntity;
import com.iikun.anivideo.service.VideTagService;
import com.iikun.anivideo.service.VideoStatService;
import org.springframework.stereotype.Service;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Service
public class VideoStatServiceimpl implements VideoStatService {
    @Override
    public boolean initVideoStat(String videoId) {
        return false;
    }

    @Override
    public void increasePlay(String videoId) {

    }

    @Override
    public void increaseLike(String videoId) {

    }

    @Override
    public void increaseComment(String videoId) {

    }

    @Override
    public void increaseShare(String videoId) {

    }

    @Override
    public VideoStatEntity getStat(String videoId) {
        return null;
    }
}
