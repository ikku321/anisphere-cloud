package com.iikun.animessage.modules.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.animessage.modules.notification.dto.NotificationQueryDTO;
import com.iikun.animessage.modules.notification.dto.NotificationRequestDTO;
import com.iikun.animessage.modules.notification.entity.Notification;
import com.iikun.animessage.modules.notification.mapper.NotificationMapper;
import com.iikun.animessage.modules.notification.service.NotificationService;
import com.iikun.animessage.modules.notification.vo.NotificationResponseVO;
import com.iikun.animessage.feign.client.UserFeignClient;
import com.iikun.animessage.feign.entity.dto.UserDTO;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 通知业务逻辑接口实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    private final UserFeignClient userFeignClient;

    @Override
    public Page<NotificationResponseVO> getNotificationPage(NotificationQueryDTO queryDTO) {
        Page<Notification> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();

        // 只能看自己的通知
        Result<UserDTO> userDTOResult = userFeignClient.getUserByToken();
//        String userId = UserContext.getUser() != null ? UserContext.getUser().getUid() : null;
//        if (userId == null && !StringUtils.hasText(queryDTO.getTargetUser())) {
//            throw new ServiceException("用户未登录");
//        }
        String uid = userDTOResult.getData().getUserId();
        wrapper.eq(Notification::getTargetUser, uid != null ? uid : queryDTO.getTargetUser());

        // 分类筛选
        if (StringUtils.hasText(queryDTO.getCategory())) {
            wrapper.eq(Notification::getCategory, queryDTO.getCategory());
        }

        // 已读/未读筛选
        if (queryDTO.getIsRead() != null) {
            wrapper.eq(Notification::getIsRead, queryDTO.getIsRead());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(Notification::getCreateTime);

        Page<Notification> notificationPage = this.page(page, wrapper);

        // 转换为 VO
        Page<NotificationResponseVO> voPage = new Page<>(notificationPage.getCurrent(), notificationPage.getSize(), notificationPage.getTotal());
        voPage.setRecords(notificationPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));

        return voPage;
    }

    @Override
    public void sendNotification(NotificationRequestDTO requestDTO) {
        // 使用 Feign 验证接收者是否存在
        Result<UserDTO> userResult = userFeignClient.getUserById(requestDTO.getTargetUser());
        if (userResult == null || userResult.getCode() != 200 || userResult.getData() == null) {
            log.error("发送通知失败，接收者用户不存在: {}", requestDTO.getTargetUser());
            throw new ServiceException("接收者用户不存在");
        }

        Notification notification = new Notification();
        BeanUtils.copyProperties(requestDTO, notification);

        // 设置业务ID
        notification.setNotificationId(UUID.randomUUID().toString().replace("-", ""));
        // 默认未读
        notification.setIsRead(0);

        if (!this.save(notification)) {
            throw new ServiceException("发送通知失败");
        }
    }

    @Override
    public void markAsRead(String notificationId) {
        Notification notification = this.getByNotificationId(notificationId);
        notification.setIsRead(1);

        if (!this.updateById(notification)) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public void markAllAsRead(String userId) {
        if (userId == null) {
            // userId = UserContext.getUser() != null ? UserContext.getUser().getUid() : null;
            Result<UserDTO> userDTOResult = userFeignClient.getUserByToken();
            userId = userDTOResult.getData().getUserId();
        }
        if (userId == null) {
            throw new ServiceException("用户未登录");
        }

        Notification updateEntity = new Notification();
        updateEntity.setIsRead(1);

        this.update(updateEntity, new LambdaQueryWrapper<Notification>()
                .eq(Notification::getTargetUser, userId)
                .eq(Notification::getIsRead, 0));
    }

    @Override
    public void deleteNotification(String notificationId) {
        Notification notification = this.getByNotificationId(notificationId);
        if (!this.removeById(notification.getId())) {
            throw new ServiceException("删除通知失败");
        }
    }

    @Override
    public long getUnreadCount(String userId) {
        if (userId == null) {
            // userId = UserContext.getUser() != null ? UserContext.getUser().getUid() : null;
            Result<UserDTO> userDTOResult = userFeignClient.getUserByToken();
            userId = userDTOResult.getData().getUserId();
        }
        if (userId == null) {
            return 0;
        }
        return this.count(new LambdaQueryWrapper<Notification>()
                .eq(Notification::getTargetUser, userId)
                .eq(Notification::getIsRead, 0));
    }

    @Override
    public long broadcastToAll(String category, String title, String content) {
        // 1) 拉所有启用状态用户 ID. user-service 不可达时 fallback 返回空列表,
        //    此时广播 0 条 (不报错, 不阻塞公告主表保存).
        Result<List<String>> userIdsResult = userFeignClient.listAllActiveUserIds();
        if (userIdsResult == null || userIdsResult.getCode() != 200 || userIdsResult.getData() == null) {
            log.warn("[NotificationServiceImpl.broadcastToAll] 拉取用户列表失败, 跳过广播");
            return 0L;
        }
        List<String> userIds = userIdsResult.getData();
        if (userIds.isEmpty()) {
            return 0L;
        }

        // 2) 为每个用户构造一条 Notification 实体
        List<Notification> batch = new ArrayList<>(userIds.size());
        for (String uid : userIds) {
            if (uid == null || uid.isBlank()) {
                continue;
            }
            Notification n = new Notification();
            n.setNotificationId(UUID.randomUUID().toString().replace("-", ""));
            n.setTargetUser(uid);
            n.setCategory(category);
            n.setTitle(title);
            n.setContent(content);
            n.setIsRead(0);
            batch.add(n);
        }
        if (batch.isEmpty()) {
            return 0L;
        }

        // 3) 批量插入 (MyBatis-Plus saveBatch). 默认 1000 一批事务提交.
        boolean ok = this.saveBatch(batch);
        if (!ok) {
            log.warn("[NotificationServiceImpl.broadcastToAll] saveBatch 返回 false, target={}\u6761", batch.size());
            return 0L;
        }
        log.info("[NotificationServiceImpl.broadcastToAll] 广播完成, category={}, count={}", category, batch.size());
        return batch.size();
    }

    private Notification getByNotificationId(String notificationId) {
        Notification notification = this.getOne(new LambdaQueryWrapper<Notification>()
                .eq(Notification::getNotificationId, notificationId));
        if (notification == null) {
            throw new ServiceException("通知不存在");
        }
        return notification;
    }

    private NotificationResponseVO convertToVO(Notification notification) {
        NotificationResponseVO vo = new NotificationResponseVO();
        BeanUtils.copyProperties(notification, vo);
        return vo;
    }
}
