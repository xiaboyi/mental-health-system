<template>
  <div>
    <el-card>
      <template #header><span>心理测评</span></template>
      <el-row :gutter="20">
        <el-col :span="8" v-for="scale in scales" :key="scale.id" style="margin-bottom:20px">
          <el-card shadow="hover" class="scale-card" @click="$router.push(`/assessment/do/${scale.id}`)">
            <div class="scale-type">{{ scale.typeName }}</div>
            <h3>{{ scale.name }}</h3>
            <p class="scale-desc">{{ scale.description }}</p>
            <div class="scale-info">
              <el-tag size="small">{{ scale.questionCount }}题</el-tag>
              <el-tag size="small" type="success">满分{{ scale.totalScore }}</el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-card style="margin-top:20px">
      <template #header><span>我的测评记录</span></template>
      <el-table :data="records" style="width:100%">
        <el-table-column prop="scaleName" label="量表名称" />
        <el-table-column prop="totalScore" label="得分" width="100" />
        <el-table-column prop="resultLevel" label="等级" width="120">
          <template #default="{ row }">
            <el-tag :type="levelType(row.resultLevel)">{{ levelLabel(row.resultLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="测评时间" width="180" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" text @click="showDetail(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="detailVisible" title="测评详情" width="500px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="量表">{{ currentRecord.scaleName }}</el-descriptions-item>
        <el-descriptions-item label="得分">{{ currentRecord.totalScore }}分</el-descriptions-item>
        <el-descriptions-item label="等级">
          <el-tag :type="levelType(currentRecord.resultLevel)">{{ levelLabel(currentRecord.resultLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="时间">{{ currentRecord.createTime }}</el-descriptions-item>
        <el-descriptions-item label="结果分析" :span="2">{{ currentRecord.resultDetail }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { assessmentApi } from '../api/index'

const scales = ref([])
const records = ref([])
const detailVisible = ref(false)
const currentRecord = ref({})

const typeNames = { ANXIETY: '焦虑评估', DEPRESSION: '抑郁评估', STRESS: '压力评估', PERSONALITY: '人格评估' }
const levelLabel = (l) => ({ NORMAL: '正常', MILD: '轻度', MODERATE: '中度', SEVERE: '重度' }[l] || l)
const levelType = (l) => ({ NORMAL: 'success', MILD: 'warning', MODERATE: 'danger', SEVERE: 'info' }[l] || '')

onMounted(async () => {
  try {
    const [sRes, rRes] = await Promise.all([assessmentApi.getScales(), assessmentApi.getRecords()])
    scales.value = sRes.data.map(s => ({ ...s, typeName: typeNames[s.type] || s.type }))
    records.value = rRes.data
  } catch (e) {}
})

const showDetail = (row) => { currentRecord.value = row; detailVisible.value = true }
</script>

<style scoped>
.scale-card { cursor: pointer; }
.scale-card:hover { border-color: #409EFF; }
.scale-type { color: #409EFF; font-size: 13px; margin-bottom: 8px; }
.scale-desc { color: #999; font-size: 13px; margin: 8px 0; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.scale-info { display: flex; gap: 8px; margin-top: 12px; }
</style>
