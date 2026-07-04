<template>
  <el-row :gutter="20">
    <el-col :span="16">
      <el-card>
        <template #header><span>我的预约</span></template>
        <el-table :data="appointments" style="width:100%">
          <el-table-column prop="counselorName" label="咨询师" />
          <el-table-column prop="counselorTitle" label="职称" />
          <el-table-column prop="appointmentDate" label="日期" width="120" />
          <el-table-column prop="timeSlot" label="时间段" width="120" />
          <el-table-column prop="type" label="方式" width="100">
            <template #default="{row}">{{ row.type === 'ONLINE' ? '线上' : '面对面' }}</template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{row}">
              <el-tag :type="statusType(row.status)">{{ statusLabel(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="140">
            <template #default="{row}">
              <el-button v-if="row.status === 'PENDING'" type="danger" text @click="cancel(row.id)">取消</el-button>
              <template v-if="userInfo.role !== 'STUDENT' && row.status === 'PENDING'">
                <el-button type="success" text @click="confirm(row.id)">确认</el-button>
                <el-button type="danger" text @click="reject(row.id)">拒绝</el-button>
              </template>
              <el-button v-if="row.status === 'CONFIRMED'" type="primary" text @click="complete(row.id)">完成</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="appointments.length === 0" description="暂无预约记录" />
      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card v-if="userInfo.role === 'STUDENT'">
        <template #header><span>新建预约</span></template>
        <el-form :model="form" label-width="80px">
          <el-form-item label="咨询师">
            <el-select v-model="form.counselorId" placeholder="请选择" style="width:100%">
              <el-option v-for="c in counselors" :key="c.id" :label="c.realName + ' - ' + c.title" :value="c.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="日期"><input v-model="form.appointmentDate" type="date" class="el-input__inner" style="width:100%;padding: 9px 12px; border: 1px solid #dcdfe6; border-radius: 4px;" /></el-form-item>
          <el-form-item label="时间段">
            <el-select v-model="form.timeSlot" placeholder="选择时间段" style="width:100%">
              <el-option v-for="t in timeSlots" :key="t" :label="t" :value="t" />
            </el-select>
          </el-form-item>
          <el-form-item label="方式">
            <el-radio-group v-model="form.type">
              <el-radio :value="'FACE'">面对面</el-radio>
              <el-radio :value="'ONLINE'">线上</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="原因"><el-input v-model="form.reason" type="textarea" :rows="3" placeholder="请简要描述咨询原因" /></el-form-item>
          <el-form-item><el-button type="primary" @click="createAppointment" :loading="creating">提交预约</el-button></el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { appointmentApi, counselorApi } from '../api/index'

const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
const appointments = ref([])
const counselors = ref([])
const creating = ref(false)
const form = reactive({ counselorId: null, appointmentDate: null, timeSlot: '', type: 'FACE', reason: '' })
const timeSlots = ['09:00-10:00', '10:00-11:00', '11:00-12:00', '14:00-15:00', '15:00-16:00', '16:00-17:00']

const statusLabel = (s) => ({ PENDING: '待确认', CONFIRMED: '已确认', COMPLETED: '已完成', CANCELLED: '已取消' }[s] || s)
const statusType = (s) => ({ PENDING: 'warning', CONFIRMED: 'success', COMPLETED: 'info', CANCELLED: 'danger' }[s] || '')

const formatDate = (date) => {
  if (!date) return ''
  if (typeof date === 'string') return date
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const loadData = async () => {
  try {
    const [aRes, cRes] = await Promise.all([appointmentApi.getMy(), counselorApi.list()])
    appointments.value = aRes.data
    counselors.value = cRes.data
  } catch (e) {
    console.error(e)
  }
}

onMounted(async () => { await loadData() })
const createAppointment = async () => {
  if (!form.counselorId || !form.appointmentDate || !form.timeSlot) { ElMessage.warning('请填写完整信息'); return }
  creating.value = true
  try {
    await appointmentApi.create({
      counselorId: form.counselorId,
      appointmentDate: formatDate(form.appointmentDate),
      timeSlot: form.timeSlot,
      type: form.type,
      reason: form.reason
    })
    ElMessage.success('预约成功')
    form.counselorId = null
    form.appointmentDate = null
    form.timeSlot = ''
    form.type = 'FACE'
    form.reason = ''
    await loadData()
  } catch (e) {
    ElMessage.error(e.message || '预约失败')
  }
  creating.value = false
}

const cancel = async (id) => { try { await appointmentApi.updateStatus(id, 'CANCELLED'); loadData() } catch (e) {} }
const confirm = async (id) => { try { await appointmentApi.updateStatus(id, 'CONFIRMED'); loadData() } catch (e) {} }
const reject = async (id) => { try { await appointmentApi.updateStatus(id, 'CANCELLED'); loadData() } catch (e) {} }
const complete = async (id) => { try { await appointmentApi.updateStatus(id, 'COMPLETED'); loadData() } catch (e) {} }
</script>
