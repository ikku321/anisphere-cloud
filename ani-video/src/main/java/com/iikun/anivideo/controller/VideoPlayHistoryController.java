package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.VideoPlayHistoryEntity;
import com.iikun.anivideo.service.VideoPlayHistoryService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 视频播放历史控制器
 * <p>
 * 处理用户视频播放进度和历史记录相关操作
 * </p>
 */
@RestController
@RequestMapping("/video/play-history")
@RequiredArgsConstructor
@Tag(name = "视频播放历史", description = "处理用户视频播放进度和历史记录相关操作")
public class VideoPlayHistoryController {

    private final VideoPlayHistoryService videoPlayHistoryService;

}
