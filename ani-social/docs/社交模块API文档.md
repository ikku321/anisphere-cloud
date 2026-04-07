# 社交模块 API 接口文档

本项目为 AniSphere 微服务系统中的社交模块（ani-social），主要负责用户之间的人际关系（好友、关注）、动态发布及相关社交行为。

## 1. 数据库表结构

### 1.1 好友关系表 (`friend_relation`)
用于存储双向好友关系。
| 字段名 | 类型 | 说明 |
| :--- | :--- | :--- |
| id | BIGINT | 主键 ID (自增) |
| user_id | VARCHAR(64) | 发起/拥有者 user_id |
| friend_id | VARCHAR(64) | 好友 user_id |
| status | TINYINT | 状态：0=申请中, 1=已成为好友, 2=拒绝/拉黑 |
| remark | VARCHAR(255) | 备注名（owner 对 friend 的备注） |
| create_time | TIMESTAMP | 创建时间 |

### 1.2 好友分组表 (`friend_group`)
用户自定义的好友分组。
| 字段名 | 类型 | 说明 |
| :--- | :--- | :--- |
| id | BIGINT | 主键 ID (自增) |
| user_id | VARCHAR(64) | 拥有者 user_id |
| name | VARCHAR(100) | 分组名 |
| create_time | TIMESTAMP | 创建时间 |

### 1.3 关注表 (`follow`)
用于存储单向关注关系。
| 字段名 | 类型 | 说明 |
| :--- | :--- | :--- |
| id | BIGINT | 主键 ID (自增) |
| user_id | VARCHAR(64) | 发起关注的用户 |
| target_user | VARCHAR(64) | 被关注用户 |
| create_time | TIMESTAMP | 创建时间 |

### 1.4 用户动态表 (`user_post`)
用户发布的说说/动态。
| 字段名 | 类型 | 说明 |
| :--- | :--- | :--- |
| id | BIGINT | 主键 ID (自增) |
| post_id | VARCHAR(64) | 业务唯一 ID |
| user_id | VARCHAR(64) | 发表者 ID |
| content | TEXT | 动态文本 |
| media | JSON | 媒体附件列表 (图片/视频) |
| like_count | INT | 点赞数 |
| comment_count | INT | 评论数 |
| status | TINYINT | 状态：1=正常, 0=删除 |
| create_time | TIMESTAMP | 创建时间 |

---

## 2. API 接口定义

### 2.1 好友管理 (`/social/friend`)

#### 2.1.1 发送好友申请
- **接口地址**: `POST /social/friend/request`
- **请求参数**:
    - `friendId` (String): 目标用户 ID
- **返回数据**: `Result<Void>`

#### 2.1.2 处理好友申请
- **接口地址**: `POST /social/friend/process`
- **请求参数**:
    - `friendId` (String): 申请人 ID
    - `status` (Integer): 1=接受, 2=拒绝/拉黑
- **返回数据**: `Result<Void>`

#### 2.1.3 获取好友列表
- **接口地址**: `GET /social/friend/list`
- **返回数据**: `Result<List<FriendRelation>>`

#### 2.1.4 修改好友备注
- **接口地址**: `POST /social/friend/remark`
- **请求参数**:
    - `friendId` (String): 好友 ID
    - `remark` (String): 备注名
- **返回数据**: `Result<Void>`

#### 2.1.5 删除好友
- **接口地址**: `DELETE /social/friend/delete`
- **请求参数**:
    - `friendId` (String): 好友 ID
- **返回数据**: `Result<Void>`

#### 2.1.6 创建好友分组
- **接口地址**: `POST /social/friend/group/create`
- **请求参数**:
    - `groupName` (String): 分组名称
- **返回数据**: `Result<FriendGroup>`

#### 2.1.7 将好友添加到分组
- **接口地址**: `POST /social/friend/group/add-friend`
- **请求参数**:
    - `groupId` (Long): 分组 ID
    - `friendId` (String): 好友 ID
- **返回数据**: `Result<Void>`

---

### 2.2 关注管理 (`/social/follow`)

#### 2.2.1 关注用户
- **接口地址**: `POST /social/follow/add`
- **请求参数**:
    - `targetUser` (String): 目标用户 ID
- **返回数据**: `Result<Void>`

#### 2.2.2 取消关注
- **接口地址**: `DELETE /social/follow/remove`
- **请求参数**:
    - `targetUser` (String): 目标用户 ID
- **返回数据**: `Result<Void>`

#### 2.2.3 获取关注列表
- **接口地址**: `GET /social/follow/following`
- **返回数据**: `Result<List<Follow>>`

#### 2.2.4 获取粉丝列表
- **接口地址**: `GET /social/follow/followers`
- **返回数据**: `Result<List<Follow>>`

---

### 2.3 动态管理 (`/social/post`)

#### 2.3.1 发布动态
- **接口地址**: `POST /social/post/create`
- **请求体 (JSON)**:
    ```json
    {
      "content": "动态内容",
      "media": [
        {"type": "image", "url": "http://..."}
      ]
    }
    ```
- **返回数据**: `Result<UserPost>`

#### 2.3.2 分页获取全站动态
- **接口地址**: `GET /social/post/global-list`
- **查询参数**:
    - `page` (int): 页码 (默认1)
    - `size` (int): 每页大小 (默认10)
- **返回数据**: `Result<Page<UserPost>>`

---

### 2.4 社交行为 (`/social/action`)

#### 2.4.1 举报用户
- **接口地址**: `POST /social/action/report`
- **请求参数**:
    - `targetUser` (String): 被举报人 ID
    - `reason` (String): 举报原因
- **返回数据**: `Result<Void>`

#### 2.4.2 清除聊天记录记录
- **接口地址**: `POST /social/action/chat/clear`
- **请求参数**:
    - `conversationId` (String): 会话 ID
    - `scope` (String): self/all
- **返回数据**: `Result<Void>`
