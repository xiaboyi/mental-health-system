<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="title">🎓 大学生心理健康咨询系统</h1>
      <p class="subtitle">关注心理健康，呵护心灵成长</p>
      <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" size="large" style="width:100%">登 录</el-button>
        </el-form-item>
        <div class="footer-link">
          <span>还没有账号？</span><router-link to="/register">立即注册</router-link>
        </div>
        <div class="demo-tips">
          <p>演示账号：admin / admin123</p>
          <p>咨询师：counselor1 / 123456</p>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi, userApi } from '../api/index'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const res = await authApi.login(form)
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userInfo', JSON.stringify(res.data))
      ElMessage.success('登录成功')
      router.push('/')
    } catch (e) { /* error handled in interceptor */ }
    finally { loading.value = false }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  background: white; border-radius: 16px; padding: 50px 40px;
  width: 420px; box-shadow: 0 20px 60px rgba(0,0,0,.2);
}
.title { text-align: center; color: #333; font-size: 24px; margin-bottom: 8px; }
.subtitle { text-align: center; color: #999; margin-bottom: 30px; font-size: 14px; }
.login-form { margin-top: 10px; }
.footer-link { text-align: center; font-size: 14px; color: #999; }
.footer-link a { color: #667eea; margin-left: 4px; }
.demo-tips { margin-top: 16px; padding: 12px; background: #f5f7fa; border-radius: 8px; font-size: 12px; color: #999; text-align: center; }
.demo-tips p { margin: 2px 0; }
</style>
