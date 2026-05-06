package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.config.UploadConfig;
import com.iikun.anivideo.service.FileService;
import com.iikun.anivideo.utils.FileUtil;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
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
            // transferTo 对相对路径会基于 Tomcat 临时目录解析,必须先转绝对路径
            File dir = new File(uploadDir).getAbsoluteFile();
            // 如果目录不存在就创建
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(dir, newFileName);
            file.transferTo(dest);
            // 返回的 URL 路径必须与 VideoUploadStaticConfig 注册的 /uploads/video/** 一致
            return "/uploads/video/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadVideoFile(MultipartFile file) {

        String suffix = getString(file);

        // MIME校验（加分点）
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("video/mp4")) {
            throw new ServiceException("文件类型必须为MP4视频!");
        }

        try {
            String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
            String uploadDir = uploadConfig.getUploadDir();
            // transferTo 对相对路径会基于 Tomcat 临时目录解析,必须先转绝对路径
            File dir = new File(uploadDir).getAbsoluteFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(dir, newFileName);
            file.transferTo(dest);
            return "/uploads/video/" + newFileName;
        } catch (Exception e) {
            throw new ServiceException("文件上传失败: " + e.getMessage());
        }
    }

    @NonNull
    private static String getString(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ServiceException("视频文件不能为空!");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new ServiceException("文件格式不正确!");
        }

        // 后缀校验
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!".mp4".equals(suffix)) {
            throw new ServiceException("只支持上传MP4格式视频!");
        }
        return suffix;
    }

}
