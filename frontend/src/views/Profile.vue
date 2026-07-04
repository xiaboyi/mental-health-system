<template>
  <el-card>
    <template #header><span>个人信息</span></template>
    <el-form :model="form" label-width="100px" style="max-width:500px">
      <el-form-item label="头像">
        <el-avatar :size="80" :src="form.avatar" />
        <el-upload :http-request="uploadAvatar" :show-file-list="false" style="display:inline-block;margin-left:16px">
          <el-button type="primary" size="small">更换头像</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
      <el-form-item label="真实姓名"><el-input v-model="form.realName" /></el-form-item>
      <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
      <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="年龄"><el-input-number v-model="form.age" :min="1" :max="120" /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveProfile">保存修改</el-button>
        <el-button @click="showPwdDialog = true">修改密码</el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="showPwdDialog" title="修改密码" width="400px">
      <el-form :model="pwdForm" label-width="100px">
        <el-form-item label="原密码"><el-input v-model="pwdForm.oldPassword" type="password" show-password /></el-form-item>
        <el-form-item label="新密码"><el-input v-model="pwdForm.newPassword" type="password" show-password /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPwdDialog = false">取消</el-button>
        <el-button type="primary" @click="updatePassword">确认</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi, fileApi } from '../api/index'

const form = reactive({ username: '', realName: '', phone: '', email: '', gender: 1, age: 20, avatar: '' })
const showPwdDialog = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '' })

onMounted(async () => {
  try { const res = await userApi.getInfo(); Object.assign(form, res.data) } catch (e) {}
})

const saveProfile = async () => {
  try { await userApi.updateInfo(form); ElMessage.success('保存成功') } catch (e) {}
}

const updatePassword = async () => {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) { ElMessage.warning('请填写完整'); return }
  try { await userApi.updatePassword(pwdForm); ElMessage.success('密码修改成功'); showPwdDialog.value = false } catch (e) {}
}

const onAvatarSuccess = (url) => {
  form.avatar = url
  ElMessage.success('头像上传成功')
  saveProfile()
}

const uploadAvatar = async (option) => {
  const formData = new FormData()
  formData.append('file', option.file)
  try {
    const res = await fileApi.upload(formData, 'AVATAR')
    if (res.data?.url) {
      onAvatarSuccess(res.data.url)
    }
  } catch (e) {
    ElMessage.error('头像上传失败，请重试')
  }
}
</script>
