package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.entity.DTO.DanmakuDTO;
import com.iikun.anivideo.entity.DTO.UserDTO;
import com.iikun.anivideo.entity.Danmaku;
import com.iikun.anivideo.mapper.DanmakuMapper;
import com.iikun.anivideo.service.DanmakuService;
import com.iikun.anivideo.service.UserService;
import com.iikun.anivideo.ws.DanmakuPushService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 弹幕业务核心实现类
 * <p>
 * 功能说明：
 * 实现弹幕系统的核心业务逻辑，包括发送弹幕、查询弹幕、点赞等操作。
 * <p>
 * 设计说明：
 * - 使用 Service 层封装业务逻辑
 * - 调用 Mapper 层完成数据库操作
 * - 统一进行参数校验与异常控制
 * <p>
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DanmakuServiceImpl implements DanmakuService {

    /**
     * 弹幕数据访问对象（MyBatis Mapper）
     */
    private final DanmakuMapper danmakuMapper;

    /**
     * 用户数据访问对象
     */
    private final UserService userService;

    /**
     * 弹幕 WebSocket 推送中枢：入库后向同视频房间所有观众广播。
     */
    private final DanmakuPushService danmakuPushService;

    /**
     * 发送弹幕
     * <p>
     * 业务逻辑：
     * 1. 校验弹幕内容长度
     * 2. DTO 转 Entity
     * 3. 设置默认值（状态、点赞数）
     * 4. 写入数据库
     *
     * @param dto 弹幕数据传输对象
     */
    @Override
    public void sendDanmaku(DanmakuDTO dto) {
        try {
            // 参数校验
            if (dto == null || dto.getContent() == null || dto.getContent().isEmpty()) {
                throw new ServiceException("弹幕内容不能为空");
            }
            if (dto.getContent().length() > 100) {
                throw new ServiceException("弹幕过长");
            }
            // 获取用户uid
            Result<UserDTO> byTokenUserInfo = userService.getByTokenUserInfo();
            if (byTokenUserInfo.getCode() != 200) {
                log.info("获取用户信息失败：{}", byTokenUserInfo.getMessage());
                throw new ServiceException("获取用户信息失败!");
            }

            // DTO → Entity
            Danmaku danmaku = new Danmaku();
            BeanUtils.copyProperties(dto, danmaku);
            // 设置系统字段
            danmaku.setUserId(byTokenUserInfo.getData().getUserId());
            danmaku.setStatus(1);   // 默认正常
            danmaku.setLikes(0);    // 默认0点赞
            // 入库
            int result = danmakuMapper.insert(danmaku);
            if (result <= 0) {
                throw new ServiceException("发送弹幕失败");
            }

            // WebSocket 实时广播：把这条弹幕推给同 videoId 房间的所有在线观众。
            // mybatis-plus 默认开启 useGeneratedKeys，danmaku.id 会被回填。
            // 推送失败仅记录日志：弹幕已入库，对方下次拉历史能看到，不影响主流程。
            try {
                danmakuPushService.pushToRoom(danmaku.getVideoId(), danmaku);
            } catch (Exception e) {
                log.warn("Danmaku WS 推送失败：videoId={}, msg={}", danmaku.getVideoId(), e.getMessage());
            }
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: {}" + e.getMessage());
        }
    }

    /**
     * 获取视频弹幕列表
     * <p>
     * 使用场景：
     * - 视频播放前加载弹幕
     *
     * @param videoId 视频ID
     * @return 按时间排序的弹幕列表
     */
    @Override
    public List<Danmaku> getDanmakuList(String videoId) {
        try {
            if (videoId == null || videoId.isEmpty()) {
                throw new ServiceException("视频ID不能为空");
            }
            return danmakuMapper.selectByVideoId(videoId);
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: {}" + e.getMessage());
        }
    }

    /**
     * 弹幕点赞
     * <p>
     * 业务逻辑：
     * - 对指定弹幕点赞数 +1
     * <p>
     * 注意：
     * - 当前未限制重复点赞（可扩展）
     *
     * @param id 弹幕ID
     */
    @Override
    public void like(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("弹幕ID不能为空");
            }
            int result = danmakuMapper.likeDanmaku(id);
            if (result <= 0) {
                throw new ServiceException("点赞失败");
            }
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: {}" + e.getMessage());
        }
    }


    /**
     * 删除当前用户所发布的弹幕
     * 注: 只能删除用户已经发布的弹幕且必须是当前用户自己的弹幕
     *
     * @param id 弹幕id
     */
    @Override
    public void deleteByDanmaku(Long id) {
        try {
            // 验证弹幕是否属于当前用户所发送的弹幕
            // 获取用户信息
            Result<UserDTO> userDTOResult = userService.getByTokenUserInfo();
            // 查询弹幕
            Danmaku danmaku = danmakuMapper.selectById(id);
            if (danmaku == null) {
                throw new ServiceException("查询该弹幕id为空");
            }
            // 比较
            if (!Objects.equals(danmaku.getUserId(), userDTOResult.getData().getUserId())) {
                throw new ServiceException("删除失败! 弹幕不属于当前用户所发布的弹幕");
            }

            int updateStatus = danmakuMapper.updateStatus(id, 0);
            if (updateStatus <= 0) {
                throw new ServiceException("删除弹幕失败");
            }
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: {}" + e.getMessage());
        }
    }
}