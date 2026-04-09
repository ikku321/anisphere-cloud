package com.iikun.anivideo.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 视频收益服务接口
 * <p>
 * 处理UP主相关的收益流水相关业务逻辑
 * </p>
 */
public interface VideoIncomeService {

    void addIncome(String videoId, String userId, Integer incomeType, BigDecimal amount);

    Map<String, Object> page(Integer pageNum, Integer pageSize, String videoId, String userId, Integer incomeType);

    BigDecimal sumAmount(String videoId, String userId, Integer incomeType);
}
