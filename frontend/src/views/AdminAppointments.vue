<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>预约管理</span>
        <el-select v-model="filterStatus" placeholder="筛选状态" clearable style="width:160px" @change="loadData">
          <el-option label="全部" value="" />
          <el-option label="待确认" value="PENDING" />
          <el-option label="已确认" value="CONFIRMED" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
      </div>
    </template>
    <el-table :data="appointments" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="studentName" label="学生姓名" width="100" />
      <el-table-column prop="counselorName" label="咨询师" width="100" />
      <el-table-column prop="counselorTitle" label="职称" width="100" />
      <el-table-column prop="appointmentDate" label="日期" width="110" />
      <el-table-column prop="timeSlot" label="时间段" width="110" />
      <el-table-column prop="type" label="方式" width="80">
        <template #default="{row}">{{ row.type === 'ONLINE' ? '线上' : '面对面' }}</template>
      </el-table-column>
      <el-table-column prop="reason" label="咨询原因" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{row}">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{row}">
          <el-button v-if="row.status==='PENDING'" type="success" size="small" @click="handleStatus(row.id,'CONFIRMED')">确认</el-button>
          <el-button v-if="row.status==='PENDING'" type="danger" size="small" @click="handleStatus(row.id,'CANCELLED')">拒绝</el-button>
          <el-button v-if="row.status==='CONFIRMED'" type="primary" size="small" @click="handleStatus(row.id,'COMPLETED')">完成</el-button>
          <el-button v-if="row.status!=='CANCELLED'" type="warning" size="small" @click="handleStatus(row.id,'CANCELLED')">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="total, prev, pager, next" @current-change="loadData" style="margin-top:20px;justify-content:center" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { appointmentApi } from '../api/index'

const appointments = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(15)
const total = ref(0)
const filterStatus = ref('')

const statusLabel = (s) => ({ PENDING:'待确认', CONFIRMED:'已确认', COMPLETED:'已完成', CANCELLED:'已取消' }[s]||s)
const statusType = (s) => ({ PENDING:'warning', CONFIRMED:'success', COMPLETED:'info', CANCELLED:'danger' }[s]||'')

const loadData = async () => {
  loading.value = true
  try {
    const res = await appointmentApi.getAll({ page: page.value, size: size.value, status: filterStatus.value || undefined })
    appointments.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (e) {}
  loading.value = false
}

const handleStatus = async (id, status) => {
  try {
    await appointmentApi.updateStatus(id, status)
    ElMessage.success('操作成功')
    loadData()
  } catch (e) {}
}

onMounted(loadData)
</script>
