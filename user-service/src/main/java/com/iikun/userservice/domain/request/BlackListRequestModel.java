package com.iikun.userservice.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/2/8 12:32
 * version 1.0.0
 * msg: 用户黑名单详情实体（多表查询结果）
 */
@Data
public class BlackListRequestModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 黑名单记录ID
     */
    private Long id;

    /**
     * 操作人用户ID（黑名单发起者）
     */
    private String userId;

    /**
     * 操作人UID（user表）
     */
    private String uid;

    /**
     * 操作人昵称
     */
    private String nickname;

    /**
     * 操作人头像
     */
    private String avatarUrl;

    /**
     * 被拉黑用户ID
     */
    private String blockedUserId;

    /**
     * 被拉黑用户UID
     */
    private String blockedUserUid;

    /**
     * 被拉黑用户昵称
     */
    private String blockedUserNickname;

    /**
     * 被拉黑用户头像
     */
    private String blockedUserAvatarUrl;

    /**
     * 拉黑时间
     */
    private LocalDateTime createTime;
}

