package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.TagEntity;
import com.iikun.anivideo.service.TagService;
import com.iikun.anivideo.service.VideoTagService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 视频标签表控制层
 */
@RestController
@RequestMapping("/video/tag")
@RequiredArgsConstructor
@Tag(name = "标签管理", description = "处理视频标签的增删改查操作")
public class TagController {

    private final TagService tagService;

    private final VideoTagService videoTagService;

    @Operation(summary = "新增标签")
    @PostMapping("/add-tag")
    public Result<?> addTag(@RequestParam String name, @RequestParam(required = false) String type) {
        if (name == null || name.trim().isEmpty()) {
            throw new ServiceException("标签名称不能为空!");
        }
        if (name.length() > 20) {
            throw new ServiceException("标签名称不能超过20个字符");
        }
        tagService.insertTag(name, type);
        return Result.success();
    }

    @Operation(summary = "更新标签")
    @PostMapping("/update")
    public Result<?> updateTag(@RequestParam Long tagId, @RequestParam String name, 
                              @RequestParam(required = false) String type) {
        if (tagId == null) {
            throw new ServiceException("标签ID不能为空");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new ServiceException("标签名称不能为空");
        }
        if (name.length() > 20) {
            throw new ServiceException("标签名称不能超过20个字符");
        }
        tagService.updateTag(tagId, name, type);
        return Result.success();
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/delete")
    public Result<?> deleteTag(@RequestParam Long tagId) {
        if (tagId == null) {
            throw new ServiceException("标签ID不能为空!");
        }
        tagService.deleteTag(tagId);
        return Result.success();
    }

    @Operation(summary = "查询所有标签列表")
    @GetMapping("/all")
    public Result<List<TagEntity>> selectAllTag() {
        return Result.success(tagService.allTag());
    }

    @Operation(summary = "根据标签名称查询标签内容")
    @GetMapping("/found-name")
    public Result<List<TagEntity>> selectAll(@RequestParam String name) {
        if (name == null) {
            throw new ServiceException("标签名称不能为空");
        }
        return Result.success(tagService.selectByTagName(name));
    }

    @Operation(summary = "根据标签ID查询")
    @GetMapping("/found-id")
    public Result<TagEntity> foundByTagId(@RequestParam Long tagId) {
        if (tagId == null) {
            throw new ServiceException("标签ID不能为空");
        }
        return Result.success(tagService.selectByTagId(tagId));
    }
}
