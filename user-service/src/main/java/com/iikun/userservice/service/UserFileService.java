package com.iikun.userservice.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 用户文件业务逻辑接口定义
 * <p>
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
public interface UserFileService {

    /**
     * 上传用户头像地址
     *
     * @param file 文件
     * @return 上传成功后的地址
     */
    String uploadUserAvatarUrlFile(MultipartFile file);
}
