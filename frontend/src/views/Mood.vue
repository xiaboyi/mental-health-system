<template>
  <div>
    <el-card>
      <template #header><span>今日情绪打卡</span></template>
      <div class="mood-today" v-if="!todayMood">
        <h3>今天感觉怎么样？</h3>
        <div class="mood-options">
          <div v-for="m in moodTypes" :key="m.type" class="mood-option" @click="doCheckin(m.type)">
            <span class="mood-emoji">{{ m.emoji }}</span>
            <span>{{ m.label }}</span>
          </div>
        </div>
        <div style="margin-top: 16px">
          <span>情绪评分 (1-10)：</span>
          <el-slider v-model="moodScore" :min="1" :max="10" show-input style="width:300px" />
        </div>
        <el-input v-model="moodNote" placeholder="记录一下今天的心情..." style="margin-top:12px;width:400px" />
      </div>
      <div v-else class="mood-today done">
        <h2>今日已打卡 ✅</h2>
        <p>情绪：{{ getMoodLabel(todayMood.moodType) }} ({{ todayMood.moodScore }}分)</p>
        <p v-if="todayMood.note">记录：{{ todayMood.note }}</p>
      </div>
    </el-card>

    <el-card style="margin-top:20px">
      <template #header><span>情绪历史</span></template>
      <div id="moodHistoryChart" style="height:350px"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { statsApi } from '../api/index'

const todayMood = ref(null)
const moodScore = ref(5)
const moodNote = ref('')
const moodHistory = ref([])

const moodTypes = [
  { type: 'HAPPY', label: '开心', emoji: '😊' },
  { type: 'CALM', label: '平静', emoji: '😌' },
  { type: 'SAD', label: '难过', emoji: '😢' },
  { type: 'ANXIOUS', label: '焦虑', emoji: '😰' },
  { type: 'ANGRY', label: '生气', emoji: '😡' }
]

const moodLabels = { HAPPY: '开心', CALM: '平静', SAD: '难过', ANXIOUS: '焦虑', ANGRY: '生气' }
const getMoodLabel = (t) => moodLabels[t] || t

onMounted(async () => {
  try {
    const [todayRes, historyRes] = await Promise.all([statsApi.todayMood(), statsApi.moodHistory()])
    todayMood.value = todayRes.data
    moodHistory.value = historyRes.data || []
    await nextTick()
    initChart()
  } catch (e) {}
})

const doCheckin = async (type) => {
  try {
    const res = await statsApi.moodCheckin({ moodType: type, moodScore: moodScore.value, note: moodNote.value })
    todayMood.value = res.data
    const historyRes = await statsApi.moodHistory()
    moodHistory.value = historyRes.data || []
    await nextTick()
    initChart()
  } catch (e) {}
}

const initChart = () => {
  const dom = document.getElementById('moodHistoryChart')
  if (!dom) return
  const chart = echarts.init(dom)
  const data = [...moodHistory.value].reverse()
  chart.setOption({
    title: { text: '近30日情绪变化', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.checkinDate) },
    yAxis: { type: 'value', min: 1, max: 10, name: '情绪指数' },
    series: [{
      data: data.map(d => d.moodScore), type: 'line', smooth: true,
      areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(64,158,255,.4)' }, { offset: 1, color: 'rgba(64,158,255,.05)' }] } }
    }]
  })
}
</script>

<style scoped>
.mood-today { text-align: center; padding: 20px; }
.mood-options { display: flex; justify-content: center; gap: 24px; margin: 20px 0; }
.mood-option { cursor: pointer; text-align: center; padding: 12px; border-radius: 12px; transition: .2s; }
.mood-option:hover { background: #ecf5ff; transform: scale(1.1); }
.mood-emoji { display: block; font-size: 40px; }
.mood-today.done { }
.mood-today.done h2 { color: #67c23a; }
</style>
