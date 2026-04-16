package com.iikun.anivideo.controller;

import com.iikun.anivideo.service.FileService;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 文件上传控制器
 */
@RestController
@RequestMapping("/video/video-file")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    /**
     * 上传视频封面文件接口
     */
    @Operation(summary = "上传视频封面")
    @PostMapping(
            value = "/upload/video-img",
            consumes = "multipart/form-data"
    )
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadFile(file);
        return Result.success(url);
    }


    @Operation(summary = "上传视频文件")
    @PutMapping(
            value = "/upload/video-file",
            consumes = "multipart/form-data"
    )
    public Result<String> uploadVideoFile(@RequestParam("file") MultipartFile file) {
        return Result.success(fileService.uploadVideoFile(file));
    }

    @GetMapping("/test")
    public String Test() {
        return "Hello World";
    }
}


















