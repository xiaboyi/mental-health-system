<template>
  <el-card v-if="article" class="article-detail">
    <h1 class="a-title">{{ article.title }}</h1>
    <div class="a-meta">
      <span>作者：{{ article.authorName }}</span>
      <span>浏览：{{ article.viewCount }}</span>
      <span>点赞：{{ article.likeCount }}</span>
      <span>{{ article.createTime?.substring(0, 10) }}</span>
    </div>
    <el-divider />
    <div class="a-content" v-html="article.content"></div>
    <div class="a-actions">
      <el-button @click="handleLike" type="warning"><el-icon><Star /></el-icon> 点赞 ({{ article.likeCount }})</el-button>
    </div>

    <el-divider />
    <h3>评论 ({{ comments.length }})</h3>
    <div class="comment-input">
      <el-input v-model="commentText" type="textarea" rows="2" placeholder="写下你的评论..." />
      <el-button type="primary" @click="addComment" style="margin-top:10px">发表评论</el-button>
    </div>
    <div v-for="c in comments" :key="c.id" class="comment-item">
      <div class="comment-header">
        <el-avatar :size="32">{{ c.realName?.charAt(0) }}</el-avatar>
        <span class="comment-user">{{ c.realName || c.username }}</span>
        <span class="comment-time">{{ c.createTime }}</span>
      </div>
      <div class="comment-content">{{ c.content }}</div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { articleApi } from '../api/index'

const route = useRoute()
const article = ref(null)
const comments = ref([])
const commentText = ref('')

onMounted(async () => {
  await loadArticle()
  await loadComments()
})

const loadArticle = async () => {
  try { const res = await articleApi.detail(route.params.id); article.value = res.data } catch (e) {}
}
const loadComments = async () => {
  try { const res = await articleApi.comments(route.params.id); comments.value = res.data } catch (e) {}
}
const handleLike = async () => {
  try { await articleApi.like(article.value.id); article.value.likeCount++; ElMessage.success('点赞成功') } catch (e) {}
}
const addComment = async () => {
  if (!commentText.value.trim()) return
  try {
    await articleApi.addComment({ articleId: article.value.id, content: commentText.value })
    commentText.value = ''
    loadComments()
  } catch (e) {}
}
</script>

<style scoped>
.article-detail { max-width: 800px; margin: 0 auto; }
.a-title { font-size: 24px; }
.a-meta { display: flex; gap: 20px; color: #999; font-size: 13px; margin-top: 12px; }
.a-content { line-height: 1.8; font-size: 15px; color: #333; }
.a-content :deep(h2) { margin: 20px 0 10px; }
.a-content :deep(p) { margin: 10px 0; }
.a-actions { text-align: center; margin: 30px 0; }
.comment-input { margin: 16px 0; }
.comment-item { padding: 12px 0; border-bottom: 1px solid #f0f0f0; }
.comment-header { display: flex; align-items: center; gap: 8px; }
.comment-user { font-weight: bold; font-size: 14px; }
.comment-time { font-size: 12px; color: #ccc; margin-left: auto; }
.comment-content { margin: 8px 0 0 40px; font-size: 14px; color: #555; }
</style>
