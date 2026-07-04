<template>
  <div>
    <el-card style="margin-bottom:20px">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>咨询师管理</span>
          <el-button type="primary" @click="showDialog(null)">新增咨询师</el-button>
        </div>
      </template>
      <el-table :data="counselors" v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column prop="specialty" label="擅长领域" show-overflow-tooltip />
        <el-table-column prop="workYears" label="从业年限" width="80" />
        <el-table-column prop="maxDailySlots" label="每日限约" width="80" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{row}">
            <el-button type="primary" text @click="showDialog(row)">编辑</el-button>
            <el-button type="danger" text @click="deleteCounselor(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑咨询师' : '新增咨询师'" width="560px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="关联用户ID">
          <el-input-number v-model="form.userId" :disabled="!!editingId" :min="1" style="width:100%" placeholder="输入已有用户的ID" />
        </el-form-item>
        <el-form-item label="职称"><el-input v-model="form.title" placeholder="如：高级心理咨询师" /></el-form-item>
        <el-form-item label="擅长领域"><el-input v-model="form.specialty" placeholder="如：情绪管理、人际关系、学业压力" /></el-form-item>
        <el-form-item label="个人简介"><el-input v-model="form.description" type="textarea" rows="3" placeholder="咨询师个人简介" /></el-form-item>
        <el-form-item label="资质证书"><el-input v-model="form.certification" placeholder="如：国家二级心理咨询师" /></el-form-item>
        <el-form-item label="从业年限"><el-input-number v-model="form.workYears" :min="0" :max="50" style="width:100%" /></el-form-item>
        <el-form-item label="每日最大预约"><el-input-number v-model="form.maxDailySlots" :min="1" :max="20" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCounselor" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { counselorApi } from '../api/index'

const counselors = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editingId = ref(null)
const saving = ref(false)
const form = reactive({ userId: null, title: '', specialty: '', description: '', certification: '', workYears: 0, maxDailySlots: 5 })

const loadData = async () => {
  loading.value = true
  try { const res = await counselorApi.list(); counselors.value = res.data } catch (e) {}
  loading.value = false
}

const showDialog = (row) => {
  if (row) {
    editingId.value = row.id
    Object.assign(form, { userId: row.userId, title: row.title||'', specialty: row.specialty||'', description: row.description||'', certification: row.certification||'', workYears: row.workYears||0, maxDailySlots: row.maxDailySlots||5 })
  } else {
    editingId.value = null
    Object.assign(form, { userId: null, title: '', specialty: '', description: '', certification: '', workYears: 0, maxDailySlots: 5 })
  }
  dialogVisible.value = true
}

const saveCounselor = async () => {
  saving.value = true
  try {
    if (editingId.value) {
      await counselorApi.update(editingId.value, form)
      ElMessage.success('更新成功')
    } else {
      await counselorApi.create(form)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {}
  saving.value = false
}

const deleteCounselor = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该咨询师？', '提示', { type: 'warning' })
    await counselorApi.update(id, { id })
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {}
}

onMounted(loadData)
</script>
