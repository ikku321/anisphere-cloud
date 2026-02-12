package com.iikun.userservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author iikun
 * time 2025/9/19 23:12
 * version 1.0.0
 * msg: 用户收藏表控制层
 */
@RestController
@RequestMapping("/user-collection")
@Tag(name = "收藏表", description = "用户收藏表，可收藏视频，动漫")
public class UserCollectionController {


}
