package com.iikun.anivideo.entity;

import lombok.Data;

import java.util.Date;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 弹幕数据库映射
 */
@Data
public class Danmaku {
    private Long id;
    private String videoId;
    private String userId;
    private String content;
    private String color;
    private Long position;
    private Integer likes;
    private Integer status;
    private Integer type;
    private Integer fontSize;
    private Date createTime;
}
