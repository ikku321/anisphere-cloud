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

    /**
     * 当前登录用户是否关注了「列表里这一行的用户」。
     * 用于 UI 上展示「回关 / 互相关注 / 已关注」状态：
     * - 在「我的粉丝」列表：true = 互相关注，false = 可点击「回关」
     * - 在「我的关注」列表：恒为 true（既然在我的关注里那一定是我关注的）
     * - 看「别人」的列表时：仍按当前登录用户口径计算
     * 未登录或未注入查询者 id 时，由后端返回 false。
     */
    private Boolean isMyFollowing;
}
