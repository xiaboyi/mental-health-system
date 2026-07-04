<template>
  <el-container class="layout">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
      <div class="logo">
        <span v-if="!isCollapse">🎓 心理呵护</span>
        <span v-else>🎓</span>
      </div>
      <el-menu :default-active="activeMenu" router :collapse="isCollapse" background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
        <template v-for="item in menuItems" :key="item.path">
          <el-sub-menu v-if="item.children" :index="item.path">
            <template #title><el-icon><component :is="item.icon" /></el-icon><span>{{ item.title }}</span></template>
            <el-menu-item v-for="child in item.children" :key="child.path" :index="child.path">
              <el-icon><component :is="child.icon" /></el-icon><span>{{ child.title }}</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon><span>{{ item.title }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse"><Fold /></el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-badge :value="unreadCount" :hidden="unreadCount === 0">
            <el-button type="primary" text @click="$router.push('/chat')"><el-icon><Bell /></el-icon></el-button>
          </el-badge>
          <el-dropdown>
            <span class="user-info">{{ userInfo.realName || userInfo.username }} <el-icon><ArrowDown /></el-icon></span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/profile')">个人信息</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { chatApi } from '../api/index'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)
const unreadCount = ref(0)
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

const currentTitle = computed(() => route.meta.title || '')

const menusByRole = {
  STUDENT: [
    { path: '/dashboard', title: '首页看板', icon: 'HomeFilled' },
    { path: '/profile', title: '个人信息', icon: 'User' },
    { path: '/assessment', title: '心理测评', icon: 'DocumentChecked' },
    { path: '/counselor', title: '咨询师列表', icon: 'Service' },
    { path: '/appointment', title: '咨询预约', icon: 'Calendar' },
    { path: '/chat', title: '在线咨询', icon: 'ChatDotRound' },
    { path: '/articles', title: '心理健康知识', icon: 'Reading' },
    { path: '/mood', title: '情绪打卡', icon: 'Sunny' }
  ],
  COUNSELOR: [
    { path: '/dashboard', title: '咨询师看板', icon: 'HomeFilled' },
    { path: '/profile', title: '个人信息', icon: 'User' },
    { path: '/counselor/clients', title: '我的来访者', icon: 'UserFilled' },
    { path: '/appointment', title: '咨询预约管理', icon: 'Calendar' },
    { path: '/chat', title: '在线咨询', icon: 'ChatDotRound' },
    { path: '/admin/articles', title: '文章管理', icon: 'Edit' }
  ],
  ADMIN: [
    { path: '/dashboard', title: '数据看板', icon: 'HomeFilled' },
    { path: '/profile', title: '个人信息', icon: 'User' },
    { path: 'admin', title: '系统管理', icon: 'Setting', children: [
      { path: '/admin/users', title: '用户管理', icon: 'UserFilled' },
      { path: '/admin/counselors', title: '咨询师管理', icon: 'Service' },
      { path: '/admin/appointments', title: '预约管理', icon: 'Calendar' },
      { path: '/admin/articles', title: '文章管理', icon: 'Edit' },
      { path: '/admin/scales', title: '量表管理', icon: 'SetUp' },
      { path: '/admin/logs', title: '操作日志', icon: 'Document' }
    ] }
  ]
}

const menuItems = computed(() => {
  return menusByRole[userInfo.value.role] || menusByRole.STUDENT
})

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/login')
}

onMounted(async () => {
  try { const res = await chatApi.getUnreadCount(); unreadCount.value = res.data } catch(e) {}
})
</script>

<style scoped>
.layout { height: 100vh; }
.sidebar { background: #304156; overflow: hidden; transition: width .3s; }
.logo { height: 60px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 18px; font-weight: bold; border-bottom: 1px solid rgba(255,255,255,.1); }
.header { background: #fff; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; box-shadow: 0 1px 4px rgba(0,0,0,.08); }
.header-left { display: flex; align-items: center; gap: 16px; }
.collapse-btn { font-size: 20px; cursor: pointer; }
.header-right { display: flex; align-items: center; gap: 20px; }
.user-info { cursor: pointer; display: flex; align-items: center; gap: 4px; }
.main-content { padding: 20px; background: #f0f2f5; min-height: calc(100vh - 60px); }
</style>
