package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.VideoStatEntity;
import com.iikun.anivideo.service.VideoStatService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "管理端：获取视频统计信息", description = "若统计记录不存在则初始化为 0")
    @GetMapping("/admin/detail")
    public Result<VideoStatEntity> detail(@RequestParam String videoId) {
        return Result.success(videoStatService.getOrInit(videoId));
    }

    @Operation(summary = "管理端：增量更新统计", description = "delta 可正可负（不建议传过大）")
    @PutMapping("/admin/incr")
    public Result<?> incr(@RequestBody @Valid StatIncrRequest request) {
        switch (request.getType()) {
            case "play" -> videoStatService.incrPlay(request.getVideoId(), request.getDelta());
            case "like" -> videoStatService.incrLike(request.getVideoId(), request.getDelta());
            case "share" -> videoStatService.incrShare(request.getVideoId(), request.getDelta());
            case "comment" -> videoStatService.incrComment(request.getVideoId(), request.getDelta());
            default -> throw new ServiceException("type不合法");
        }
        return Result.success();
    }

    @Operation(summary = "管理端：播放量排行榜", description = "按 playCount 倒序")
    @GetMapping("/admin/top-play")
    public Result<Map<String, Object>> topPlay(@RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(videoStatService.topPlay(limit));
    }

    @Data
    public static class StatIncrRequest {
        @NotBlank
        private String videoId;
        @NotBlank
        private String type;
        @NotNull
        private Long delta;
    }
}
