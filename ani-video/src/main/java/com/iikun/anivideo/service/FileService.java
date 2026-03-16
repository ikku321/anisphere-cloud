package com.iikun.anivideo.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 文件服务接口
 */

public interface FileService {

    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file);

}
