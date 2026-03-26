package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.VideoEntity;
import com.iikun.anivideo.service.VideoTagService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 视频标签关系控制层
 */
@RestController
@RequestMapping("/video/tag")
@RequiredArgsConstructor
public class VideoTagController {

    private final VideoTagService videoTagService;

    @Operation(summary = "为视频添加标签")
    @PostMapping("/video-tag")
    public Result<String> addTag(@RequestParam String videoId, @RequestParam String tagId) {
        if (videoId == null || tagId == null) {
            throw new ServiceException("视频id或标签id不能为空!");
        }
        videoTagService.addVideoTag(videoId, Integer.parseInt(tagId));
        return Result.success();
    }

    @Operation(summary = "删除视频标签")
    @DeleteMapping("/delete-videoTag")
    public Result<String> deleteTag(@RequestParam String videoTagId) {
        if (videoTagId == null) {
            throw new ServiceException("视频标签id不能为空!");
        }
        videoTagService.deleteVideoTag(videoTagId);
        return  Result.success();
    }
}
