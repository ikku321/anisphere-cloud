package com.iikun.anivideo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author iikun
 * time 2026/2/13 0:28
 * version 1.0.0
 * msg:
 */
@RestController
@RequestMapping("/video")
@Tag(name = "视频管理", description = "处理视频的上传和转码，查询等操作")
public class VideoController {

}
