# AniSphere Cloud

**AniSphere** 是一个动漫视频社交平台的后端工程，基于 Spring Boot 3 + Spring Cloud 构建的多模块微服务系统。

平台围绕「看番 + 社交」两条主线展开：用户可以投稿与观看视频、发送弹幕、评论互动，也可以关注好友、发布动态、私聊沟通；同时提供一套完整的内容审核与后台管理能力，保证站内内容合规可控。

整个工程由 9 个 Maven 模块组成：1 个网关、7 个业务服务、1 个公共基础模块。所有服务共享 `common` 模块提供的鉴权、限流、审计日志、链路追踪等横切能力，对外只暴露网关一个入口。

## 功能概览

- **用户体系** — 注册登录、JWT 鉴权、个人资料、地址管理、收藏夹、浏览历史、关注与黑名单
- **视频服务** — 视频投稿与分片上传、播放与播放历史、标签分类、首页轮播、播放统计、收益统计、举报处理
- **实时弹幕** — 基于 WebSocket 的弹幕收发与广播
- **评论系统** — 多级评论、点赞，数据存储在 MongoDB，配套评论后台管理
- **社交互动** — 关注、好友关系、用户动态（Post）、点赞收藏等社交行为
- **即时聊天** — WebSocket 私聊，会话列表与历史消息，配套聊天后台管理
- **消息中心** — 站内通知与系统公告，含管理端下发接口
- **内容审核** — 审核任务分派、审核组与审核员管理、审核记录留痕、审核员申请流程，并内置 Vaadin 审核工作台
- **系统治理** — 操作审计日志、登录日志、多维度 API 限流、Redis 缓存、TraceId 全链路追踪

## 系统架构

```
                          ┌──────────────┐
     客户端  ──────────▶   │   gateway    │  :9099
                          │  统一入口/CORS │
                          │  路由/负载均衡  │
                          └──────┬───────┘
                                 │ lb://  (LoadBalancer + OpenFeign)
   ┌──────────┬──────────┬───────┼────────┬──────────┬──────────┐
   ▼          ▼          ▼       ▼        ▼          ▼          ▼
user-service ani-video ani-comment ani-audit ani-message ani-social ani-chat
  :9090       :9091      :9092      :9093     :9094      :9095     :9096
   │            │           │          │         │          │         │
   └────────────┴───────────┴──────────┴─────────┴──────────┴─────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              ▼                  ▼                  ▼
          MySQL 8            MongoDB             Redis
        (ani_sphere)        (comment)         (缓存/限流)

                     所有服务依赖 common 模块
        (JWT / 限流 / 审计日志 / TraceId / 统一响应与异常处理)
```

服务注册采用 Spring Cloud 的 `SimpleDiscoveryClient` 静态注册（配置集中在 `common/src/main/resources/anisphere-microservice.yaml`），服务地址可通过环境变量覆盖，无需额外部署注册中心即可在本地跑通全链路。服务间调用走 OpenFeign，并统一接入 Resilience4j 的熔断、重试与超时限制。

## 模块说明

| 模块 | 端口 | 职责 | 数据存储 |
| --- | --- | --- | --- |
| `gateway` | 9099 | 统一入口，路由转发、CORS、响应式网关（WebFlux） | Redis |
| `user-service` | 9090 | 用户、管理员、文件、地址、收藏、浏览历史、关注、黑名单、登录与操作日志 | MySQL + Redis |
| `ani-video` | 9091 | 视频、分片上传、弹幕（WebSocket）、标签、轮播、统计、收益、举报 | MySQL |
| `ani-comment` | 9092 | 评论与评论后台管理 | MongoDB |
| `ani-audit` | 9093 | 审核任务、审核组、审核员、审核记录，含 Vaadin 审核工作台 | MySQL |
| `ani-message` | 9094 | 站内通知与系统公告（用户端 + 管理端） | MySQL |
| `ani-social` | 9095 | 关注、好友、用户动态、社交行为，含社交后台管理 | MySQL |
| `ani-chat` | 9096 | 会话与消息、WebSocket 私聊，含聊天后台管理 | MySQL |
| `common` | — | 公共基础库：统一响应、JWT、限流、审计日志、TraceId、异常处理 | — |

