<template>
  <el-card>
    <template #header><span>咨询师列表</span></template>
    <el-row :gutter="20">
      <el-col :span="8" v-for="c in counselors" :key="c.id" style="margin-bottom:20px">
        <el-card shadow="hover" class="counselor-card">
          <div class="counselor-header">
            <el-avatar :size="60" :src="c.avatar">{{ c.realName?.charAt(0) }}</el-avatar>
            <div class="counselor-info">
              <h3>{{ c.realName }}</h3>
              <el-tag size="small">{{ c.title }}</el-tag>
            </div>
          </div>
          <div class="counselor-body">
            <p><strong>擅长领域：</strong>{{ c.specialty }}</p>
            <p><strong>从业年限：</strong>{{ c.workYears }}年</p>
            <p class="desc">{{ c.description }}</p>
          </div>
          <div class="counselor-footer">
            <el-button type="primary" @click="$router.push('/appointment')">预约咨询</el-button>
            <el-button @click="startChat(c.userId)">在线咨询</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-if="counselors.length === 0" description="暂无咨询师信息" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { counselorApi, chatApi } from '../api/index'

const router = useRouter()
const counselors = ref([])

onMounted(async () => {
  try { const res = await counselorApi.list(); counselors.value = res.data } catch (e) {}
})

const startChat = async (counselorId) => {
  try {
    await chatApi.getOrCreateSession({ counselorId })
    router.push('/chat')
  } catch (e) {}
}
</script>

<style scoped>
.counselor-card { cursor: pointer; }
.counselor-header { display: flex; gap: 16px; align-items: center; margin-bottom: 16px; }
.counselor-info h3 { margin: 0 0 6px 0; }
.counselor-body p { margin: 6px 0; font-size: 14px; color: #666; }
.counselor-body .desc { display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; color: #999; }
.counselor-footer { display: flex; gap: 10px; margin-top: 16px; }
</style>
