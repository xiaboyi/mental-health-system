<template>
  <div>
    <el-card>
      <div class="article-filter">
        <el-radio-group v-model="category" @change="loadArticles">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="EMOTION">情绪管理</el-radio-button>
          <el-radio-button label="STRESS">压力应对</el-radio-button>
          <el-radio-button label="RELATION">人际关系</el-radio-button>
          <el-radio-button label="GROWTH">自我成长</el-radio-button>
          <el-radio-button label="DEPRESSION">抑郁科普</el-radio-button>
        </el-radio-group>
      </div>
    </el-card>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="8" v-for="a in articles" :key="a.id" style="margin-bottom:20px">
        <el-card shadow="hover" class="article-card" @click="$router.push(`/article/${a.id}`)">
          <img v-if="a.coverImage" :src="a.coverImage" class="cover-img" />
          <h3>{{ a.title }}</h3>
          <p class="article-summary">{{ a.summary }}</p>
          <div class="article-meta">
            <span><el-icon><View /></el-icon> {{ a.viewCount }}</span>
            <span><el-icon><Star /></el-icon> {{ a.likeCount }}</span>
            <span>{{ a.createTime?.substring(0, 10) }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-pagination v-if="total > size" v-model:current-page="page" :page-size="size" :total="total" layout="prev, pager, next" @current-change="loadArticles" style="justify-content:center;margin-top:20px" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { articleApi } from '../api/index'

const articles = ref([])
const category = ref('')
const page = ref(1)
const size = ref(9)
const total = ref(0)

const loadArticles = async () => {
  try {
    const res = await articleApi.list({ page: page.value, size: size.value, category: category.value || undefined })
    articles.value = res.data.records
    total.value = res.data.total
  } catch (e) {}
}

onMounted(loadArticles)
</script>

<style scoped>
.article-filter { }
.article-card { cursor: pointer; }
.article-card:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,.1); }
.cover-img { width: 100%; height: 160px; object-fit: cover; border-radius: 8px; margin-bottom: 12px; }
.article-summary { color: #999; font-size: 13px; margin: 8px 0; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.article-meta { display: flex; gap: 16px; font-size: 12px; color: #bbb; margin-top: 12px; padding-top: 12px; border-top: 1px solid #f0f0f0; }
.article-meta span { display: flex; align-items: center; gap: 4px; }
</style>
