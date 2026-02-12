package com.iikun.userservice.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * author iikun
 * time 2025/10/18 13:03
 * version 1.0.0
 * msg: 查询关注列表返回的表字段映射模型
 */
@Data
public class AttentionListModel {
    // 关注表id
    private String attentionId;
    // 被关注用户id
    private String followerId;
    // 关注者id
    private String userId;
    // 关注者uid
    private String userUid;
    // 关注者昵称
    private String nickname;
    // 关注者用户头像
    private String avatarUrl;
    // 关注时间
    private String createTime;
}
