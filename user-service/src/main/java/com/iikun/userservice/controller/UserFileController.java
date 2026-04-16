package com.iikun.userservice.controller;

import com.iikun.common.base.Result;
import com.iikun.userservice.service.UserFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * 用户文件上传控制器
 * <p>
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Tag(name = "用户文件管理", description = "用于上传头像等操作")
@RestController
@RequestMapping("/user-file")
@RequiredArgsConstructor
public class UserFileController {

    private final UserFileService userFileService;

    /**
     * 上传用户头像文件接口
     */
    @Operation(summary = "上传用户头像")
    @PutMapping(
            value = "/upload/user/avatar-url",
            consumes = "multipart/form-data"
    )
    public Result<String> uploadUserAvatarUrlFile(@RequestParam("file") MultipartFile file) {
        String url = userFileService.uploadUserAvatarUrlFile(file);
        // 将已经保存的地址返回
        return Result.success(url);
    }

}
