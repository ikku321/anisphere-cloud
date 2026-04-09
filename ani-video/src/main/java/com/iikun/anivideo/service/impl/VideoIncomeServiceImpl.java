package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iikun.anivideo.entity.VideoIncomeEntity;
import com.iikun.anivideo.mapper.VideoIncomeMapper;
import com.iikun.anivideo.service.VideoIncomeService;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 视频收益服务实现
 * <p>
 * 处理UP主相关的收益流水相关业务逻辑
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoIncomeServiceImpl implements VideoIncomeService {

    private final VideoIncomeMapper videoIncomeMapper;

    @Override
    public void addIncome(String videoId, String userId, Integer incomeType, BigDecimal amount) {
        if (videoId == null || videoId.isEmpty()) {
            throw new ServiceException("videoId不能为空");
        }
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("userId不能为空");
        }
        if (incomeType == null || incomeType < 1 || incomeType > 3) {
            throw new ServiceException("incomeType不合法");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("amount必须大于0");
        }

        VideoIncomeEntity entity = new VideoIncomeEntity();
        entity.setVideoId(videoId);
        entity.setUserId(userId);
        entity.setIncomeType(incomeType);
        entity.setAmount(amount);
        entity.setCreateTime(LocalDateTime.now());

        try {
            if (videoIncomeMapper.insert(entity) <= 0) {
                throw new ServiceException("新增收益记录失败");
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public Map<String, Object> page(Integer pageNum, Integer pageSize, String videoId, String userId, Integer incomeType) {
        int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 100);

        try {
            Page<VideoIncomeEntity> page = new Page<>(safePageNum, safePageSize);
            QueryWrapper<VideoIncomeEntity> wrapper = new QueryWrapper<>();
            if (videoId != null && !videoId.trim().isEmpty()) {
                wrapper.eq("video_id", videoId);
            }
            if (userId != null && !userId.trim().isEmpty()) {
                wrapper.eq("user_id", userId);
            }
            if (incomeType != null) {
                wrapper.eq("income_type", incomeType);
            }
            wrapper.orderByDesc("create_time");

            IPage<VideoIncomeEntity> resultPage = videoIncomeMapper.selectPage(page, wrapper);

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
    public BigDecimal sumAmount(String videoId, String userId, Integer incomeType) {
        try {
            QueryWrapper<VideoIncomeEntity> wrapper = new QueryWrapper<>();
            wrapper.select("COALESCE(SUM(amount), 0) AS amount");
            if (videoId != null && !videoId.trim().isEmpty()) {
                wrapper.eq("video_id", videoId);
            }
            if (userId != null && !userId.trim().isEmpty()) {
                wrapper.eq("user_id", userId);
            }
            if (incomeType != null) {
                wrapper.eq("income_type", incomeType);
            }
            Object obj = videoIncomeMapper.selectObjs(wrapper).stream().findFirst().orElse(BigDecimal.ZERO);
            if (obj instanceof BigDecimal) {
                return (BigDecimal) obj;
            }
            return new BigDecimal(String.valueOf(obj));
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }
}
