package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.VideoIncomeEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
public interface VideoIncomeService {

    /**
     * 新增收益记录
     */
    boolean addIncome(VideoIncomeEntity income);

    /**
     * 查询UP主收益列表
     */
    List<VideoIncomeEntity> getUserIncome(String userId);

    /**
     * 统计UP主总收益
     */
    BigDecimal getUserTotalIncome(String userId);
}
