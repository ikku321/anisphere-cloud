package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.VideoPlayHistoryEntity;
import com.iikun.anivideo.service.VideoPlayHistoryService;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "记录或更新播放进度", description = "同一 userId + videoId 只保留一条记录，用于断点续播")
    @PostMapping("/record")
    public Result<?> record(@RequestBody @Valid RecordRequest request) {
        videoPlayHistoryService.record(request.getUserId(), request.getVideoId(), request.getLastPosition());
        return Result.success();
    }

    @Operation(summary = "查询单个视频播放进度")
    @GetMapping("/detail")
    public Result<VideoPlayHistoryEntity> detail(@RequestParam String userId, @RequestParam String videoId) {
        return Result.success(videoPlayHistoryService.detail(userId, videoId));
    }

    @Operation(summary = "分页查询我的播放历史")
    @GetMapping("/page")
    public Result<Map<String, Object>> page(@RequestParam String userId,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(videoPlayHistoryService.pageByUser(userId, pageNum, pageSize));
    }

    @Operation(summary = "删除单条播放历史")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String userId, @RequestParam String videoId) {
        videoPlayHistoryService.deleteOne(userId, videoId);
        return Result.success();
    }

    @Operation(summary = "清空指定用户播放历史")
    @DeleteMapping("/clear")
    public Result<?> clear(@RequestParam String userId) {
        videoPlayHistoryService.clearByUser(userId);
        return Result.success();
    }

    @Operation(summary = "管理端：分页查询播放历史", description = "支持按 userId / videoId 筛选")
    @GetMapping("/admin/page")
    public Result<Map<String, Object>> adminPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(required = false) String userId,
                                                 @RequestParam(required = false) String videoId) {
        return Result.success(videoPlayHistoryService.adminPage(pageNum, pageSize, userId, videoId));
    }

    @Operation(summary = "管理端：删除单条播放历史")
    @DeleteMapping("/admin/delete")
    public Result<?> adminDelete(@RequestParam String userId, @RequestParam String videoId) {
        videoPlayHistoryService.deleteOne(userId, videoId);
        return Result.success();
    }

    @Data
    public static class RecordRequest {
        @NotBlank
        private String userId;

        @NotBlank
        private String videoId;

        @Min(0)
        private Integer lastPosition;
    }
}
