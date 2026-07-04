<template>
  <el-card>
    <template #header><span>用户管理</span></template>
    <el-table :data="users" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{row}">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : row.role === 'COUNSELOR' ? 'warning' : 'success'" size="small">{{ row.role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="160" />
      <el-table-column label="操作" width="100">
        <template #default="{row}">
          <el-button v-if="row.status === 1" type="danger" text @click="toggleStatus(row)">禁用</el-button>
          <el-button v-else type="success" text @click="toggleStatus(row)">启用</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="total, prev, pager, next" @current-change="loadUsers" style="margin-top:20px;justify-content:center" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi } from '../api/index'

const users = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await userApi.list({ page: page.value, size: size.value })
    users.value = res.data.records
    total.value = res.data.total
  } catch (e) {}
  loading.value = false
}

onMounted(loadUsers)

const toggleStatus = async (row) => {
  try {
    await userApi.updateStatus(row.id, row.status === 1 ? 0 : 1)
    row.status = row.status === 1 ? 0 : 1
    ElMessage.success('操作成功')
  } catch (e) {}
}
</script>
