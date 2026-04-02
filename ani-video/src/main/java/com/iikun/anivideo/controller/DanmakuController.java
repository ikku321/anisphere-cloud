package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.DTO.DanmakuDTO;
import com.iikun.anivideo.service.DanmakuService;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

/**
 * 弹幕控制器
 * <p>
 * 功能说明：
 * 提供弹幕相关接口，包括发送弹幕、获取弹幕列表、点赞等操作。
 * <p>
 * 接口特点：
 * - RESTful 风格
 * - 统一返回 Result 封装
 * - 与前端播放器联动（按时间轴加载弹幕）
 * <p>
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 */
@Tag(name = "弹幕管理", description = "弹幕发送、获取、点赞等接口")
@RestController
@RequestMapping("/danmaku")
@RequiredArgsConstructor
public class DanmakuController {

    /**
     * 弹幕业务服务
     */
    private final DanmakuService danmakuService;

    /**
     * 发送弹幕
     * <p>
     * 请求头：
     * - userId：当前登录用户ID（建议后续改为JWT解析）
     *
     * @param dto 弹幕数据（内容、时间轴、颜色等）
     * @return 成功响应
     */
    @Operation(summary = "发送弹幕")
    @PostMapping("/send")
    public Result<?> send(@RequestBody DanmakuDTO dto) {
        danmakuService.sendDanmaku(dto);
        return Result.success();
    }

    /**
     * 获取视频弹幕列表
     * <p>
     * 使用场景：
     * - 视频播放前加载弹幕
     * - 拖动进度条后重新获取
     *
     * @param videoId 视频ID
     * @return 弹幕列表（按时间排序）
     */
    @Operation(summary = "获取视频弹幕列表")
    @GetMapping("/list")
    public Result<?> list(@RequestParam("videoId") String videoId) {
        return Result.success(danmakuService.getDanmakuList(videoId));
    }

    /**
     * 弹幕点赞
     *
     * @param id 弹幕ID
     * @return 成功响应
     */
    @Operation(summary = "弹幕点赞")
    @PostMapping("/like")
    public Result<?> like(@RequestParam("id") Long id) {
        danmakuService.like(id);
        return Result.success();
    }

    /**
     * 删除指定的弹幕
     * 只能删除当前用户所发送的弹幕
     *
     * @param id 弹幕id
     * @return 1
     */
    @Operation(summary = "删除弹幕")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam("id") Long id) {
        danmakuService.deleteByDanmaku(id);
        return Result.success("删除成功");
    }

}
















