# 🧠 大学生心理健康咨询系统

> Spring Boot + Vue 3 企业级全栈项目 | 6大功能模块 | 20+ API接口 | 多角色权限管理

## ✨ 核心亮点

- 🏗️ **企业级架构**：Controller-Service-Mapper 三层分层，RESTful API 规范，统一 JSON 响应
- 👥 **多角色权限**：学生 / 心理咨询师 / 管理员三种角色，JWT 拦截器 + 动态路由权限控制
- 📊 **数据可视化**：ECharts 5 实现情绪趋势、预约分布、测评结果统计、咨询趋势等图表
- 💬 **在线咨询**：即时消息会话，支持消息已读/未读状态管理
- 📝 **心理测评**：SAS（焦虑）/ SDS（抑郁）/ 压力问卷在线答题，自动评分分析

## 🛠 技术栈

| 层级 | 技术 |
|------|------|
| **后端框架** | Spring Boot 2.7.18 |
| **ORM** | MyBatis-Plus 3.5（分页插件、自动填充） |
| **数据库** | MySQL 8.0 |
| **认证鉴权** | JWT（无状态）+ BCrypt 密码加密 + 自定义拦截器 |
| **前端框架** | Vue 3（Composition API）+ Vite |
| **UI 组件库** | Element Plus |
| **图表** | ECharts 5 |
| **HTTP** | Axios（请求/响应拦截器） |
| **其他** | AOP 系统日志、Validation 参数校验、全局异常处理、文件上传 |

## 📁 项目结构

```
mental-health-system/
├── backend/src/main/java/com/mentalhealth/
│   ├── config/           # 配置（CORS / MyBatis-Plus / AOP日志 / 全局异常）
│   ├── controller/       # REST API 控制器（10个）
│   ├── service/          # 业务接口 + 实现（9对）
│   ├── mapper/           # MyBatis Mapper（14个）
│   ├── entity/           # 数据库实体（14个）
│   ├── dto/              # 数据传输对象
│   ├── interceptor/      # JWT 拦截器
│   └── utils/            # JWT 工具类
├── frontend/src/
│   ├── views/            # 页面组件（20个）
│   ├── router/           # 路由配置 + 动态权限
│   ├── api/              # API 接口封装
│   └── utils/            # Axios 请求封装（token 自动注入）
└── sql/
    └── init.sql           # 数据库初始化脚本（10+ 张表）
```

## 🎯 功能模块

| 模块 | 功能 |
|------|------|
| **用户管理** | 注册/登录/JWT认证/个人信息修改/头像上传/密码修改/管理员用户管理 |
| **心理测评** | 量表管理（SAS/SDS/压力问卷）/在线答题/自动评分（NORMAL/MILD/MODERATE/SEVERE）/历史记录 |
| **咨询预约** | 咨询师列表/在线预约（时间+方式）/状态管理（确认/取消/完成） |
| **在线咨询** | 实时消息会话/消息已读未读/咨询记录/风险等级评估 |
| **知识库** | 文章发布（富文本）/分类浏览（情绪/压力/人际/成长）/评论互动/点赞/浏览量 |
| **数据看板** | ECharts图表/情绪打卡/情绪趋势/预约分布/测评结果统计 |

## 📊 API 接口概览

```
POST /api/auth/login          # 用户登录
POST /api/auth/register       # 用户注册
GET  /api/user/info           # 个人信息
PUT  /api/user/info           # 修改信息
PUT  /api/user/password       # 修改密码
GET  /api/assessment/scales   # 量表列表
GET  /api/assessment/questions/{id}  # 获取题目
POST /api/assessment/submit   # 提交测评
GET  /api/assessment/records  # 测评记录
POST /api/appointment         # 创建预约
GET  /api/appointment/my      # 我的预约
PUT  /api/appointment/{id}/status  # 修改预约状态
POST /api/consultation/session     # 创建会话
POST /api/consultation/message     # 发送消息
GET  /api/consultation/messages/{id}  # 获取消息
GET  /api/article/list        # 文章列表
GET  /api/article/{id}        # 文章详情
POST /api/article/{id}/like   # 点赞
GET  /api/statistics/dashboard  # 数据看板
POST /api/statistics/mood/checkin  # 情绪打卡
POST /api/file/upload         # 文件上传
```

## 🚀 快速启动

### 1. 数据库

```bash
mysql -u root -p < sql/init.sql
```

### 2. 后端

```bash
cd backend
# 修改 application.yml 中的数据库密码
mvn spring-boot:run
# 运行在 http://localhost:8080
```

### 3. 前端

```bash
cd frontend
npm install
npm run dev
# 访问 http://localhost:3000
```

### 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 咨询师 | counselor1 | 123456 |
| 学生 | 自行注册 | - |

---

*Made with Claude Code — AI-Assisted Development*
