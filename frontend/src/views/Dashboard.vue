<template>
  <div class="dashboard">
    <!-- Stats Row -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ stats.totalUsers || 0 }}</div><div class="stat-label">系统用户</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ stats.totalCounselors || 0 }}</div><div class="stat-label">咨询师</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ stats.totalAppointments || 0 }}</div><div class="stat-label">预约总数</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ stats.pendingAppointments || 0 }}</div><div class="stat-label">待处理预约</div></el-card></el-col>
    </el-row>

    <!-- Counselor specific stats -->
    <el-row v-if="userRole==='COUNSELOR'" :gutter="20" style="margin-top:20px">
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ counselorStats.uniqueClients || 0 }}</div><div class="stat-label">来访者总数</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ counselorStats.myAppointments || 0 }}</div><div class="stat-label">我的预约</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ counselorStats.pendingAppointments || 0 }}</div><div class="stat-label">待确认预约</div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-num">{{ counselorStats.todayCompleted || 0 }}</div><div class="stat-label">今日已完成</div></el-card></el-col>
    </el-row>

    <!-- Row 1: Mood trend + Appointment status pie -->
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card><div id="moodChart" style="height:300px"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card><div id="apptChart" style="height:300px"></div></el-card>
      </el-col>
    </el-row>

    <!-- Row 2: Assessment distribution + Weekly appointments -->
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card><div id="assessChart" style="height:300px"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card><div id="weeklyChart" style="height:300px"></div></el-card>
      </el-col>
    </el-row>

    <!-- Row 3: Role distribution + User trend (admin only) -->
    <el-row v-if="userRole==='ADMIN'" :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card><div id="roleChart" style="height:300px"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card><div id="userTrendChart" style="height:300px"></div></el-card>
      </el-col>
    </el-row>

    <!-- Counselor weekly chart -->
    <el-row v-if="userRole==='COUNSELOR'" :gutter="20" style="margin-top:20px">
      <el-col :span="24">
        <el-card><div id="counselorWeeklyChart" style="height:300px"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { statsApi, adminApi } from '../api/index'

const stats = ref({})
const counselorStats = ref({})
const userRole = ref(JSON.parse(localStorage.getItem('userInfo')||'{}').role||'STUDENT')

const loadData = async () => {
  try {
    if (userRole.value === 'COUNSELOR') {
      const [sRes, cRes] = await Promise.all([statsApi.dashboard(), statsApi.counselorDashboard()])
      stats.value = sRes.data
      counselorStats.value = cRes.data || {}
    } else {
      const res = await statsApi.dashboard()
      stats.value = res.data
    }
    await nextTick()
    initCharts()
    if (userRole.value === 'ADMIN') initAdminCharts()
    if (userRole.value === 'COUNSELOR') initCounselorCharts()
  } catch (e) {}
}

const initCharts = () => {
  const data = stats.value
  // Mood trend
  const moodDom = document.getElementById('moodChart')
  if (moodDom) {
    const chart = echarts.init(moodDom)
    const dates = (data.moodTrend||[]).map(i=>i.date).reverse()
    const scores = (data.moodTrend||[]).map(i=>i.avg_score).reverse()
    chart.setOption({
      title:{text:'近7日情绪趋势',left:'center',textStyle:{fontSize:14}},
      tooltip:{trigger:'axis'},
      xAxis:{type:'category',data:dates},
      yAxis:{type:'value',min:0,max:10,name:'情绪指数'},
      series:[{data:scores,type:'line',smooth:true,areaStyle:{color:'rgba(103,194,58,.3)'},itemStyle:{color:'#67c23a'}}]
    })
  }
  // Appointment status pie
  const apptDom = document.getElementById('apptChart')
  if (apptDom) {
    const chart = echarts.init(apptDom)
    const statuses = (data.appointmentByStatus||[]).map(i=>i.status)
    const counts = (data.appointmentByStatus||[]).map(i=>i.count)
    chart.setOption({
      title:{text:'预约状态分布',left:'center',textStyle:{fontSize:14}},
      tooltip:{trigger:'item'},
      legend:{bottom:10},
      series:[{type:'pie',radius:['40%','70%'],center:['50%','45%'],data:statuses.map((s,i)=>({name:s,value:counts[i]})),emphasis:{itemStyle:{shadowBlur:10,shadowColor:'rgba(0,0,0,.3)'}}}]
    })
  }
  // Assessment distribution bar
  const assessDom = document.getElementById('assessChart')
  if (assessDom) {
    const chart = echarts.init(assessDom)
    const levels = (data.assessmentDistribution||[]).map(i=>i.result_level)
    const vals = (data.assessmentDistribution||[]).map(i=>i.count)
    const colorMap = {NORMAL:'#67c23a',MILD:'#e6a23c',MODERATE:'#f56c6c',SEVERE:'#909399'}
    chart.setOption({
      title:{text:'测评结果分布',left:'center',textStyle:{fontSize:14}},
      tooltip:{trigger:'axis'},
      xAxis:{type:'category',data:levels},
      yAxis:{type:'value',name:'人次'},
      series:[{data:vals,type:'bar',itemStyle:{color:(p)=>colorMap[levels[p.dataIndex]]||'#409EFF'},barWidth:'40%'}]
    })
  }
  // Weekly appointments bar
  const weeklyDom = document.getElementById('weeklyChart')
  if (weeklyDom) {
    const chart = echarts.init(weeklyDom)
    const wdates = (data.weeklyAppointments||[]).map(i=>i.date)
    const wcounts = (data.weeklyAppointments||[]).map(i=>i.count)
    chart.setOption({
      title:{text:'近7日预约趋势',left:'center',textStyle:{fontSize:14}},
      tooltip:{trigger:'axis'},
      xAxis:{type:'category',data:wdates},
      yAxis:{type:'value',name:'预约数'},
      series:[{data:wcounts,type:'bar',itemStyle:{color:'#409EFF',borderRadius:[4,4,0,0]},barWidth:'40%'}]
    })
  }
}

