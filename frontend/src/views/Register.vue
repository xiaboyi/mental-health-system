<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="title">用户注册</h1>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd">
          <el-input v-model="form.confirmPwd" type="password" placeholder="请确认密码" show-password />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width:100%">注 册</el-button>
        </el-form-item>
        <div class="footer-link"><span>已有账号？</span><router-link to="/login">去登录</router-link></div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '../api/index'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '', confirmPwd: '', realName: '', phone: '', email: '', gender: 1 })
const rules = {
  username: [{ required: true, message: '请输入用户名' }],
  password: [{ required: true, message: '请输入密码', min: 6 }],
  confirmPwd: [{ required: true, message: '请确认密码' }, {
    validator: (rule, value, callback) => value === form.password ? callback() : callback(new Error('两次密码不一致'))
  }],
  realName: [{ required: true, message: '请输入姓名' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确' }]
}

const handleRegister = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      await authApi.register(form)
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    } catch (e) {}
    finally { loading.value = false }
  })
}
</script>

<style scoped>
.login-container { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.login-card { background: white; border-radius: 16px; padding: 40px; width: 480px; box-shadow: 0 20px 60px rgba(0,0,0,.2); }
.title { text-align: center; color: #333; font-size: 24px; margin-bottom: 24px; }
.footer-link { text-align: center; font-size: 14px; color: #999; }
.footer-link a { color: #667eea; margin-left: 4px; }
</style>
