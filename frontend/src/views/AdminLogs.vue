<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>操作日志</span>
        <div style="display:flex;gap:10px">
          <el-input v-model="searchUser" placeholder="搜索用户名" clearable style="width:160px" @change="loadLogs" />
          <el-input v-model="searchOp" placeholder="搜索操作" clearable style="width:160px" @change="loadLogs" />
        </div>
      </div>
    </template>
    <el-table :data="logs" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="操作用户" width="110" />
      <el-table-column prop="operation" label="操作内容" show-overflow-tooltip />
      <el-table-column prop="method" label="方法" width="200" show-overflow-tooltip />
      <el-table-column prop="params" label="参数" show-overflow-tooltip />
      <el-table-column prop="ip" label="IP地址" width="140" />
      <el-table-column prop="duration" label="耗时(ms)" width="80" />
      <el-table-column prop="createTime" label="时间" width="160" />
    </el-table>
    <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="total, prev, pager, next" @current-change="loadLogs" style="margin-top:20px;justify-content:center" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminApi } from '../api/index'

const logs = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(20)
const total = ref(0)
const searchUser = ref('')
const searchOp = ref('')

const loadLogs = async () => {
  loading.value = true
  try {
    const res = await adminApi.logs({ page: page.value, size: size.value, username: searchUser.value || undefined, operation: searchOp.value || undefined })
    logs.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (e) {}
  loading.value = false
}

onMounted(loadLogs)
</script>
