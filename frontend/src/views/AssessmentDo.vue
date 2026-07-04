<template>
  <div>
    <el-card v-if="scale">
      <template #header><span>{{ scale.name }}</span></template>
      <el-alert :title="scale.description" type="info" :closable="false" style="margin-bottom:20px" />
      <div class="question-list">
        <div v-for="(q, idx) in questions" :key="q.id" class="question-item">
          <h4>{{ idx + 1 }}. {{ q.questionText }}</h4>
          <el-radio-group v-model="answers[idx]" class="option-group">
            <el-radio v-for="opt in parseOptions(q.options)" :key="opt.label" :label="opt.label" size="large">
              {{ opt.label }}. {{ opt.text }}
            </el-radio>
          </el-radio-group>
        </div>
      </div>
      <div style="text-align:center;margin-top:30px">
        <el-button type="primary" size="large" @click="submitAssessment" :loading="submitting">提交测评</el-button>
      </div>
    </el-card>

    <el-dialog v-model="resultVisible" title="测评结果" width="450px">
      <el-result :icon="resultIcon" :title="resultTitle" :sub-title="resultDetail">
        <template #extra>
          <el-button type="primary" @click="$router.push('/assessment')">返回列表</el-button>
          <el-button @click="$router.push('/appointment')" v-if="resultRecord.resultLevel === 'MODERATE' || resultRecord.resultLevel === 'SEVERE'">预约咨询</el-button>
        </template>
      </el-result>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { assessmentApi } from '../api/index'

const route = useRoute()
const scale = ref(null)
const questions = ref([])
const answers = ref([])
const submitting = ref(false)
const resultVisible = ref(false)
const resultRecord = ref({})

const parseOptions = (opts) => {
  if (!opts) return []
  try { return typeof opts === 'string' ? JSON.parse(opts) : opts } catch (e) { return [] }
}

onMounted(async () => {
  try {
    const [sRes, qRes] = await Promise.all([
      assessmentApi.getScales(),
      assessmentApi.getQuestions(route.params.scaleId)
    ])
    scale.value = sRes.data.find(s => s.id == route.params.scaleId)
    questions.value = qRes.data
    answers.value = new Array(questions.value.length).fill('')
  } catch (e) {}
})

const resultIconMap = { NORMAL: 'success', MILD: 'warning', MODERATE: 'danger', SEVERE: 'error' }
const resultTitleMap = { NORMAL: '心理状态良好', MILD: '轻度困扰', MODERATE: '中度困扰', SEVERE: '需要关注' }

const submitAssessment = async () => {
  const unanswered = answers.value.findIndex(a => !a)
  if (unanswered >= 0) { ElMessage.warning(`请回答第 ${unanswered + 1} 题`); return }
  submitting.value = true
  try {
    const ansList = questions.value.map((q, i) => ({ questionId: q.id, selectedOption: answers.value[i] }))
    const res = await assessmentApi.submit({ scaleId: scale.value.id, answers: ansList })
    resultRecord.value = res.data
    resultVisible.value = true
  } catch (e) {}
  submitting.value = false
}

const resultIcon = computed(() => resultIconMap[resultRecord.value.resultLevel] || 'info')
const resultTitle = computed(() => resultTitleMap[resultRecord.value.resultLevel] || '')
const resultDetail = computed(() => `得分：${resultRecord.value.totalScore}分。${resultRecord.value.resultDetail || ''}`)
</script>

<style scoped>
.question-item { margin-bottom: 24px; padding: 16px; background: #fafafa; border-radius: 8px; }
.question-item h4 { margin-bottom: 12px; color: #333; }
.option-group { display: flex; flex-direction: column; gap: 8px; }
</style>
