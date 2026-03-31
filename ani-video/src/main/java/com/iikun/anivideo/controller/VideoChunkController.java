package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.VideoChunkEntity;
import com.iikun.anivideo.service.VideoChunkService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

}