const initAdminCharts = async () => {
  try {
    const [roleRes, trendRes] = await Promise.all([adminApi.roleDistribution(), adminApi.userTrend()])
    const roleData = roleRes.data
    const trendData = trendRes.data

    // Role distribution pie
    const roleDom = document.getElementById('roleChart')
    if (roleDom) {
      const chart = echarts.init(roleDom)
      chart.setOption({
        title:{text:'用户角色分布',left:'center',textStyle:{fontSize:14}},
        tooltip:{trigger:'item'},
        legend:{bottom:10},
        series:[{type:'pie',radius:['40%','70%'],center:['50%','45%'],data:[
          {name:'学生',value:roleData.STUDENT||0,itemStyle:{color:'#67c23a'}},
          {name:'咨询师',value:roleData.COUNSELOR||0,itemStyle:{color:'#e6a23c'}},
          {name:'管理员',value:roleData.ADMIN||0,itemStyle:{color:'#409EFF'}},
        ],label:{formatter:'{b}: {c}'},emphasis:{itemStyle:{shadowBlur:10,shadowColor:'rgba(0,0,0,.3)'}}}]
      })
    }
    // User trend line
    const trendDom = document.getElementById('userTrendChart')
    if (trendDom) {
      const chart = echarts.init(trendDom)
      chart.setOption({
        title:{text:'用户增长趋势(近30天)',left:'center',textStyle:{fontSize:14}},
        tooltip:{trigger:'axis'},
        xAxis:{type:'category',data:trendData.dates||[],axisLabel:{rotate:45,fontSize:8}},
        yAxis:{type:'value',name:'累计用户'},
        series:[{data:trendData.counts||[],type:'line',smooth:true,areaStyle:{color:'rgba(102,126,234,.2)'},itemStyle:{color:'#667eea'},lineStyle:{width:2}}]
      })
    }
  } catch (e) {}
}

const initCounselorCharts = () => {
  const data = counselorStats.value
  const weeklyDom = document.getElementById('counselorWeeklyChart')
  if (weeklyDom) {
    const chart = echarts.init(weeklyDom)
    const wdates = (data.weeklyAppointments||[]).map(i=>i.date)
    const wcounts = (data.weeklyAppointments||[]).map(i=>i.count)
    chart.setOption({
      title:{text:'我的近7日预约趋势',left:'center',textStyle:{fontSize:14}},
      tooltip:{trigger:'axis'},
      xAxis:{type:'category',data:wdates},
      yAxis:{type:'value',name:'预约数'},
      series:[{data:wcounts,type:'bar',itemStyle:{color:'#409EFF',borderRadius:[4,4,0,0]},barWidth:'40%'}]
    })
  }
}

onMounted(loadData)
</script>

<style scoped>
.dashboard { }
.stat-card { text-align:center; cursor:pointer; transition:transform .2s; }
.stat-card:hover { transform:translateY(-4px); }
.stat-num { font-size:32px; font-weight:bold; color:#409EFF; }
.stat-label { font-size:14px; color:#999; margin-top:8px; }
</style>
