import request from '../utils/request'

export const authApi = {
  login: (data) => request.post('/auth/login', data),
  register: (data) => request.post('/auth/register', data)
}

export const userApi = {
  getInfo: () => request.get('/user/info'),
  updateInfo: (data) => request.put('/user/info', data),
  updatePassword: (data) => request.put('/user/password', data),
  list: (params) => request.get('/user/list', { params }),
  updateStatus: (id, status) => request.put(`/user/status/${id}`, { status })
}

export const assessmentApi = {
  getScales: () => request.get('/assessment/scales'),
  getQuestions: (scaleId) => request.get(`/assessment/questions/${scaleId}`),
  submit: (data) => request.post('/assessment/submit', data),
  getRecords: () => request.get('/assessment/records')
}

export const counselorApi = {
  list: () => request.get('/counselor/list'),
  detail: (id) => request.get(`/counselor/${id}`),
  create: (data) => request.post('/counselor', data),
  update: (id, data) => request.put(`/counselor/${id}`, data),
  getClients: () => request.get('/counselor/clients')
}

export const appointmentApi = {
  create: (data) => request.post('/appointment', data),
  updateStatus: (id, status) => request.put(`/appointment/${id}/status`, { status }),
  getMy: () => request.get('/appointment/my'),
  getAll: (params) => request.get('/appointment/all', { params }),
  check: (params) => request.get('/appointment/check', { params })
}

export const chatApi = {
  getOrCreateSession: (data) => request.post('/consultation/session', data),
  getSessions: () => request.get('/consultation/sessions'),
  sendMessage: (data) => request.post('/consultation/message', data),
  getMessages: (sessionId) => request.get(`/consultation/messages/${sessionId}`),
  markAsRead: (sessionId) => request.put(`/consultation/read/${sessionId}`),
  getUnreadCount: () => request.get('/consultation/unread'),
  saveRecord: (data) => request.post('/consultation/record', data),
  updateRecord: (id, data) => request.put(`/consultation/record/${id}`, data),
  getRecords: () => request.get('/consultation/records')
}

export const articleApi = {
  list: (params) => request.get('/article/list', { params }),
  detail: (id) => request.get(`/article/${id}`),
  create: (data) => request.post('/article', data),
  update: (id, data) => request.put(`/article/${id}`, data),
  delete: (id) => request.delete(`/article/${id}`),
  like: (id) => request.post(`/article/${id}/like`),
  comments: (id) => request.get(`/article/${id}/comments`),
  addComment: (data) => request.post('/article/comment', data),
  manageList: (params) => request.get('/article/manage', { params })
}

export const statsApi = {
  dashboard: () => request.get('/statistics/dashboard'),
  counselorDashboard: () => request.get('/statistics/counselor-dashboard'),
  moodHistory: () => request.get('/statistics/mood/history'),
  moodCheckin: (data) => request.post('/statistics/mood/checkin', data),
  todayMood: () => request.get('/statistics/mood/today')
}

export const adminApi = {
  dashboard: () => request.get('/admin/dashboard'),
  logs: (params) => request.get('/admin/logs', { params }),
  userTrend: () => request.get('/admin/user-trend'),
  roleDistribution: () => request.get('/admin/role-distribution')
}

export const fileApi = {
  upload: (formData, businessType) => request.post(`/file/upload?businessType=${businessType}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
