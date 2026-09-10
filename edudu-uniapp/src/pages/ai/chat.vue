
<template>
  <view class="chat-page">
    <view class="chat-messages" ref="messagesContainer">
      <view v-for="(msg, index) in messages" :key="index" class="message-item" :class="msg.role">
        <view v-if="msg.role === 'user'" class="user-avatar">
          <text>{{ currentChild?.name?.charAt(0) || '我' }}</text>
        </view>
        <view v-else class="assistant-avatar">
          <text>🤖</text>
        </view>
        <view class="message-content">
          <text class="message-text">{{ msg.content }}</text>
          <text v-if="msg.timestamp" class="message-time">{{ formatTime(msg.timestamp) }}</text>
        </view>
      </view>

      <view v-if="loading" class="message-item assistant">
        <view class="assistant-avatar">
          <text>🤖</text>
        </view>
        <view class="message-content">
          <view class="typing-indicator">
            <view class="dot"></view>
            <view class="dot"></view>
            <view class="dot"></view>
          </view>
        </view>
      </view>
    </view>

    <view class="chat-input-section">
      <view class="quick-prompts" v-if="messages.length === 0">
        <view class="prompt-chip" @click="sendQuickPrompt('给我讲个睡前故事')">
          <text>🌙 睡前故事</text>
        </view>
        <view class="prompt-chip" @click="sendQuickPrompt('如何培养孩子的阅读习惯')">
          <text>📚 育儿建议</text>
        </view>
        <view class="prompt-chip" @click="sendQuickPrompt('推荐适合3岁孩子的游戏')">
          <text>🎮 游戏推荐</text>
        </view>
      </view>

      <view class="input-wrapper">
        <textarea
          v-model="inputMessage"
          class="input-field"
          placeholder="说点什么..."
          :disabled="loading"
          :maxlength="500"
          :auto-height="true"
          :show-confirm-bar="false"
        />
        <button class="send-btn" @click="sendMessage" :disabled="loading || !inputMessage.trim()">
          <text>发送</text>
        </button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, nextTick, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { sendChatMessage } from '@/api/ai'
import type { ChatMessage } from '@/api/ai'
import { showError } from '@/utils'

// 常量定义
const MAX_MESSAGE_LENGTH = 500
const SCROLL_DURATION = 300

const userStore = useUserStore()
const childStore = useChildStore()

const messagesContainer = ref<HTMLElement | null>(null)
const inputMessage = ref('')
const loading = ref(false)
const conversationId = ref('')
const messages = ref<ChatMessage[]>([])

const currentChild = computed(() => {
  if (!userStore.currentChildId) return null
  return childStore.children.find(c => c.id === userStore.currentChildId)
})

const formatTime = (time: string) => {
  const date = new Date(time)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      uni.pageScrollTo({
        scrollTop: 999999,
        duration: 300
      })
    }
  })
}

const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || loading.value) return

  const userMessage: ChatMessage = {
    role: 'user',
    content,
    timestamp: new Date().toISOString()
  }

  messages.value.push(userMessage)
  inputMessage.value = ''
  loading.value = true
  scrollToBottom()

  try {
    const res = await sendChatMessage({
      message: content,
      childId: userStore.currentChildId || undefined,
      conversationId: conversationId.value || undefined
    })

    if (res.code === 200 && res.data) {
      const assistantMessage: ChatMessage = {
        role: 'assistant',
        content: res.data.content,
        timestamp: new Date().toISOString()
      }
      messages.value.push(assistantMessage)
      conversationId.value = res.data.conversationId
    }
  } catch (error) {
    showError('发送失败，请重试')
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

const sendQuickPrompt = (prompt: string) => {
  inputMessage.value = prompt
  sendMessage()
}

onMounted(() => {
  if (!userStore.isLoggedIn) {
    uni.reLaunch({ url: '/pages/user/login' })
    return
  }
  childStore.fetchChildren()
  // 添加欢迎消息
  messages.value = [
    {
      role: 'assistant',
      content: `你好！我是嘟嘟AI助手，我可以帮你${currentChild.value?.name || '孩子'}讲故事、解答育儿问题、推荐早教任务。有什么需要帮助的吗？`,
      timestamp: new Date().toISOString()
    }
  ]
})
</script>

<style lang="scss" scoped>
.chat-page {
  min-height: 100vh;
  background: #F8F8F8;
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex: 1;
  padding: 32rpx;
  overflow-y: auto;
}

.message-item {
  display: flex;
  margin-bottom: 32rpx;
  align-items: flex-start;
}

.message-item.user {
  flex-direction: row-reverse;
}

.user-avatar, .assistant-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.user-avatar {
  background: linear-gradient(135deg, #F59E0B, #F97316);
  margin-left: 20rpx;
}

.user-avatar text {
  color: #fff;
  font-size: 28rpx;
  font-weight: bold;
}

.assistant-avatar {
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  margin-right: 20rpx;
  font-size: 36rpx;
}

.message-content {
  max-width: 520rpx;
}

.message-text {
  background: #fff;
  padding: 24rpx;
  border-radius: 20rpx;
  font-size: 28rpx;
  line-height: 1.6;
  color: #1F2937;
  display: block;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.message-item.user .message-text {
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  color: #fff;
}

.message-time {
  font-size: 22rpx;
  color: #9CA3AF;
  margin-top: 8rpx;
  display: block;
}

.message-item.user .message-time {
  text-align: right;
}

.typing-indicator {
  background: #fff;
  padding: 32rpx 24rpx;
  border-radius: 20rpx;
  display: flex;
  gap: 8rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.dot {
  width: 12rpx;
  height: 12rpx;
  background: #9CA3AF;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out;
}

.dot:nth-child(1) { animation-delay: 0s; }
.dot:nth-child(2) { animation-delay: 0.2s; }
.dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0.8); opacity: 0.5; }
  40% { transform: scale(1); opacity: 1; }
}

.chat-input-section {
  background: #fff;
  padding: 20rpx 32rpx 32rpx;
  border-top: 1rpx solid #E5E7EB;
}

.quick-prompts {
  display: flex;
  gap: 16rpx;
  margin-bottom: 20rpx;
  overflow-x: auto;
  flex-wrap: nowrap;
}

.prompt-chip {
  background: linear-gradient(135deg, #EEF2FF 0%, #DBEAFE 100%);
  padding: 16rpx 24rpx;
  border-radius: 40rpx;
  flex-shrink: 0;
}

.prompt-chip text {
  font-size: 24rpx;
  color: #3B82F6;
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 16rpx;
}

.input-field {
  flex: 1;
  background: #F3F4F6;
  border-radius: 24rpx;
  padding: 20rpx 24rpx;
  font-size: 28rpx;
  max-height: 200rpx;
  min-height: 80rpx;
}

.send-btn {
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  color: #fff;
  border-radius: 40rpx;
  padding: 20rpx 32rpx;
  font-size: 26rpx;
  border: none;
}

.send-btn[disabled] {
  opacity: 0.5;
}
</style>
