<template>
  <div class="chat-container">
    <el-card class="session-list">
      <template #header><span>会话列表</span></template>
      <div v-for="s in sessions" :key="s.id" class="session-item" :class="{ active: currentSessionId === s.id }" @click="selectSession(s)">
        <div class="session-name">{{ userInfo.role === 'COUNSELOR' ? s.studentName : s.counselorName }}</div>
        <div class="session-last">{{ s.lastMessage || '暂无消息' }}</div>
      </div>
      <el-empty v-if="sessions.length === 0" description="暂无会话" />
    </el-card>
    <div class="chat-main">
      <el-card v-if="currentSessionId" class="chat-box">
        <div class="chat-messages" ref="msgContainer">
          <div v-for="msg in messages" :key="msg.id" class="msg-item" :class="{ mine: msg.senderId === userId }">
            <div class="msg-bubble">{{ msg.content }}</div>
            <div class="msg-time">{{ msg.createTime }}</div>
          </div>
        </div>
        <div class="chat-input">
          <el-input v-model="inputMsg" placeholder="输入消息..." @keyup.enter="sendMsg" />
          <el-button type="primary" @click="sendMsg" style="margin-left:10px">发送</el-button>
        </div>
      </el-card>
      <el-card v-else class="chat-placeholder">
        <el-empty description="请选择一个会话开始聊天" />
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue'
import { chatApi } from '../api/index'

const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
const userId = computed(() => userInfo.value.userId)
const sessions = ref([])
const messages = ref([])
const currentSessionId = ref(null)
const inputMsg = ref('')
const msgContainer = ref(null)
let pollTimer = null

onMounted(async () => {
  await loadSessions()
  pollTimer = setInterval(refreshMessages, 3000)
})

const loadSessions = async () => {
  try { const res = await chatApi.getSessions(); sessions.value = res.data } catch (e) {}
}

const selectSession = async (session) => {
  currentSessionId.value = session.id
  await loadMessages()
  try { await chatApi.markAsRead(session.id) } catch (e) {}
}

const loadMessages = async () => {
  if (!currentSessionId.value) return
  try { const res = await chatApi.getMessages(currentSessionId.value); messages.value = res.data; scrollToBottom() } catch (e) {}
}

const refreshMessages = async () => { if (currentSessionId.value) { await loadMessages() } }

const sendMsg = async () => {
  if (!inputMsg.value.trim() || !currentSessionId.value) return
  try {
    await chatApi.sendMessage({ sessionId: currentSessionId.value, content: inputMsg.value, receiverId: 0 })
    inputMsg.value = ''
    await loadMessages()
  } catch (e) {}
}

const scrollToBottom = async () => {
  await nextTick()
  if (msgContainer.value) msgContainer.value.scrollTop = msgContainer.value.scrollHeight
}
</script>

<style scoped>
.chat-container { display: flex; gap: 20px; height: calc(100vh - 120px); }
.session-list { width: 280px; overflow-y: auto; }
.session-item { padding: 12px; cursor: pointer; border-bottom: 1px solid #eee; }
.session-item:hover, .session-item.active { background: #ecf5ff; }
.session-name { font-weight: bold; font-size: 14px; }
.session-last { font-size: 12px; color: #999; margin-top: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.chat-main { flex: 1; display: flex; flex-direction: column; }
.chat-box { display: flex; flex-direction: column; height: 100%; }
.chat-messages { flex: 1; overflow-y: auto; padding: 10px; }
.msg-item { margin-bottom: 16px; display: flex; flex-direction: column; }
.msg-item.mine { align-items: flex-end; }
.msg-bubble { max-width: 60%; padding: 10px 16px; border-radius: 12px; background: #ecf5ff; font-size: 14px; }
.msg-item.mine .msg-bubble { background: #409EFF; color: #fff; }
.msg-time { font-size: 11px; color: #ccc; margin-top: 4px; }
.chat-input { display: flex; padding-top: 10px; border-top: 1px solid #eee; }
.chat-placeholder { flex: 1; display: flex; align-items: center; justify-content: center; }
</style>
