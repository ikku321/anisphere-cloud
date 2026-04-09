package com.iikun.anivideo.controller;

import com.iikun.anivideo.service.VideoReportService;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "提交视频举报", description = "用户举报视频违规内容")
    @PostMapping("/submit")
    public Result<?> submit(@RequestBody @Valid ReportSubmitRequest request) {
        videoReportService.submit(request.getVideoId(), request.getUserId(), request.getReason());
        return Result.success();
    }

    @Operation(summary = "管理端：分页查询举报记录", description = "按状态/视频ID/用户ID筛选")
    @GetMapping("/admin/page")
    public Result<Map<String, Object>> adminPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(required = false) Integer status,
                                                 @RequestParam(required = false) String videoId,
                                                 @RequestParam(required = false) String userId) {
        return Result.success(videoReportService.page(pageNum, pageSize, status, videoId, userId));
    }

    @Operation(summary = "管理端：标记举报已处理", description = "仅将举报记录 status 置为 1")
    @PutMapping("/admin/handle")
    public Result<?> handle(@RequestParam Long reportId) {
        videoReportService.markHandled(reportId);
        return Result.success();
    }

    @Data
    public static class ReportSubmitRequest {
        @NotBlank
        private String videoId;
        @NotBlank
        private String userId;
        @NotBlank
        private String reason;
    }
}
