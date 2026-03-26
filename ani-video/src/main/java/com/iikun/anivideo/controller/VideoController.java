package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.TagEntity;
import com.iikun.anivideo.entity.VideoEntity;
import com.iikun.anivideo.service.VideoService;
import com.iikun.common.annotation.Admin;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.DateTimeUtil;
import com.iikun.common.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2026/2/13 0:28
 * version 1.0.0
 * msg: 视频管理控制层
 */
@RestController
@RequestMapping("/video")
@Tag(name = "视频管理", description = "处理视频的上传和转码，查询等操作")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Operation(summary = "上传视频信息")
    @PostMapping("/video-list")
    public Result<?> uploading(@RequestBody VideoEntity videoEntity, @RequestParam String uid) {
        // 执行将数据存储到sql表中
        videoService.save(getVideoEntity(videoEntity, uid));
        return Result.success();
    }

    @Operation(summary = "修改视频可见状态")
    @PostMapping("/modifi-visible")
    public Result<?> modifiVisible(@RequestParam Integer visible, @RequestParam String videoId) {
        if (visible == null) {
            return Result.failed("数值不能为空?");
        }
        if (visible != 1 && visible != 0) {
            return Result.failed("数值只能选择0和1?");
        }
        if (videoId == null) {
            return Result.failed("视频id不能为空?");
        }
        videoService.modifiVideoVisible(visible, videoId);
        return Result.success();
    }


    @Operation(summary = "修改视频简介")
    @PostMapping("/update-description")
    public Result<?> updateDescription(@RequestParam String description, @RequestParam String videoId) {
        if (description == null) {
            throw new ServiceException("简介不能为空!");
        }
        if (description.length() > 300) {
            throw new ServiceException("简介字数不能大于300字");
        }
        // 执行修改
        videoService.updateVideoDescription(description, videoId);
        return Result.success();
    }

    @Operation(summary = "修改视频标题")
    @PostMapping("/update-title")
    public Result<?> uploadVideo(@RequestParam String videoTitle, @RequestParam String videoId) {
        if (videoTitle == null) throw new ServiceException("视频标题不能为空");
        if (videoId == null) throw new ServiceException("视频id不能为空");
        if (videoTitle.length() > 30) throw new ServiceException("视频标题字数不能大于30位");
        videoService.modifiVideoVideoTitle(videoTitle, videoId);
        return Result.success();
    }


    @Operation(summary = "删除视频")
    @DeleteMapping("/delete")
    public Result<?> deleteVideo(@RequestParam String videoId) {
        videoService.deleteVideo(videoId);
        return Result.success();
    }

    @Operation(summary = "查询所有视频信息")
    @GetMapping("/all")
    public Result<List<VideoEntity>> getVideoList() {
        return Result.success(videoService.getVideoAll());
    }

    @Operation(summary = "根据标题查询视频信息")
    @GetMapping("/found-title")
    public Result<List<VideoEntity>> getFoundVideoInfo(@RequestParam String videoTitle) {
        if (videoTitle == null) {
            throw new ServiceException("视频标题不能为空?");
        }
        return Result.success(videoService.foundVideoInfo(videoTitle));
    }

    @Operation(summary = "根据视频ID查询视频详情")
    @GetMapping("/detail")
    public Result<VideoEntity> getVideoDetail(@RequestParam String videoId) {
        if (videoId == null) {
            throw new ServiceException("视频ID不能为空");
        }
        return Result.success(videoService.getVideoById(videoId));
    }

    @Operation(summary = "根据用户ID查询视频列表")
    @GetMapping("/user-videos")
    public Result<List<VideoEntity>> getUserVideos(@RequestParam String userId) {
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }
        return Result.success(videoService.getVideosByUserId(userId));
    }

    @Operation(summary = "分页查询视频列表")
    @GetMapping("/page")
    public Result<Map<String, Object>> getVideoPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(videoService.getVideoPage(pageNum, pageSize, keyword));
    }

    @Operation(summary = "修改视频状态")
    @PostMapping("/update-status")
    public Result<?> updateVideoStatus(@RequestParam Integer status, @RequestParam String videoId) {
        if (status == null || videoId == null) {
            throw new ServiceException("参数不能为空");
        }
        if (status < 0 || status > 4) {
            throw new ServiceException("状态值不合法");
        }
        videoService.updateVideoStatus(status, videoId);
        return Result.success();
    }

    @Operation(summary = "修改视频审核状态")
    @PostMapping("/update-audit-status")
    public Result<?> updateAuditStatus(@RequestParam Integer auditStatus, @RequestParam String videoId) {
        if (auditStatus == null || videoId == null) {
            throw new ServiceException("参数不能为空");
        }
        if (auditStatus < 0 || auditStatus > 2) {
            throw new ServiceException("审核状态值不合法");
        }
        videoService.updateAuditStatus(auditStatus, videoId);
        return Result.success();
    }

    @Operation(summary = "修改视频价格")
    @PostMapping("/update-price")
    public Result<?> updateVideoPrice(@RequestParam BigDecimal price, @RequestParam String videoId) {
        if (price == null || videoId == null) {
            throw new ServiceException("参数不能为空");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("价格不能为负数");
        }
        videoService.updateVideoPrice(price, videoId);
        return Result.success();
    }

    @Operation(summary = "批量删除视频")
    @DeleteMapping("/batch-delete")
    public Result<?> batchDeleteVideos(@RequestBody List<String> videoIds) {
        if (videoIds == null || videoIds.isEmpty()) {
            throw new ServiceException("视频ID列表不能为空");
        }
        videoService.batchDeleteVideos(videoIds);
        return Result.success();
    }

    @Operation(summary = "获取热门视频列表")
    @GetMapping("/hot-videos")
    public Result<List<VideoEntity>> getHotVideos(@RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(videoService.getHotVideos(limit));
    }

    @Operation(summary = "获取推荐视频列表")
    @GetMapping("/recommend-videos")
    public Result<List<VideoEntity>> getRecommendVideos(@RequestParam String userId, 
                                                       @RequestParam(defaultValue = "10") Integer limit) {
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }
        return Result.success(videoService.getRecommendVideos(userId, limit));
    }

    @Operation(summary = "根据id获取视频详情")
    @GetMapping("/found-videoinfo")
    public Result<VideoEntity> getVideoInfo(@RequestParam String videoId) {
        if (videoId == null) {
            throw new ServiceException("视频id不能为空!");
        }
        return Result.success(videoService.getVideoInfo(videoId));
    }

    /**
     * 填充完整的数据
     *
     * @param videoEntity 数据实体类
     * @param uid         用户uid
     * @return 返回完整的视频实体数据
     */
    private VideoEntity getVideoEntity(VideoEntity videoEntity, String uid) {
        // 生成视频唯一id
        videoEntity.setVideoId(Utils.videoId());
        // 发布者ID
        videoEntity.setUserId(uid);
        // 视频标题
        videoEntity.setTitle(videoEntity.getTitle());
        // 视频简介
        videoEntity.setDescription(videoEntity.getDescription());
        // 封面图片地址
        videoEntity.setCoverUrl(videoEntity.getCoverUrl());
        // 视频文件地址
        videoEntity.setVideoUrl(videoEntity.getVideoUrl());
        // 视频时长（秒）
        videoEntity.setDuration(videoEntity.getDuration());
        // 视频状态(默认为审核状态)
        videoEntity.setStatus(0);
        // 是否公开
        videoEntity.setVisible(videoEntity.getVisible());
        // 视频价格
        videoEntity.setPrice(videoEntity.getPrice());
        // 审核状态 (默认为待审核)
        videoEntity.setAuditStatus(0);
        // 创建时间
        videoEntity.setCreateTime(DateTimeUtil.now());
        // 更新时间
        videoEntity.setUpdateTime(DateTimeUtil.now());
        return videoEntity;
    }

}


























