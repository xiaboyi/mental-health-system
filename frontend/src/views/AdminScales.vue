<template>
  <el-card>
    <template #header><span>量表管理</span></template>
    <el-table :data="scales" v-loading="loading" style="width:100%">
      <el-table-column prop="name" label="量表名称" />
      <el-table-column prop="type" label="类型" width="120" />
      <el-table-column prop="questionCount" label="题目数" width="100" />
      <el-table-column prop="totalScore" label="满分" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button type="primary" text @click="toggleStatus(row)">{{ row.status === 1 ? '停用' : '启用' }}</el-button>
          <el-button type="info" text @click="viewQuestions(row)">题目</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="questionsVisible" title="题目列表" width="700px">
      <el-table :data="questions" style="width:100%">
        <el-table-column prop="sortOrder" label="序号" width="60" />
        <el-table-column prop="questionText" label="题目内容" show-overflow-tooltip />
        <el-table-column label="选项" width="100">
          <template #default="{row}">{{ parseOptions(row.options).length }}个选项</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { assessmentApi } from '../api/index'

const scales = ref([])
const loading = ref(false)
const questionsVisible = ref(false)
const questions = ref([])

const parseOptions = (opts) => {
  if (!opts) return []
  try { return typeof opts === 'string' ? JSON.parse(opts) : opts } catch (e) { return [] }
}

onMounted(async () => {
  loading.value = true
  try { const res = await assessmentApi.getScales(); scales.value = res.data } catch (e) {}
  loading.value = false
})

const toggleStatus = async (row) => {
  ElMessage.info('演示模式：状态切换功能')
}

const viewQuestions = async (row) => {
  try {
    const res = await assessmentApi.getQuestions(row.id)
    questions.value = res.data || []
    questionsVisible.value = true
  } catch (e) {}
}
</script>
