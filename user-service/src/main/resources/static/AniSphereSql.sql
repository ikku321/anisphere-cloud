-- 删除数据库`
drop database if exists ani_sphere;

-- 创建数据库
create database if not exists ani_sphere;
use ani_sphere;

-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 用户表
-- ---------------------------------------------------------------------------------------------------------------------
CREATE TABLE `user`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`         VARCHAR(64)  NOT NULL COMMENT '用户唯一id（雪花算法/UUID）',
    `username`        VARCHAR(100) NOT NULL COMMENT '账号名称',
    `password`        VARCHAR(255) NOT NULL COMMENT '加密密码（BCrypt/Argon2）',
    `nickname`        VARCHAR(100) COMMENT '用户昵称',
    `phone`           VARCHAR(20) UNIQUE COMMENT '手机号（可登录）',
    `email`           VARCHAR(100) UNIQUE COMMENT '邮箱（可登录）',
    `gender`          TINYINT  DEFAULT 0 COMMENT '性别(0未知 1男 2女)',
    `bio`             TEXT COMMENT '用户简介',
    `birthday`        DATE COMMENT '生日',
    `avatar_url`      VARCHAR(255) COMMENT '头像地址',
    -- 社交 & 状态
    `followers_count` INT      DEFAULT 0 COMMENT '粉丝数',
    `following_count` INT      DEFAULT 0 COMMENT '关注数',
    `online_status`   TINYINT  DEFAULT 0 COMMENT '在线状态(0离线 1在线 2隐身)',
    -- 账号 & 会员
    `vip_id`          BIGINT COMMENT '会员id（关联vip表）',
    `status`          TINYINT  DEFAULT 0 COMMENT '账号状态(0正常 1禁言 2封禁 3注销中)',
    `role`            TINYINT  DEFAULT 1 COMMENT '用户角色(0管理员 1普通用户 2UP主 3审核员)',
    -- 游戏化
    `coins`           INT      DEFAULT 0 COMMENT '虚拟币（豆子）',
    `level`           INT      DEFAULT 1 COMMENT '等级',
    `exp`             INT      DEFAULT 0 COMMENT '经验值',
    -- 安全 & 登录
    `id_card_id`      BIGINT COMMENT '实名认证信息id',
    `last_login_at`   DATETIME COMMENT '最后登录时间',
    `last_login_ip`   VARCHAR(50) COMMENT '最后登录IP',
    `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_user_phone` (`phone`),
    UNIQUE KEY `uk_user_email` (`email`),
    UNIQUE KEY `uk_user_name` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';


-- TODO 收货地址表
CREATE TABLE `user_address`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`        BIGINT       NOT NULL COMMENT '用户ID（关联user.id）',
    `receiver_name`  VARCHAR(100) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20)  NOT NULL COMMENT '收货人电话',
    `province`       VARCHAR(50)  NOT NULL COMMENT '省份',
    `city`           VARCHAR(50)  NOT NULL COMMENT '城市',
    `district`       VARCHAR(50) COMMENT '区/县',
    `detail_address` VARCHAR(255) NOT NULL COMMENT '详细地址',
    `is_default`     BOOLEAN  DEFAULT FALSE COMMENT '是否为默认地址',
    `create_time`    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户收货地址表';


-- TODO 浏览记录表
CREATE TABLE `user_browsing_history`
(
    `id`          BIGINT  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`     BIGINT  NOT NULL COMMENT '用户ID（关联user.id）',
    `target_type` TINYINT NOT NULL COMMENT '内容类型(0视频 1漫画 2评论 3其他)',
    `target_id`   BIGINT  NOT NULL COMMENT '目标内容ID',
    `view_time`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_history_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户浏览记录表';


