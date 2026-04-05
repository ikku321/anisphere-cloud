package com.iikun.animessage.modules.announcement.service.impl;

import com.iikun.animessage.modules.announcement.mapper.AnnouncementMapper;
import com.iikun.animessage.modules.announcement.service.AnnouncementService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 公告业务逻辑接口实现类
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Resource
    private AnnouncementMapper announcementMapper;


}
