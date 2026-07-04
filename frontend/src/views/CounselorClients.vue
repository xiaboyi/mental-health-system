<template>
  <div>
    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ clients.length }}</div><div class="stat-label">来访者总数</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ todayAppts }}</div><div class="stat-label">今日预约</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ pendingCount }}</div><div class="stat-label">待处理预约</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ riskCount }}</div><div class="stat-label">重点关注</div></el-card></el-col>
    </el-row>

    <el-card>
      <template #header><span>来访者列表</span></template>
      <el-table :data="clients" v-loading="loading" style="width:100%">
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="gender" label="性别" width="60">
          <template #default="{row}">{{ row.gender===1?'男':row.gender===2?'女':'未知' }}</template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="60" />
        <el-table-column prop="totalAppointments" label="咨询次数" width="80" />
        <el-table-column prop="lastAppointmentDate" label="最近咨询" width="110" />
        <el-table-column prop="riskLevel" label="测评风险" width="100">
          <template #default="{row}">
            <el-tag v-if="row.riskLevel" :type="riskType(row.riskLevel)" size="small">{{ row.riskLevel }}</el-tag>
            <span v-else style="color:#999">未测评</span>
          </template>
        </el-table-column>
        <el-table-column prop="latestMood" label="今日情绪" width="90">
          <template #default="{row}">
            <span v-if="row.latestMood" :style="{color: row.latestMood>=7?'#67c23a':row.latestMood>=4?'#e6a23c':'#f56c6c'}">{{ row.latestMood }}分</span>
            <span v-else style="color:#999">未打卡</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{row}">
            <el-button type="primary" text @click="startChat(row)">发起咨询</el-button>
            <el-button type="success" text @click="showMoodHistory(row)">情绪历史</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="clients.length===0&&!loading" description="暂无来访者" />
    </el-card>

    <!-- 情绪历史弹窗 -->
    <el-dialog v-model="moodVisible" :title="moodStudentName + ' 的情绪变化'" width="700px" @opened="initMoodChart">
      <div id="clientMoodChart" style="height:350px"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { counselorApi, chatApi, statsApi } from '../api/index'
import request from '../utils/request'

const router = useRouter()
const clients = ref([])
const loading = ref(false)
const counselorStats = ref({})
const moodVisible = ref(false)
const moodStudentName = ref('')
const moodHistoryData = ref([])

const todayAppts = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return (counselorStats.value.weeklyAppointments||[]).find(w=>w.date===today)?.count||0
})
const pendingCount = computed(() => counselorStats.value.pendingAppointments||0)
const riskCount = computed(() => clients.value.filter(c=>c.riskLevel==='SEVERE'||c.riskLevel==='MODERATE').length)

const riskType = (level) => ({ NORMAL:'success', MILD:'warning', MODERATE:'danger', SEVERE:'danger' }[level]||'info')

const loadData = async () => {
  loading.value = true
  try {
    const [cRes, sRes] = await Promise.all([counselorApi.getClients(), statsApi.counselorDashboard()])
    clients.value = cRes.data || []
    counselorStats.value = sRes.data || {}
  } catch (e) {}
  loading.value = false
}

const startChat = async (client) => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo')||'{}')
    const res = await chatApi.getOrCreateSession({ studentId: client.studentId, counselorId: userInfo.userId })
    if (res.data?.id) {
      router.push('/chat')
      ElMessage.success('已进入咨询会话')
    }
  } catch (e) {}
}

const showMoodHistory = async (client) => {
  moodStudentName.value = client.studentName
  moodVisible.value = true
  try {
    const res = await request.get(`/statistics/mood/student/${client.studentId}`)
    moodHistoryData.value = res.data || []
  } catch (e) {
    moodHistoryData.value = []
  }
}

const initMoodChart = async () => {
  await nextTick()
  const dom = document.getElementById('clientMoodChart')
  if (!dom) return
  const chart = echarts.init(dom)
  const data = [...moodHistoryData.value].reverse()
  if (data.length === 0) {
    chart.setOption({
      title: { text: '该学生暂无情绪打卡记录', left: 'center', textStyle: { fontSize: 14, color: '#999' } }
    })
    return
  }
  chart.setOption({
    title: { text: '近30日情绪变化趋势', left: 'center', textStyle: { fontSize: 14 } },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.checkinDate), axisLabel: { rotate: 30, fontSize: 10 } },
    yAxis: { type: 'value', min: 1, max: 10, name: '情绪指数' },
    series: [{
      data: data.map(d => d.moodScore), type: 'line', smooth: true,
      areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(64,158,255,.4)' }, { offset: 1, color: 'rgba(64,158,255,.05)' }] } },
      itemStyle: { color: '#409EFF' },
      markLine: {
        silent: true,
        data: [{ yAxis: 4, lineStyle: { color: '#f56c6c', type: 'dashed' }, label: { formatter: '警戒线' } }]
      }
    }]
  })
}

onMounted(loadData)
</script>

<style scoped>
.stat-card { text-align:center; cursor:pointer; transition:transform .2s; }
.stat-card:hover { transform:translateY(-4px); }
.stat-num { font-size:28px; font-weight:bold; color:#409EFF; }
.stat-label { font-size:13px; color:#999; margin-top:6px; }
</style>
