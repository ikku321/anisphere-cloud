package com.iikun.userservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author iikun
 * time 2025/9/19 23:07
 * version 1.0.0
 * msg:
 */
@RestController
@RequestMapping("/user-browsing-history")
@Tag(name = "用户浏览控制层", description = "用于记录用户点击各种浏览")
public class UserBrowsingHistoryController {

}
