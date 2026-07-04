# 大学生心理健康咨询系统

> 基于 Spring Boot + Vue 3 的前后端分离项目

## 📋 项目简介

本项目是一个面向大学生的心理健康咨询系统，提供心理测评、在线咨询预约、心理健康知识科普、情绪打卡等功能，帮助大学生关注和维护自身心理健康。

## 🚀 技术栈

| 层级 | 技术 | 说明 |
|------|------|------|
| 后端框架 | Spring Boot 2.7.18 | Java Web 框架 |
| ORM | MyBatis-Plus 3.5 | 持久层框架 |
| 数据库 | MySQL 8.0 | 关系型数据库 |
| 认证鉴权 | JWT + BCrypt | 无状态认证 + 密码加密 |
| 接口规范 | RESTful API | 统一 JSON 响应 |
| 前端框架 | Vue 3 + Vite | 渐进式框架 |
| UI 组件库 | Element Plus | 企业级 UI 组件 |
| 图表 | ECharts 5 | 数据可视化 |
| HTTP 客户端 | Axios | 前后端通信 |

## 📁 项目结构

```
mental-health-system/
├── backend/                     # 后端 Spring Boot 项目
│   ├── src/main/java/com/mentalhealth/
│   │   ├── config/              # 配置类 (CORS, MyBatis-Plus, AOP日志)
│   │   ├── controller/          # REST API 控制器
│   │   ├── service/             # 业务接口
│   │   ├── service/impl/        # 业务实现
│   │   ├── mapper/              # MyBatis Mapper
│   │   ├── entity/              # 数据库实体
│   │   ├── dto/                 # 数据传输对象
│   │   ├── utils/               # 工具类 (JWT)
│   │   └── interceptor/         # JWT拦截器
│   └── src/main/resources/
│       └── application.yml      # 应用配置
├── frontend/                    # 前端 Vue 3 项目
│   └── src/
│       ├── views/               # 页面组件 (15个页面)
│       ├── router/              # 路由配置 (动态路由权限)
│       ├── api/                 # API 接口封装
│       ├── utils/               # Axios 请求封装
│       └── components/          # 公共组件
├── sql/
│   └── init.sql                 # 数据库初始化脚本 (含示例数据)
└── docs/
    └── 个人项目报告模板.docx       # 个人报告模板
```

## 🔧 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+
- npm 8+

## ⚡ 快速启动

### 1. 初始化数据库

```bash
# 执行 SQL 脚本创建数据库和表
mysql -u root -p < sql/init.sql
```

### 2. 启动后端

```bash
cd backend
mvn clean package -DskipTests
mvn spring-boot:run
# 或直接运行 MentalHealthApplication.java
```

后端运行在 http://localhost:8080

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 http://localhost:3000

## 👥 默认账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 系统管理员 |
| 咨询师 | counselor1 | 123456 | 高级心理咨询师 |
| 咨询师 | counselor2 | 123456 | 中级心理咨询师 |

> 学生账号可通过注册页面自行注册

## 🎯 功能模块

### 模块1：用户管理 (成员1)
- 用户注册/登录 (BCrypt 加密 + JWT Token)
- 个人信息修改 (含头像上传)
- 密码修改
- 管理员：用户列表、状态管理、角色管理

### 模块2：心理测评 (成员2)
- 心理量表列表 (SAS/SDS/压力问卷)
- 在线答题评测
- 测评结果分析 (NORMAL/MILD/MODERATE/SEVERE)
- 测评历史记录查看

### 模块3：咨询预约 (成员3)
- 咨询师列表查看
- 在线预约 (选择时间、方式)
- 预约状态管理 (确认/取消/完成)
- 咨询师端：预约处理

### 模块4：在线咨询 (成员4)
- 实时消息会话
- 消息已读/未读
- 咨询记录管理
- 风险等级评估

### 模块5：知识库 (成员5)
- 心理健康文章发布 (支持富文本)
- 文章分类浏览 (情绪管理/压力应对/人际关系/自我成长)
- 评论互动 (支持回复)
- 点赞/浏览量统计

### 模块6：数据看板 (成员6)
- ECharts 数据可视化大屏
- 情绪打卡 (每日心情记录)
- 情绪趋势图表
- 预约/测评数据统计

## ✨ 项目亮点 (加分项)

- ✅ **BCrypt 密码加密** (+5分) - 用户密码全程加密存储
- ✅ **JWT 无状态认证** (+10分) - 自定义注解拦截器实现权限校验
- ✅ **ECharts 数据图表** - 看板页面4个图表 (情绪趋势/预约分布/测评结果/预约趋势)
- ✅ **文件上传** - 头像上传功能
- ✅ **RESTful 接口规范** - 统一 JSON 返回格式
- ✅ **AOP 系统日志** - 自动记录操作日志
- ✅ **参数校验** - Validation 注解进行数据校验
- ✅ **分页查询** - MyBatis-Plus 分页插件
- ✅ **交叉异常处理** - GlobalExceptionHandler 全局异常处理
- ✅ **动态路由权限** - 前后端角色权限控制

## 📊 API 接口概览

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 认证 | POST /api/auth/login | 用户登录 |
| 认证 | POST /api/auth/register | 用户注册 |
| 用户 | GET /api/user/info | 获取个人信息 |
| 用户 | PUT /api/user/info | 修改个人信息 |
| 用户 | PUT /api/user/password | 修改密码 |
| 测评 | GET /api/assessment/scales | 量表列表 |
| 测评 | GET /api/assessment/questions/{id} | 获取题目 |
| 测评 | POST /api/assessment/submit | 提交测评 |
| 测评 | GET /api/assessment/records | 测评记录 |
| 预约 | POST /api/appointment | 创建预约 |
| 预约 | GET /api/appointment/my | 我的预约 |
| 预约 | PUT /api/appointment/{id}/status | 修改预约状态 |
| 咨询 | POST /api/consultation/session | 创建/获取会话 |
| 咨询 | POST /api/consultation/message | 发送消息 |
| 咨询 | GET /api/consultation/messages/{id} | 获取消息 |
| 文章 | GET /api/article/list | 文章列表 |
| 文章 | GET /api/article/{id} | 文章详情 |
| 文章 | POST /api/article | 发布文章 |
| 文章 | POST /api/article/{id}/like | 点赞 |
| 统计 | GET /api/statistics/dashboard | 数据看板 |
| 情绪 | POST /api/statistics/mood/checkin | 情绪打卡 |
| 情绪 | GET /api/statistics/mood/history | 情绪历史 |
| 文件 | POST /api/file/upload | 文件上传 |

## 📝 小组分工建议

| 成员 | 负责模块 | 核心功能 |
|------|----------|----------|
| 成员1 | 用户管理 | 注册/登录/JWT/个人信息/管理员用户管理 |
| 成员2 | 心理测评 | 量表管理/在线答题/结果分析/测评记录 |
| 成员3 | 咨询预约 | 咨询师列表/预约创建/状态管理 |
| 成员4 | 在线咨询 | 即时消息/会话管理/咨询记录 |
| 成员5 | 知识库 | 文章发布管理/评论系统/心理健康科普 |
| 成员6 | 数据看板 | ECharts图表/情绪打卡/数据统计 |

## 📄 文档说明

项目已包含完整代码和 SQL 初始化脚本，每个模块均遵循 Controller-Service-Mapper 分层架构，
前端使用 Vue 3 Composition API 编写，所有 API 接口遵循 RESTful 规范。
