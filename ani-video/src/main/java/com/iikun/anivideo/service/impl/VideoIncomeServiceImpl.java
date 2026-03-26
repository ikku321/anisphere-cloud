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

}
