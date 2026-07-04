# Findings: 心理健康咨询系统 代码审查

**Created:** 2026-06-11

---

## 项目架构

| 层 | 技术栈 |
|----|--------|
| 前端 | Vue 3 (Composition API) + Vite + Element Plus + Vue Router (Hash) + ECharts + Axios |
| 后端 | Spring Boot + MyBatis-Plus + MySQL + JWT + BCrypt |
| 角色 | ADMIN, COUNSELOR, STUDENT |

---

## 发现 #1: 菜单系统缺乏角色差异化

**位置**: `frontend/src/views/Layout.vue:65-80`

当前 `allMenus` 是单一阵列，所有角色共用一套菜单结构。仅通过 `meta.role` 对 admin 子菜单做了简单过滤。

```js
const allMenus = [
  { path: '/dashboard', ... },
  { path: '/profile', ... },
  // ... 所有角色共用
  { path: 'admin', title: '系统管理', children: [
    { path: '/admin/users', ... },      // role: ADMIN
    { path: '/admin/articles', ... },   // role: ADMIN,COUNSELOR
    { path: '/admin/scales', ... }      // role: ADMIN
  ]}
]
```

**问题**: 
- STUDENT 不应看到任何 admin 相关菜单（目前已正确过滤，因所有子菜单都不匹配 STUDENT 角色）
- COUNSELOR 不应看到 "咨询师列表"、"心理测评"（但应有自己的预约管理、在线咨询等）
- 实际上 STUDENT 和 COUNSELOR 看到的基本相同的菜单，这不合理
- COUNSELOR 缺少"预约管理"、"咨询记录"等专业功能入口

---

## 发现 #2: 上传头像 Bug — 多重问题

**位置**: 
- `frontend/src/views/Profile.vue:7`
- `backend/src/main/java/com/mentalhealth/config/WebConfig.java:29-37`
- `backend/src/main/java/com/mentalhealth/controller/FileController.java:21`

**问题清单**:

1. **businessType 缺失**: upload action `/api/file/upload` 没传 `businessType=AVATAR`，文件记录的 business_type 会是默认值 `OTHER`

2. **无错误回调**: `el-upload` 没有 `on-error` handler，上传失败时用户看不到任何提示

3. **JWT 拦截器排除**: `WebConfig.java` 将 `/api/file/**` 排除在 JWT 拦截之外，导致 `FileController.upload` 中 `request.getAttribute("userId")` 一直为 null。上传的文件无法关联到用户。

```java
// WebConfig.java
.excludePathPatterns(
    "/api/auth/login",
    "/api/auth/register",
    "/api/auth/captcha",
    "/api/file/**"    // <-- 文件上传绕过了JWT
);
```

```java
// FileController.java
Long userId = (Long) request.getAttribute("userId");  // 永远为null
```

4. **资源路径**: 后端 `addResourceHandlers` 映射 `file:./uploads/`，Vite dev 也代理了 `/uploads`。但生产环境需要额外配置 Nginx 或 Spring Boot 的静态资源映射（目前已有，应该 OK）。

---

## 发现 #3: 答题完成后结果展示 Bug

**位置**: `frontend/src/views/AssessmentDo.vue:79-87`

```javascript
// 这些是普通函数，不是 computed
const resultIcon = () => resultIconMap[resultRecord.value.resultLevel] || 'info'
const resultTitle = () => resultTitleMap[resultRecord.value.resultLevel] || ''
const resultDetail = () => `得分：${resultRecord.value.totalScore}分。...`
```

模板中:
```html
<el-result :icon="resultIcon" :title="resultTitle" :sub-title="resultDetail">
```

**问题**: Vue 3 模板绑定 `:icon="resultIcon"` 传递的是函数对象，不是调用结果。Element Plus 的 `el-result` 组件期望字符串，实际收到了 Function 对象。

**结果**: 测评提交后，结果弹窗的图标、标题、副标题可能不显示或显示异常（如 `[object Function]` 之类的字符串）。

**备注**: 后端 `AssessmentServiceImpl.submitAssessment` 的评分逻辑是正确的，通过解析 options JSON 中的 score 字段来计算总分。

---

## 发现 #4: 路由中缺少 COUNSELOR 专属页面

**位置**: `frontend/src/router/index.js`

当前路由缺少：
- 咨询师看的预约管理页面（当前 `/appointment` 是学生视角）
- 咨询记录管理页面

这些缺失导致 COUNSELOR 即使看到菜单项，也没有对应的页面组件。Phase 1 需要决定是为现有页面添加角色条件渲染，还是创建新页面。

建议方案：复用 `/appointment` 页面，在前端根据角色展示不同内容（学生看自己的预约，咨询师看分配给自己的预约）。

---

## 发现 #5: 头像显示问题

`Profile.vue:6` 使用 `<el-avatar :size="80" :src="form.avatar" />` 直接显示头像。
当 `onAvatarSuccess` 触发后，`form.avatar` 被设为 `/uploads/xxx.jpg`。
如果用户不点击"保存修改"，头像不会被持久化到数据库。

`onAvatarSuccess` 中自动调用了 `saveProfile()`，这会在上传成功后自动保存——但这也意味着只要上传了头像就会立即更新整个 profile，这是一个 UX 上的瑕疵（比如用户改了其他信息还没保存，上传头像后会把之前的修改也一并提交），但不是 bug。