`service-manager` 是独立的服务管理辅助工具，不在父 POM 的模块列表中。

## 技术栈

### 核心框架

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Java | 21 (LTS) | 运行时，全模块统一 |
| Spring Boot | 3.4.5 | 应用框架，父 POM 统一管理 |
| Spring Cloud | 2024.0.3 | 微服务框架 |
| Maven | 3.9+ | 多模块构建 |

### 微服务组件

- **Spring Cloud Gateway** — 响应式 API 网关，按路径前缀路由到各业务服务
- **Spring Cloud LoadBalancer** — 客户端负载均衡，开启重试
- **OpenFeign** — 声明式服务间调用，开启请求/响应压缩
- **Resilience4j** — 熔断（滑动窗口 20、失败率阈值 50%）、重试（2 次）、超时（8s）
- **SimpleDiscoveryClient** — 静态服务发现，地址可由环境变量注入

### 数据与缓存

- **MySQL 8** — 主业务库 `ani_sphere`，通过 `mysql-connector-j` 接入
- **MongoDB** — 评论服务专用存储（Spring Data MongoDB）
- **Redis** — 缓存、限流计数、网关侧响应式访问（Lettuce）
- **MyBatis-Plus 3.5.5** — ORM，配合 MyBatis Spring Boot Starter 3.0.3
- **MyBatis-Plus Generator + Velocity** — 代码生成（user-service）

### 安全与鉴权

- **Spring Security** — 各服务安全基线配置
- **JJWT 0.11.5** — JWT 签发与校验，`common` 模块统一封装 `JwtUtil` 与过滤器
- **自定义注解** — `@Admin` 管理员权限校验、`@RateLimit` 限流、`@OperationLog` 操作留痕，均由 AOP 切面实现

### 可观测性

- **Spring Boot Actuator** — 健康检查（含 liveness/readiness 探针）、指标暴露
- **Micrometer + Prometheus** — 指标采集，按 `application` 标签区分服务
- **Micrometer Tracing (Brave)** — 分布式链路追踪，默认全采样
- **TraceId 透传** — `TraceIdFilter` 生成并跨服务传递 traceId，日志格式内置 `traceId/spanId`

### 实时通信与其他

- **Spring WebSocket** — 弹幕（ani-video）与私聊（ani-chat）
- **Vaadin 25.1.0** — ani-audit 审核工作台前端
- **SpringDoc OpenAPI 1.7.0** — 接口文档
- **Spring AI 1.0.1 (BOM)** — user-service 已引入版本管理，为 AI 能力预留
- **Lombok** — 样板代码简化
- **Spring AOP** — 限流、日志、权限切面的实现基础

## 快速开始

### 环境要求

- JDK 21
- Maven 3.9+
- MySQL 8（库名 `ani_sphere`）
- MongoDB（库名 `comment`）
- Redis 6+

### 构建

```bash
mvn clean install -DskipTests
```

### 本地一键启动

在当前目录执行：

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\start-all.ps1
```

也可以双击 `start-all.cmd`。

查看状态：

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\status-all.ps1 -Health
```

停止全部服务：

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\stop-all.ps1
```

详细说明见 [docs/local-one-click-start.md](docs/local-one-click-start.md)。

### 访问入口

启动后统一通过网关访问：`http://localhost:9099`

各服务的 Swagger 文档在自身端口下的 `/swagger-ui.html`。

### 配置覆盖

数据库、Redis、各服务地址均支持环境变量覆盖，例如：

```bash
DB_URL=jdbc:mysql://your-host:3306/ani_sphere
DB_USERNAME=root
DB_PASSWORD=******
MONGODB_URI=mongodb://your-host/comment
USER_SERVICE_URL=http://user-service:9090
FEIGN_READ_TIMEOUT=8000
TRACING_SAMPLING_PROBABILITY=0.1
```

