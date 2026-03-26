package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.VideoStatEntity;
import com.iikun.anivideo.service.VideoStatService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 视频统计信息控制器
 * <p>
 * 处理视频播放量、点赞数、分享数、评论数等统计操作
 * </p>
 */
@RestController
@RequestMapping("/video/stat")
@RequiredArgsConstructor
@Tag(name = "视频统计", description = "处理视频播放量、点赞数、分享数、评论数等统计操作")
public class VideoStatController {

    private final VideoStatService videoStatService;

}