-- TODO 收藏表
CREATE TABLE `user_collection`
(
    `id`          BIGINT  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`     BIGINT  NOT NULL COMMENT '用户ID（关联user.id）',
    `target_type` TINYINT NOT NULL COMMENT '收藏类型(0视频 1漫画 2评论)',
    `target_id`   BIGINT  NOT NULL COMMENT '收藏目标ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_collection_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户收藏表';


-- TODO 登录日志表
CREATE TABLE `user_login_log`
(
    `id`         BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`    BIGINT NOT NULL COMMENT '用户ID（关联user.id）',
    `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `login_ip`   VARCHAR(50) COMMENT '登录IP地址',
    `device`     VARCHAR(100) COMMENT '登录设备信息',
    `status`     TINYINT  DEFAULT 1 COMMENT '登录状态(0失败 1成功)',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_login_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户登录日志表';


-- TODO 黑名单表
CREATE TABLE `user_blacklist`
(
    `id`              BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`         BIGINT NOT NULL COMMENT '用户ID（关联user.id）',
    `blocked_user_id` BIGINT NOT NULL COMMENT '被拉黑的用户ID（关联user.id）',
    `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '拉黑时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_blocked` (`user_id`, `blocked_user_id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_blacklist_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_blacklist_blocked` FOREIGN KEY (`blocked_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户黑名单表';


-- TODO 关注/粉丝关系表
CREATE TABLE `user_follow`
(
    `id`           BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `follower_id`  varchar(255) NOT NULL COMMENT '粉丝用户ID（关联user.id）',
    `following_id` varchar(255) NOT NULL COMMENT '被关注用户ID（关联user.id）',
    `create_time`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_follower_following` (`follower_id`, `following_id`),
    KEY `idx_follower_id` (`follower_id`),
    KEY `idx_following_id` (`following_id`),
    CONSTRAINT `fk_follow_follower` FOREIGN KEY (`follower_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_follow_following` FOREIGN KEY (`following_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户关注关系表';


-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 会员表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- 会员等级表（member）
-- 存储不同的会员类型（比如月度、年度、至尊会员等）。
CREATE TABLE `member`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `member_id`   VARCHAR(64)    NOT NULL UNIQUE COMMENT '会员唯一ID（UUID/雪花ID）',
    `name`        VARCHAR(100)   NOT NULL COMMENT '会员名称（如：月卡、年卡、至尊会员）',
    `duration`    INT            NOT NULL COMMENT '会员时长（天）',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '会员价格',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '会员说明',
    `status`      TINYINT      DEFAULT 1 COMMENT '状态：1启用 0停用',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- TODO 用户会员关系表（user_member）
-- 存储 用户的会员开通信息，支持购买 / 续费。
CREATE TABLE `user_member`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     VARCHAR(64) NOT NULL COMMENT '用户ID',
    `member_id`   VARCHAR(64) NOT NULL COMMENT '会员ID',
    `start_time`  TIMESTAMP   NOT NULL COMMENT '开始时间',
    `end_time`    TIMESTAMP   NOT NULL COMMENT '到期时间',
    `status`      TINYINT   DEFAULT 1 COMMENT '状态：1有效 0已过期 2禁用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY (`user_id`, `member_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- TODO 会员特权表（member_privilege）
-- 存储会员可以享受的特权（比如免广告、高清视频、专属表情包等）。
CREATE TABLE `member_privilege`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `privilege_key` VARCHAR(64)  NOT NULL UNIQUE COMMENT '特权标识（如 no_ads、hd_video、emoji_pack）',
    `name`          VARCHAR(100) NOT NULL COMMENT '特权名称',
    `description`   VARCHAR(255) DEFAULT NULL COMMENT '特权说明'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- TODO 会员-特权关联表（member_privilege_mapping）
-- 用于定义 不同会员类型拥有的特权。
CREATE TABLE `member_privilege_mapping`
(
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `member_id`    VARCHAR(64) NOT NULL COMMENT '会员ID',
    `privilege_id` BIGINT      NOT NULL COMMENT '特权ID',
    UNIQUE KEY (`member_id`, `privilege_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 视频模块表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 视频主表
CREATE TABLE `video`
(
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `video_id`     VARCHAR(64)  NOT NULL UNIQUE COMMENT '视频唯一ID',
    `user_id`      VARCHAR(64)  NOT NULL COMMENT '发布者ID',
    `title`        VARCHAR(255) NOT NULL COMMENT '标题',
    `description`  TEXT COMMENT '简介',
    `cover_url`    VARCHAR(255)   DEFAULT NULL COMMENT '封面图',
    `video_url`    VARCHAR(255)   DEFAULT NULL COMMENT '合并后视频文件地址',
    `duration`     INT            DEFAULT 0 COMMENT '时长（秒）',
    `status`       TINYINT        DEFAULT 0 COMMENT '状态：0审核中 1正常 2隐藏 3违规 4付费',
    `visible`      TINYINT        DEFAULT 1 COMMENT '可见性：1公开 0隐藏',
    `price`        DECIMAL(10, 2) DEFAULT 0 COMMENT '价格（0=免费）',
    `audit_status` TINYINT        DEFAULT 0 COMMENT '审核：0待审 1通过 2拒绝',
    `create_time`  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    `update_time`  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (`user_id`),
    INDEX idx_title (`title`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- TODO 分片上传表
-- 把一个大视频文件切成 多个小分片（chunk），客户端逐片上传到服务器，上传完成后再由服务器合并成完整文件。
CREATE TABLE `video_chunk`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `upload_id`   VARCHAR(64)  NOT NULL COMMENT '上传任务ID',
    `video_id`    VARCHAR(64)  NOT NULL COMMENT '视频ID',
    `chunk_index` INT          NOT NULL COMMENT '分片序号',
    `chunk_path`  VARCHAR(255) NOT NULL COMMENT '分片文件地址',
    `status`      TINYINT   DEFAULT 0 COMMENT '状态：0未上传 1已上传',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY (`upload_id`, `chunk_index`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ===========================================
-- 标签表：存放系统内所有视频可用标签
-- 用于分类/主题/风格等统一管理
-- ===========================================
CREATE TABLE `tag`
(
    `id`   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `name` VARCHAR(100) NOT NULL UNIQUE COMMENT '标签名，需全局唯一，例如：搞笑、影视剪辑、科技评测',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '标签分类类型，可用于二级归类，例如：category/genre/theme 等；可为空'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='视频标签维表（分类/主题/风格等）';


-- ===========================================
-- 视频-标签映射表：视频与标签的多对多关系
-- 一条记录表示“某视频绑定了某标签”
-- ===========================================
CREATE TABLE `video_tag`
(
    `id`       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `video_id` VARCHAR(64) NOT NULL COMMENT '视频ID（业务ID/雪花ID），指向 video.video_id',
    `tag_id`   BIGINT      NOT NULL COMMENT '标签主键ID，指向 tag.id',
    UNIQUE KEY (`video_id`, `tag_id`) COMMENT '同一视频同一标签仅能绑定一次，防止重复'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='视频与标签的关联表（多对多）';
-- 说明：如需按 tag_id 反查视频，建议额外创建索引：ALTER TABLE video_tag ADD INDEX idx_tag(tag_id);


-- ===========================================
-- 视频热度指标表：按视频维度聚合的四类热度计数
-- 通常由异步任务/消息队列/埋点汇总，允许最终一致
-- ===========================================
CREATE TABLE `video_stat`
(
    `video_id`      VARCHAR(64) PRIMARY KEY COMMENT '视频ID（业务ID/雪花ID），与 video.video_id 对应，作为主键一对一',
    `play_count`    BIGINT    DEFAULT 0 COMMENT '播放次数累计（去重策略由业务决定，可为近实时汇总）',
    `like_count`    BIGINT    DEFAULT 0 COMMENT '点赞数累计',
    `share_count`   BIGINT    DEFAULT 0 COMMENT '分享数累计（站内/站外分享总和）',
    `comment_count` BIGINT    DEFAULT 0 COMMENT '评论数累计（含楼层/楼中楼聚合）',
    `update_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次聚合/写入时间（便于缓存失效/排序）'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='视频热度统计表（播放/点赞/分享/评论聚合）';


-- ===========================================
-- 视频播放记录表：用户对单个视频的断点续播进度
-- 一用户一视频仅保留一条记录（唯一约束）
-- ===========================================
CREATE TABLE `video_play_history`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `user_id`       VARCHAR(64) NOT NULL COMMENT '用户ID（业务ID/雪花ID），指向用户系统',
    `video_id`      VARCHAR(64) NOT NULL COMMENT '视频ID（业务ID/雪花ID），指向 video.video_id',
    `last_position` INT       DEFAULT 0 COMMENT '最近播放进度，单位：秒（精确到秒；更高精度可由业务自行扩展）',
    `update_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次更新进度的时间',
    UNIQUE KEY (`user_id`, `video_id`) COMMENT '同一用户对同一视频仅有一条记录，用于UPSERT更新'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='视频播放进度表（断点续播）';


-- ===========================================
-- 视频举报表：记录用户对视频的违规举报
-- 可配合审核流使用（人工/算法处理）
-- ===========================================
CREATE TABLE `video_report`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `video_id`    VARCHAR(64)  NOT NULL COMMENT '被举报的视频ID（业务ID/雪花ID），指向 video.video_id',
    `user_id`     VARCHAR(64)  NOT NULL COMMENT '举报人用户ID（业务ID/雪花ID）',
    `reason`      VARCHAR(255) NOT NULL COMMENT '举报原因文案（可选：存枚举code + 详情）',
    `status`      TINYINT   DEFAULT 0 COMMENT '处理状态：0待处理 1已处理（含通过/驳回等细分可在审核记录表体现）',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '举报创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='视频举报记录表（待处理/已处理）';
-- 说明：如需高效筛查未处理举报，建议加索引：ALTER TABLE video_report ADD INDEX idx_status(status);


-- ===========================================
-- 视频收益表：记录UP主相关的收益流水
-- 收益类型可包含：平台分成/广告收益/付费购买等
-- ===========================================
CREATE TABLE `video_income`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `video_id`    VARCHAR(64)    NOT NULL COMMENT '视频ID（业务ID/雪花ID），指向 video.video_id',
    `user_id`     VARCHAR(64)    NOT NULL COMMENT 'UP主/收益归属用户ID（业务ID/雪花ID）',
    `income_type` TINYINT        NOT NULL COMMENT '收益类型：1=平台分成 2=广告收益 3=付费购买（可扩展）',
    `amount`      DECIMAL(10, 2) NOT NULL COMMENT '本次收益金额（单位：元，保留两位小数）',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收益入账/记账时间',
    INDEX idx_user (`user_id`) COMMENT '便于按UP主聚合/分页查询个人收益'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='视频收益流水表（分成/广告/付费等）';



-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 审核模块表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- ---------------------------------------------------------------------------------------------------------------------
-- 审核任务表：记录每个视频的审核任务分配情况
-- 用于调度审核流程，分发给审核员（可配合任务池/调度中心）
-- ---------------------------------------------------------------------------------------------------------------------
CREATE TABLE `audit_task`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `video_id`    VARCHAR(64) NOT NULL COMMENT '待审核的视频ID（业务ID/雪花ID），指向 video.video_id',
    `assign_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '任务分配时间（派发到审核队列的时间）',
    `status`      TINYINT   DEFAULT 0 COMMENT '任务状态：0待审 1进行中 2完成（完成后可查对应的 audit_record）',
    INDEX idx_video (`video_id`) COMMENT '加速按视频ID查询任务'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='审核任务表（记录视频审核流程中的任务状态）';


-- ---------------------------------------------------------------------------------------------------------------------
-- 审核记录表：记录审核员对视频的具体审核结果
-- 一条记录代表一次审核操作，可存在多条（多轮/多审）
-- ---------------------------------------------------------------------------------------------------------------------
CREATE TABLE `audit_record`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `video_id`    VARCHAR(64) NOT NULL COMMENT '被审核的视频ID，指向 video.video_id',
    `auditor_id`  VARCHAR(64) NOT NULL COMMENT '审核人用户ID（业务ID/雪花ID），指向用户表',
    `result`      TINYINT     NOT NULL COMMENT '审核结果：1通过 2拒绝（可扩展更多状态，如3=待人工复审）',
    `comment`     VARCHAR(255) DEFAULT NULL COMMENT '审核意见或备注，存储原因/说明',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间（操作提交时间）',
    INDEX idx_video (`video_id`) COMMENT '加速按视频ID查询审核记录'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='审核记录表（记录审核员对视频的处理结果）';


-- ---------------------------------------------------------------------------------------------------------------------
-- 审核组申请表：记录普通用户申请加入审核组的请求
-- 用于构建社区共治/志愿审核员体系
-- ---------------------------------------------------------------------------------------------------------------------
CREATE TABLE `audit_group_apply`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `user_id`     VARCHAR(64) NOT NULL COMMENT '申请人用户ID（业务ID/雪花ID），指向用户表',
    `reason`      VARCHAR(255) DEFAULT NULL COMMENT '申请理由/自荐说明',
    `status`      TINYINT      DEFAULT 0 COMMENT '审核状态：0待审核 1通过 2拒绝',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '申请提交时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='审核组申请表（用户申请成为审核员的记录）';


-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 评论表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 评论表设计（建议 MongoDB）
# {
#   "comment_id": "c123",
#   "video_id": "v123",
#   "user_id": "u456",
#   "content": "好看！",
#   "parent_id": null,
#   "reply_to": null,
#   "likes": 10,
#   "shares": 2,
#   "create_time": "2025-09-09T02:00:00Z",
#   "children": [
#     {
#       "comment_id": "c124",
#       "user_id": "u789",
#       "content": "同感",
#       "parent_id": "c123",
#       "reply_to": "u456"
#     }
#   ]
# }


-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 弹幕表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- ---------------------------------------------------------------------------------------------------------------------
-- 弹幕表：记录用户在视频播放过程中的实时评论信息（带时间戳）
-- 用于实现视频播放时的“飞屏弹幕”效果
-- ---------------------------------------------------------------------------------------------------------------------
CREATE TABLE `danmaku`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键（内部用）',
    `video_id`    VARCHAR(64)  NOT NULL COMMENT '所属视频ID，指向 video.video_id',
    `user_id`     VARCHAR(64)  NOT NULL COMMENT '发送弹幕的用户ID，指向 user.user_id',
    `content`     VARCHAR(255) NOT NULL COMMENT '弹幕内容（文字内容，限制长度避免刷屏）',
    `color`       VARCHAR(10) DEFAULT '#FFFFFF' COMMENT '弹幕文字颜色（十六进制格式，如#FFFFFF=白色）',
    `position`    BIGINT       NOT NULL COMMENT '弹幕出现的时间戳（毫秒，基于视频播放进度）',
    `likes`       INT         DEFAULT 0 COMMENT '弹幕点赞数（用户可对某条弹幕点赞）',
    `status`      TINYINT     DEFAULT 1 COMMENT '弹幕状态：1正常 0删除 2被举报（待审核/处理）',
    `create_time` TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '弹幕发送时间',
    INDEX idx_video (`video_id`) COMMENT '加速按视频ID查询弹幕（播放时拉取用）'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='弹幕表（记录用户发送的实时弹幕，支持时间戳同步与举报管理）';



-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 消息模块（Message / Chat）表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- ======================================================
-- 会话表：支持私聊/群聊/频道
-- type: 1=私聊(两人) 2=群聊 3=频道(广播)
-- ======================================================
CREATE TABLE `conversation`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '内部主键',
    `conversation_id` VARCHAR(64) NOT NULL UNIQUE COMMENT '业务会话ID（雪花/UUID）',
    `type`            TINYINT     NOT NULL COMMENT '会话类型：1=私聊 2=群聊 3=频道',
    `title`           VARCHAR(255) DEFAULT NULL COMMENT '群/频道名称（私聊可为空或对方昵称）',
    `owner_id`        VARCHAR(64)  DEFAULT NULL COMMENT '群/频道拥有者/创建者 user_id',
    `extra`           JSON         DEFAULT NULL COMMENT '扩展信息（如群公告、头像等）',
    `create_time`     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `update_time`     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='会话表（私聊/群聊/频道）';


-- ======================================================
-- 会话成员表：记录会话中的成员及其权限/角色
-- role: 1=普通成员 2=管理员 3=群主
-- mute_until: 免打扰/禁言截止时间（若支持）
-- ======================================================
CREATE TABLE `conversation_member`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `conversation_id` VARCHAR(64) NOT NULL COMMENT 'conversation.conversation_id',
    `user_id`         VARCHAR(64) NOT NULL COMMENT '成员 user_id',
    `role`            TINYINT          DEFAULT 1 COMMENT '角色：1成员 2管理员 3群主',
    `join_time`       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    `mute_until`      TIMESTAMP   NULL DEFAULT NULL COMMENT '若被禁言则记录禁言到期时间',
    UNIQUE KEY `uk_conv_user` (`conversation_id`, `user_id`),
    INDEX `idx_conv` (`conversation_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='会话成员表';


-- ======================================================
-- 消息表：主消息持久化（用于漫游/拉历史）
-- 支持撤回（recalled）、删除（deleted）、类型区分（text/image/...）
-- ======================================================
CREATE TABLE `message`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '内部主键',
    `message_id`      VARCHAR(64) NOT NULL UNIQUE COMMENT '业务消息ID（雪花/UUID）',
    `conversation_id` VARCHAR(64) NOT NULL COMMENT '所属会话 conversation.conversation_id',
    `from_user`       VARCHAR(64) NOT NULL COMMENT '发送者 user_id（系统消息可为NULL或system）',
    `type`            VARCHAR(50) DEFAULT 'text' COMMENT '消息类型：text/image/audio/system/notice/... ',
    `content`         TEXT COMMENT '消息内容（文本或序列化的元数据）',
    `attachment`      JSON        DEFAULT NULL COMMENT '附件元数据（若有），如 URL/大小/格式等',
    `seq`             BIGINT      DEFAULT NULL COMMENT '会话内顺序号（用于保证多端一致性、漫游）',
    `recalled`        TINYINT     DEFAULT 0 COMMENT '是否已撤回：0否 1是（撤回后客户端按需展示）',
    `deleted`         TINYINT     DEFAULT 0 COMMENT '是否已逻辑删除（仅数据库层面）',
    `create_time`     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_conv_time` (`conversation_id`, `create_time`),
    INDEX `idx_from_time` (`from_user`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息表：存储聊天消息，供历史/漫游/回溯使用';


-- ======================================================
-- 消息已读/未读状态（多端/多用户）
-- 每行代表某用户对某消息的阅读状态（适用于群/私聊）
-- read_flag: 0未读 1已读
-- ======================================================
CREATE TABLE `message_read_status`
(
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `message_id` VARCHAR(64) NOT NULL COMMENT 'message.message_id',
    `user_id`    VARCHAR(64) NOT NULL COMMENT '接收/阅览用户 user_id',
    `read_flag`  TINYINT          DEFAULT 0 COMMENT '0未读 1已读',
    `read_time`  TIMESTAMP   NULL DEFAULT NULL COMMENT '具体阅读时间（若已读）',
    UNIQUE KEY `uk_msg_user` (`message_id`, `user_id`),
    INDEX `idx_user_unread` (`user_id`, `read_flag`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息已读/未读记录（支持多端漫游）';


-- ======================================================
-- 消息撤回/操作日志（选用）
-- 如果需要保留审计/撤回记录，可以记录在此表
-- ======================================================
CREATE TABLE `message_action_log`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `message_id`  VARCHAR(64) NOT NULL,
    `operator_id` VARCHAR(64) NOT NULL COMMENT '执行操作的用户（撤回者）',
    `action`      VARCHAR(50) NOT NULL COMMENT '操作类型：recall/delete/modify',
    `reason`      VARCHAR(255) DEFAULT NULL COMMENT '撤回/删除原因（可选）',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    INDEX (`message_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息操作审计日志';


-- ======================================================
-- 私聊/官方消息推送表（消息推送的持久化队列/归档）
-- 用于实现 7.1/7.2（发送官方/私聊）和消息撤回/查询等
-- ======================================================
CREATE TABLE `outbox_message`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `msg_type`        VARCHAR(50) NOT NULL COMMENT '类型：official/private/system',
    `target_user`     VARCHAR(64) DEFAULT NULL COMMENT '目标用户 user_id（官方广播可为NULL或为conversation_id）',
    `conversation_id` VARCHAR(64) DEFAULT NULL COMMENT '可选：发送到会话',
    `payload`         JSON        NOT NULL COMMENT '消息体（客户端消费格式）',
    `status`          TINYINT     DEFAULT 0 COMMENT '发送状态：0未发送 1已发送 2失败',
    `retry_count`     INT         DEFAULT 0,
    `create_time`     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='外发消息队列/归档（可与消息中间件配合）';


-- ======================================================
-- 消息举报表：记录用户对消息的举报
-- ======================================================
CREATE TABLE `message_report`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `message_id`  VARCHAR(64) NOT NULL,
    `reporter_id` VARCHAR(64) NOT NULL COMMENT '举报人 user_id',
    `reason`      VARCHAR(255) DEFAULT NULL COMMENT '举报原因/详情',
    `status`      TINYINT      DEFAULT 0 COMMENT '处理状态：0待处理 1已处理',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    INDEX (`message_id`),
    INDEX `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息举报';



-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 社交模块（Friend / Follow / 动态）表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- ======================================================
-- 好友关系表（双向确认关系）
-- status: 0=待验证 1=已通过 2=拒绝/拉黑
-- ======================================================
CREATE TABLE `friend_relation`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     VARCHAR(64) NOT NULL COMMENT '发起/拥有者 user_id',
    `friend_id`   VARCHAR(64) NOT NULL COMMENT '好友 user_id',
    `status`      TINYINT      DEFAULT 0 COMMENT '0申请中 1已成为好友 2拒绝/拉黑',
    `remark`      VARCHAR(255) DEFAULT NULL COMMENT '备注名（owner 对 friend 的备注）',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_friend` (`user_id`, `friend_id`),
    INDEX `idx_user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='好友关系（双向）';


-- ======================================================
-- 好友分组表（one user 多分组）
-- ======================================================
CREATE TABLE `friend_group`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     VARCHAR(64)  NOT NULL COMMENT '拥有者 user_id',
    `name`        VARCHAR(100) NOT NULL COMMENT '分组名',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_group` (`user_id`, `name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='好友分组';


-- ======================================================
-- 好友-分组映射：将好友放入分组
-- ======================================================
CREATE TABLE `friend_group_mapping`
(
    `id`                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    `group_id`           BIGINT NOT NULL COMMENT 'friend_group.id',
    `friend_relation_id` BIGINT NOT NULL COMMENT 'friend_relation.id',
    UNIQUE KEY `uk_group_relation` (`group_id`, `friend_relation_id`),
    INDEX (`group_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='好友-分组映射';


-- ======================================================
-- 关注/粉丝（单向关系）
-- ======================================================
CREATE TABLE `follow`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     VARCHAR(64) NOT NULL COMMENT '发起关注的用户',
    `target_user` VARCHAR(64) NOT NULL COMMENT '被关注用户',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_target` (`user_id`, `target_user`),
    INDEX `idx_target` (`target_user`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='关注/粉丝（单向）';


-- ======================================================
-- 举报好友（对某好友的举报记录）
-- ======================================================
CREATE TABLE `user_report`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `reporter_id` VARCHAR(64) NOT NULL COMMENT '举报人 user_id',
    `target_user` VARCHAR(64) NOT NULL COMMENT '被举报人 user_id',
    `reason`      VARCHAR(255) DEFAULT NULL,
    `status`      TINYINT      DEFAULT 0 COMMENT '0待处理 1已处理',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户举报（含举报好友）';


-- ======================================================
-- 清除聊天记录的操作表（逻辑记录，实际删除需异步/谨慎）
-- ======================================================
CREATE TABLE `chat_history_clear`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `requester`       VARCHAR(64) NOT NULL COMMENT '发起清除的用户',
    `conversation_id` VARCHAR(64) NOT NULL,
    `clear_time`      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    `scope`           VARCHAR(50) DEFAULT 'self' COMMENT '清除范围：self/all（谨慎）'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='聊天记录清除请求记录';


-- ======================================================
-- 用户动态/说说（类似朋友圈）
-- ======================================================
CREATE TABLE `user_post`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id`       VARCHAR(64) NOT NULL UNIQUE COMMENT '业务id',
    `user_id`       VARCHAR(64) NOT NULL,
    `content`       TEXT COMMENT '动态文本',
    `media`         JSON      DEFAULT NULL COMMENT '图片/视频/附件数组（元数据）',
    `like_count`    INT       DEFAULT 0,
    `comment_count` INT       DEFAULT 0,
    `status`        TINYINT   DEFAULT 1 COMMENT '1正常 0删除/隐藏',
    `create_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user_time` (`user_id`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户动态/说说表';



-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 通知 + 公告模块（Notification / Announcement / Admin）表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- ======================================================
-- 通知表（针对单用户或多用户的通知）
-- category: system/notice/message/activity 等
-- ======================================================
CREATE TABLE `notification`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `notification_id` VARCHAR(64) NOT NULL UNIQUE COMMENT '业务ID',
    `target_user`     VARCHAR(64) NOT NULL COMMENT '接收者 user_id（多接收者请用广播/会话）',
    `category`        VARCHAR(50)  DEFAULT 'system' COMMENT '分类',
    `title`           VARCHAR(255) DEFAULT NULL,
    `content`         TEXT COMMENT '通知内容/负载',
    `is_read`         TINYINT      DEFAULT 0 COMMENT '0未读 1已读',
    `create_time`     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_target_read` (`target_user`, `is_read`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户通知（单用户）';


-- ======================================================
-- 公告表（平台公告/全局通知）
-- is_published: 0草稿 1已发布
-- publish_time: 可用于定时发布
-- ======================================================
CREATE TABLE `announcement`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `announcement_id` VARCHAR(64)  NOT NULL UNIQUE COMMENT '业务ID',
    `title`           VARCHAR(255) NOT NULL,
    `content`         TEXT         NOT NULL,
    `author_id`       VARCHAR(64)       DEFAULT NULL COMMENT '发布者（管理员）',
    `is_published`    TINYINT           DEFAULT 0 COMMENT '0草稿 1已发布',
    `publish_time`    TIMESTAMP    NULL DEFAULT NULL COMMENT '定时发布时间（若为空则立即生效）',
    `create_time`     TIMESTAMP         DEFAULT CURRENT_TIMESTAMP,
    `update_time`     TIMESTAMP         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='平台公告/全局通知';


-- ======================================================
-- 公告消息模板（可复用模板）
-- ======================================================
CREATE TABLE `announcement_template`
(
    `id`             BIGINT AUTO_INCREMENT PRIMARY KEY,
    `template_key`   VARCHAR(100) NOT NULL UNIQUE COMMENT '模板标识，如 new_feature_v1',
    `title_template` VARCHAR(255) DEFAULT NULL,
    `body_template`  TEXT         DEFAULT NULL,
    `create_time`    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='公告/通知模板';


-- ======================================================
-- 管理操作记录（例如下线指定用户）
-- action_type: kick_offline / ban_user / grant_admin / ...
-- ======================================================
CREATE TABLE `admin_action_log`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `admin_id`    VARCHAR(64)  NOT NULL COMMENT '执行操作的管理员 user_id',
    `action_type` VARCHAR(100) NOT NULL COMMENT '操作类型（kick_offline 等）',
    `target_user` VARCHAR(64) DEFAULT NULL COMMENT '操作目标用户（如下线）',
    `details`     JSON        DEFAULT NULL COMMENT '操作详情/参数',
    `create_time` TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='管理员操作审计日志（包含下线用户）';