## 系统优化方案

### ✅ 已实现功能（P0优先级）

**核心优化已上线**：

- ✅ **系统审计日志** - 完整的操作追溯和安全合规
  - 操作日志：自动记录所有关键操作（注册、登录、修改等）
  - 登录日志：记录登录状态、IP、地理位置
  - 管理端接口：支持分页查询和多维度筛选

- ✅ **API限流保护** - 防止恶意攻击和系统过载
  - IP级别限流：防止单IP恶意请求
  - 用户级别限流：防止单用户频繁操作
  - API级别限流：保护整体系统稳定性
  - 已应用：注册（5次/5分钟）、登录（5次/1分钟）、修改信息（10次/1分钟）

- ✅ **缓存优化** - 大幅提升查询性能
  - Redis缓存集成：Spring Cache注解支持
  - 用户信息缓存：响应时间从50ms降至2ms
  - 自动失效机制：更新时自动清除缓存

- ✅ **分布式链路追踪** - 快速定位问题
  - TraceId自动生成和传播
  - 跨服务调用追踪
  - 日志自动关联

**快速开始**：
```bash
# 1. 确保Redis运行
redis-server

# 2. 执行数据库脚本（如果还没执行）
mysql -u root -p ani_sphere --default-character-set=utf8mb4 < docs/optimization-features.sql

# 3. 启动服务并测试
bash docs/test-optimization.sh

# 4. 查看运行状态
mysql -u root -p ani_sphere < docs/check-optimization-status.sql
```

**详细文档**：
- 🚀 [快速启动指南](docs/QUICK_START.md) - 5分钟快速上手
- 📄 [实现总结](docs/OPTIMIZATION_SUMMARY.md) - 完整的实现报告
- ⚙️ [配置指南](docs/OPTIMIZATION_CONFIG.md) - 详细配置说明
- 📘 [实现细节](docs/OPTIMIZATION_IMPLEMENTATION.md) - 技术实现详解
- 💾 [数据库脚本](docs/optimization-features.sql) - 所有系统表创建脚本
- 🧪 [测试脚本](docs/test-optimization.sh) - 自动化功能测试
- 📊 [性能测试](docs/performance-test.sh) - 性能对比测试
- 🔍 [状态检查](docs/check-optimization-status.sql) - 运行状态和指标查询

### 📋 待实现功能

**P1 优先级**（重要但非紧急）：
- 消息队列可靠性（Outbox模式、重试机制）
- 动态配置管理（Nacos集成、热更新）

**P2 优先级**（锦上添花）：
- 内容推荐（协同过滤算法）
- 定时任务管理（XXL-Job集成）
- 数据字典（枚举统一管理）

**实际收益**（P0功能）：
- 查询性能：缓存命中后提升 25倍
- 安全防护：防止暴力破解和恶意注册
- 可观测性：完整的审计日志和链路追踪
- ⚡ 数据库压力：缓存命中率 >80%，QPS降低 80%

## 项目结构

```
anisphere-cloud/
├── pom.xml                  # 父 POM，统一版本与模块管理
├── common/                  # 公共基础模块（鉴权/限流/日志/追踪）
├── gateway/                 # API 网关
├── user-service/            # 用户服务
├── ani-video/               # 视频与弹幕服务
├── ani-comment/             # 评论服务（MongoDB）
├── ani-audit/               # 审核服务（含 Vaadin 工作台）
├── ani-message/             # 通知与公告服务
├── ani-social/              # 社交服务
├── ani-chat/                # 聊天服务
├── service-manager/         # 服务管理辅助工具
├── scripts/                 # 一键启停脚本
├── docs/                    # 设计与优化文档
└── uploads/                 # 本地上传文件目录
```

## 许可

见 [LICENSE](LICENSE)。
