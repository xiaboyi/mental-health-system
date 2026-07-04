<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>文章管理</span>
        <el-button type="primary" @click="showEditor(null)">发布文章</el-button>
      </div>
    </template>
    <el-table :data="articles" v-loading="loading" style="width:100%">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="category" label="分类" width="120" />
      <el-table-column prop="viewCount" label="浏览量" width="100" />
      <el-table-column prop="likeCount" label="点赞" width="80" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{row}">
          <el-tag :type="row.status === 'PUBLISHED' ? 'success' : 'info'" size="small">{{ row.status === 'PUBLISHED' ? '已发布' : '草稿' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="160" />
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button type="primary" text @click="showEditor(row)">编辑</el-button>
          <el-button type="danger" text @click="deleteArticle(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :page-size="size" :total="total" layout="total, prev, pager, next" @current-change="loadArticles" style="margin-top:20px;justify-content:center" />

    <el-dialog v-model="editorVisible" :title="editingId ? '编辑文章' : '发布文章'" width="700px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width:100%">
            <el-option label="情绪管理" value="EMOTION" />
            <el-option label="压力应对" value="STRESS" />
            <el-option label="人际关系" value="RELATION" />
            <el-option label="自我成长" value="GROWTH" />
            <el-option label="抑郁科普" value="DEPRESSION" />
          </el-select>
        </el-form-item>
        <el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" rows="2" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" rows="10" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="PUBLISHED">发布</el-radio>
            <el-radio label="DRAFT">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editorVisible = false">取消</el-button>
        <el-button type="primary" @click="saveArticle" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { articleApi } from '../api/index'

const articles = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const editorVisible = ref(false)
const editingId = ref(null)
const saving = ref(false)
const form = reactive({ title: '', category: 'EMOTION', summary: '', content: '', status: 'PUBLISHED' })

const loadArticles = async () => {
  loading.value = true
  try {
    const res = await articleApi.manageList({ page: page.value, size: size.value })
    articles.value = res.data.records
    total.value = res.data.total
  } catch (e) {}
  loading.value = false
}

onMounted(loadArticles)

const showEditor = (row) => {
  if (row) {
    editingId.value = row.id
    Object.assign(form, { title: row.title, category: row.category, summary: row.summary, content: row.content, status: row.status })
  } else {
    editingId.value = null
    Object.assign(form, { title: '', category: 'EMOTION', summary: '', content: '', status: 'PUBLISHED' })
  }
  editorVisible.value = true
}

const saveArticle = async () => {
  saving.value = true
  try {
    if (editingId.value) {
      await articleApi.update(editingId.value, form)
      ElMessage.success('更新成功')
    } else {
      await articleApi.create(form)
      ElMessage.success('发布成功')
    }
    editorVisible.value = false
    loadArticles()
  } catch (e) {}
  saving.value = false
}

const deleteArticle = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    await articleApi.delete(id)
    ElMessage.success('删除成功')
    loadArticles()
  } catch (e) {}
}
</script>
