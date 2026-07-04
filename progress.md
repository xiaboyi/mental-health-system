# Progress Log: 心理健康咨询系统 修复

## Session 1 — 2026-06-11

### 已完成
- [x] 项目结构探索 ✅
- [x] 代码审查 — 阅读所有关键文件 ✅
- [x] 问题诊断 — 确认 3 个主要问题领域的具体 Bug ✅
- [x] 创建 task_plan.md 和 findings.md ✅
- [x] Phase 1: 角色菜单差异化 ✅
- [x] Phase 2: 上传头像 Bug 修复 ✅ (修复期间发现并修正了一个额外问题)
- [x] Phase 3: 答题结果展示 Bug 修复 ✅
- [x] Phase 4: 验证测试 ✅

### 改动文件
| 文件 | Phase |
|------|-------|
| `frontend/src/views/Layout.vue` | Phase 1 — 角色菜单差异化 |
| `frontend/src/views/Profile.vue` | Phase 2 — 头像上传修复 |
| `frontend/src/views/AssessmentDo.vue` | Phase 3 — 测评结果展示修复 |
| `backend/.../config/WebConfig.java` | Phase 2 — JWT 拦截器修复 |

### 额外发现
- `UserController.updateInfo` 在接收部分字段时可能清空其他字段，`saveProfile()` 发送完整表单是安全的做法
- `Appointment.vue` 已内置角色判断逻辑，COUNSELOR 可看到确认/拒绝按钮
