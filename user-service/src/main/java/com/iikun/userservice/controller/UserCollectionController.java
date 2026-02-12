package com.iikun.userservice.controller;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.userservice.service.UserCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * author iikun
 * time 2025/9/19 23:12
 * version 1.0.0
 * msg: 用户收藏表控制层
 */
@RestController
@RequestMapping("/user-collection")
@Tag(name = "收藏表", description = "用户收藏表，可收藏视频，动漫")
@RequiredArgsConstructor
public class UserCollectionController {

    /** 用户收藏业务 */
    private final UserCollectionService userCollectionService;

    @Operation(summary = "收藏", description = "对目标内容进行收藏")
    @PostMapping("/add")
    public Result<?> add(HttpServletRequest request,
                         @RequestParam Integer targetType,
                         @RequestParam Long targetId) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("token内容不能为空");
        }
        userCollectionService.add(uid, targetType, targetId);
        return Result.success();
    }

    @Operation(summary = "取消收藏", description = "取消对目标内容的收藏")
    @PostMapping("/cancel")
    public Result<?> cancel(HttpServletRequest request,
                            @RequestParam Integer targetType,
                            @RequestParam Long targetId) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("token内容不能为空");
        }
        userCollectionService.cancel(uid, targetType, targetId);
        return Result.success();
    }

    @Operation(summary = "我的收藏（分页）", description = "分页查询当前用户收藏列表")
    @GetMapping("/page")
    public Result<?> page(HttpServletRequest request,
                          @RequestParam(required = false) Integer page,
                          @RequestParam(required = false) Integer size) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("token内容不能为空");
        }
        return Result.success(userCollectionService.pageMyCollections(uid, page, size));
    }

    @Operation(summary = "是否已收藏", description = "判断当前用户是否已收藏指定目标")
    @GetMapping("/is-collected")
    public Result<?> isCollected(HttpServletRequest request,
                                 @RequestParam Integer targetType,
                                 @RequestParam Long targetId) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("token内容不能为空");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("collected", userCollectionService.isCollected(uid, targetType, targetId));
        return Result.success(result);
    }
}
