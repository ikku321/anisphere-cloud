package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.VideoReportEntity;
import com.iikun.anivideo.service.VideoReportService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 视频举报记录控制器
 * <p>
 * 处理用户对视频的违规举报相关操作
 * </p>
 */
@RestController
@RequestMapping("/video/report")
@RequiredArgsConstructor
@Tag(name = "视频举报", description = "处理用户对视频的违规举报相关操作")
public class VideoReportController {

    private final VideoReportService videoReportService;

}
