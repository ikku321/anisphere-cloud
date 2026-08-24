package com.iikun.anivideo.controller;

import com.iikun.anivideo.service.VideoChunkService;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 视频分片上传控制器
 * <p>
 * 处理大文件分片上传相关操作
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/video/chunk")
@RequiredArgsConstructor
@Tag(name = "视频分片上传", description = "处理视频文件分片上传和合并")
public class VideoChunkController {

    private final VideoChunkService videoChunkService;

    @Operation(summary = "初始化分片上传任务")
    @PostMapping("/init")
    public Result<Map<String, Object>> init(@RequestBody InitRequest request) {
        return Result.success(videoChunkService.init(
                request.getFileName(),
                request.getFileSize(),
                request.getChunkSize()
        ));
    }

    @Operation(summary = "查询分片上传状态")
    @GetMapping("/status")
    public Result<Map<String, Object>> status(@RequestParam String uploadId) {
        return Result.success(videoChunkService.status(uploadId));
    }

    @Operation(summary = "上传单个视频分片")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Result<Map<String, Object>> uploadChunk(@RequestParam String uploadId,
                                                   @RequestParam Integer chunkIndex,
                                                   @RequestParam Integer totalChunks,
                                                   @RequestParam("file") MultipartFile file,
                                                   @RequestParam(required = false) String videoId) {
        return Result.success(videoChunkService.uploadChunk(uploadId, chunkIndex, totalChunks, file, videoId));
    }

    @Operation(summary = "合并视频分片")
    @PostMapping("/merge")
    public Result<Map<String, Object>> merge(@RequestBody MergeRequest request) {
        return Result.success(videoChunkService.merge(
                request.getUploadId(),
                request.getFileName(),
                request.getTotalChunks()
        ));
    }

    @Operation(summary = "清理分片上传任务")
    @DeleteMapping("/cleanup")
    public Result<?> cleanup(@RequestParam String uploadId) {
        videoChunkService.cleanup(uploadId);
        return Result.success();
    }

    @Data
    public static class InitRequest {
        private String fileName;
        private Long fileSize;
        private Integer chunkSize;
    }

    @Data
    public static class MergeRequest {
        private String uploadId;
        private String fileName;
        private Integer totalChunks;
    }
}
