package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.config.UploadConfig;
import com.iikun.anivideo.service.FileService;
import com.iikun.anivideo.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 文件服务实现
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UploadConfig uploadConfig;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
            String uploadDir = uploadConfig.getUploadDir();
            File dir = new File(uploadDir);
            // 如果目录不存在就创建
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(dir, newFileName);
            file.transferTo(dest);
            return "/uploads/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

}
