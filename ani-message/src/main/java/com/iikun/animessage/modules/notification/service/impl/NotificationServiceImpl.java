package com.iikun.animessage.modules.notification.service.impl;

import com.iikun.animessage.modules.notification.mapper.NotificationMapper;
import com.iikun.animessage.modules.notification.service.NotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:通知业务逻辑接口实现类
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

}
