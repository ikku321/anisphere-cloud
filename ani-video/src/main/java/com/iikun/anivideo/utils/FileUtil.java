package com.iikun.anivideo.utils;

import java.io.File;
import java.util.UUID;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:文件工具类
 */
public class FileUtil {

    /**
     * 生成随机文件名
     */
    public static String generateFileName(String originalName){
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-","") + suffix;
    }

    /**
     * 创建目录
     */
    public static void createDir(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

}
