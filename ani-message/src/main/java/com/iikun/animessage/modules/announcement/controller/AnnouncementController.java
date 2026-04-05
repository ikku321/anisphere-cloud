package com.iikun.animessage.modules.announcement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iikun.animessage.modules.announcement.dto.AnnouncementQueryDTO;
import com.iikun.animessage.modules.announcement.dto.AnnouncementRequestDTO;
import com.iikun.animessage.modules.announcement.service.AnnouncementService;
import com.iikun.animessage.modules.announcement.vo.AnnouncementResponseVO;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 公告管理控制器
 */
@Tag(name = "公告管理", description = "平台公告/全局通知")
@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Operation(summary = "分页查询公告")
    @GetMapping("/page")
    public Result<Page<AnnouncementResponseVO>> getAnnouncementPage(AnnouncementQueryDTO queryDTO) {
        return Result.success(announcementService.getAnnouncementPage(queryDTO));
    }

    @Operation(summary = "获取公告详情")
    @GetMapping("/{announcementId}")
    public Result<AnnouncementResponseVO> getAnnouncementDetail(@PathVariable String announcementId) {
        return Result.success(announcementService.getAnnouncementDetail(announcementId));
    }

    @Operation(summary = "创建公告")
    @PostMapping
    public Result<Void> createAnnouncement(@RequestBody @Valid AnnouncementRequestDTO requestDTO) {
        announcementService.createAnnouncement(requestDTO);
        return Result.success();
    }

    @Operation(summary = "更新公告")
    @PutMapping("/{announcementId}")
    public Result<Void> updateAnnouncement(@PathVariable String announcementId, @RequestBody @Valid AnnouncementRequestDTO requestDTO) {
        announcementService.updateAnnouncement(announcementId, requestDTO);
        return Result.success();
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{announcementId}")
    public Result<Void> deleteAnnouncement(@PathVariable String announcementId) {
        announcementService.deleteAnnouncement(announcementId);
        return Result.success();
    }

    @Operation(summary = "发布公告")
    @PostMapping("/{announcementId}/publish")
    public Result<Void> publishAnnouncement(@PathVariable String announcementId) {
        announcementService.publishAnnouncement(announcementId, 1);
        return Result.success();
    }

    @Operation(summary = "撤回公告")
    @PostMapping("/{announcementId}/revoke")
    public Result<Void> revokeAnnouncement(@PathVariable String announcementId) {
        announcementService.publishAnnouncement(announcementId, 0);
        return Result.success();
    }
}
