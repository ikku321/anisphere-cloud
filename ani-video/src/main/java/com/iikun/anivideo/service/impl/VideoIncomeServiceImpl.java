package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.entity.VideoIncomeEntity;
import com.iikun.anivideo.service.VideoIncomeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Service
public class VideoIncomeServiceImpl implements VideoIncomeService {
    @Override
    public boolean addIncome(VideoIncomeEntity income) {
        return false;
    }

    @Override
    public List<VideoIncomeEntity> getUserIncome(String userId) {
        return List.of();
    }

    @Override
    public BigDecimal getUserTotalIncome(String userId) {
        return null;
    }
}
