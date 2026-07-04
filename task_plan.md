# Task Plan: 心理健康咨询系统 — 角色菜单差异化 & Bug修复

**Created:** 2026-06-11
**Status:** complete

## Goal

修复三类问题：
1. **角色菜单栏差异化** — 管理员(ADMIN)、咨询师(COUNSELOR)、用户(STUDENT) 的侧边栏菜单需要根据不同角色显示不同内容
2. **上传头像 Bug** — 头像上传功能存在问题需要修复
3. **答题完成后 Bug** — 心理测评提交后结果展示有问题

---

## Phase 1: 角色菜单栏差异化 (Layout.vue + Router)

**Status:** complete ✅

### 改动文件
- `frontend/src/views/Layout.vue`

### 改动内容
将原来的 `allMenus` 硬编码单一阵列替换为 `menusByRole` 对象，为三种角色提供完全独立的菜单:

- **STUDENT**: 首页看板、个人信息、心理测评、咨询师列表、咨询预约、在线咨询、心理健康知识、情绪打卡 (8项)
- **COUNSELOR**: 首页看板、个人信息、咨询预约管理、在线咨询、文章管理、情绪打卡 (6项)
- **ADMIN**: 首页看板、个人信息、系统管理(用户管理/量表管理/文章管理) (3项 + 子菜单)

简化了 `menuItems` computed 逻辑，删除了不再需要的 `getRouteMeta` 函数。

---

## Phase 2: 上传头像 Bug 修复

**Status:** complete ✅

### 改动文件
- `frontend/src/views/Profile.vue`
- `backend/src/main/java/com/mentalhealth/config/WebConfig.java`

### 修复内容

1. **businessType 缺失**: `el-upload` action 从 `/api/file/upload` 改为 `/api/file/upload?businessType=AVATAR`
2. **无错误提示**: 添加 `onError="onAvatarError"` 回调，失败时显示 "头像上传失败，请重试"
3. **JWT 拦截器排除文件上传**: 从 `WebConfig.java` 中移除 `/api/file/**` 的排除规则，使 JWT 拦截器正常运行，`FileController` 中 `userId` 不再为 null
4. **头像保存优化**: 上传成功后保留 `saveProfile()` 调用（发送完整表单，避免其他字段被清空），并添加成功提示

---

## Phase 3: 答题完成后结果展示 Bug 修复

**Status:** complete ✅

### 改动文件
- `frontend/src/views/AssessmentDo.vue`

### 修复内容

**Bug 根因**: `resultIcon`、`resultTitle`、`resultDetail` 被定义为箭头函数，模板中 `:icon="resultIcon"` 传递的是函数引用而非调用结果。Element Plus 的 `el-result` 组件收到 Function 对象，图标/标题/副标题无法正常显示。

**修复**: 将三个箭头函数改为 `computed()` 属性，Vue 3 模板自动解包 computed，组件正确收到字符串值。

---

## Phase 4: 验证测试

**Status:** complete ✅

所有改动文件已复查，逻辑正确。注意 `Appointment.vue` 已内置角色判断（第22行和第34行），COUNSELOR 可看到确认/拒绝按钮，STUDENT 可看到新建预约表单。

### 改动文件汇总
| 文件 | 改动 |
|------|------|
| `frontend/src/views/Layout.vue` | 角色菜单差异化 |
| `frontend/src/views/Profile.vue` | 头像上传修复 |
| `frontend/src/views/AssessmentDo.vue` | 测评结果展示修复 |
| `backend/.../config/WebConfig.java` | JWT 拦截器修复 |

---

## Errors Encountered

| Error | Attempt | Resolution |
|-------|---------|------------|
| onAvatarSuccess 仅发送 avatar 字段会导致后端 updateInfo 清空其他字段 | 1 | 改回调用 saveProfile() 发送完整表单 |
