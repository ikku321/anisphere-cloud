package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.VideoIncomeEntity;
import com.iikun.anivideo.service.VideoIncomeService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 视频收益流水表控制器
 * <p>
 * 处理UP主相关的收益流水相关操作
 * </p>
 */
@RestController
@RequestMapping("/video/income")
@RequiredArgsConstructor
@Tag(name = "视频收益", description = "处理UP主相关的收益流水相关操作")
public class VideoIncomeController {

    private final VideoIncomeService videoIncomeService;


}
