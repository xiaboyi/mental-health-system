import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '首页看板', icon: 'HomeFilled' } },
      { path: 'profile', name: 'Profile', component: () => import('../views/Profile.vue'), meta: { title: '个人信息', icon: 'User' } },
      { path: 'assessment', name: 'Assessment', component: () => import('../views/Assessment.vue'), meta: { title: '心理测评', icon: 'DocumentChecked' } },
      { path: 'assessment/do/:scaleId', name: 'AssessmentDo', component: () => import('../views/AssessmentDo.vue'), meta: { title: '在线测评', hidden: true } },
      { path: 'counselor', name: 'CounselorList', component: () => import('../views/CounselorList.vue'), meta: { title: '咨询师列表', icon: 'Service' } },
      { path: 'appointment', name: 'Appointment', component: () => import('../views/Appointment.vue'), meta: { title: '咨询预约', icon: 'Calendar' } },
      { path: 'chat', name: 'Chat', component: () => import('../views/Chat.vue'), meta: { title: '在线咨询', icon: 'ChatDotRound' } },
      { path: 'articles', name: 'Articles', component: () => import('../views/Articles.vue'), meta: { title: '心理健康知识', icon: 'Reading' } },
      { path: 'article/:id', name: 'ArticleDetail', component: () => import('../views/ArticleDetail.vue'), meta: { title: '文章详情', hidden: true } },
      { path: 'mood', name: 'Mood', component: () => import('../views/Mood.vue'), meta: { title: '情绪打卡', icon: 'Sunny' } },
      // Admin pages
      { path: 'admin/users', name: 'AdminUsers', component: () => import('../views/AdminUsers.vue'), meta: { title: '用户管理', icon: 'UserFilled', role: 'ADMIN' } },
      { path: 'admin/appointments', name: 'AdminAppointments', component: () => import('../views/AdminAppointments.vue'), meta: { title: '预约管理', icon: 'Calendar', role: 'ADMIN' } },
      { path: 'admin/counselors', name: 'AdminCounselors', component: () => import('../views/AdminCounselors.vue'), meta: { title: '咨询师管理', icon: 'Service', role: 'ADMIN' } },
      { path: 'admin/articles', name: 'AdminArticles', component: () => import('../views/AdminArticles.vue'), meta: { title: '文章管理', icon: 'Edit', role: 'ADMIN,COUNSELOR' } },
      { path: 'admin/scales', name: 'AdminScales', component: () => import('../views/AdminScales.vue'), meta: { title: '量表管理', icon: 'SetUp', role: 'ADMIN' } },
      { path: 'admin/logs', name: 'AdminLogs', component: () => import('../views/AdminLogs.vue'), meta: { title: '操作日志', icon: 'Document', role: 'ADMIN' } },
      // Counselor pages
      { path: 'counselor/clients', name: 'CounselorClients', component: () => import('../views/CounselorClients.vue'), meta: { title: '我的来访者', icon: 'UserFilled', role: 'COUNSELOR' } },
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 心理健康咨询系统` : '大学生心理健康咨询系统'
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && to.path !== '/register' && !token) {
    next('/login')
  } else {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (to.meta.role && to.meta.role.indexOf(userInfo.role) === -1) {
      next('/dashboard')
    } else {
      next()
    }
  }
})

export default router
