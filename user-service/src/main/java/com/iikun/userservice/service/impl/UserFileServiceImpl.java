package com.iikun.userservice.service.impl;

import com.iikun.userservice.config.UploadUserConfig;
import com.iikun.userservice.service.UserFileService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

/**
 * 用户文件业务逻辑接口定义
 * <p>
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Service
public class UserFileServiceImpl implements UserFileService {

    @Resource
    private UploadUserConfig uploadUserConfig;

    @Override
    public String uploadUserAvatarUrlFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
            String uploadDir = uploadUserConfig.getUploadDir();
            // 必须解析成绝对路径,否则 transferTo 会基于 Tomcat 临时工作目录解析,
            // 导致文件被写到 <user.home>/AppData/Local/Temp/tomcat.<port>.../ 下找不到。
            File dir = new File(uploadDir).getAbsoluteFile();
            // 如果目录不存在就创建
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(dir, newFileName);
            file.transferTo(dest);
            return "/uploads/user/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }
}
