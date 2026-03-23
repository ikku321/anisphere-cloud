package com.iikun.anivideo.controller;

import com.iikun.anivideo.entity.TagEntity;
import com.iikun.anivideo.service.TagService;
import com.iikun.anivideo.service.VideoService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 视频标签表控制层
 */
@RestController
@RequestMapping("/video/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "新增标签")
    @PostMapping("/add-tag")
    public Result<?> addTag(@RequestParam String tag) {
        if (tag == null) {
            throw new ServiceException("标签内容不能为空!");
        }
        tagService.insertTag(tag);
        return Result.success();
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/delete")
    public Result<?> deleteTag(@RequestParam String tagId) {
        if (tagId == null) {
            throw new ServiceException("标签id不能为空!");
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

    @Operation(summary = "根据标签id查询")
    @GetMapping("/foudn-id")
    public Result<TagEntity> foundByTagId(@RequestParam String tagId) {
        if (tagId == null) {
            throw new ServiceException("标签id不能为空");
        }
        return Result.success(tagService.selectByTagId(tagId));
    }
}
