<template>
  <view class="helper-page">
    <view class="page-header">
      <text class="page-title">家长助手</text>
      <text class="page-desc">AI 育儿问答</text>
    </view>

    <scroll-view scroll-y class="chat-list" :scroll-top="scrollTop">
      <view v-if="messages.length === 0" class="empty-chat">
        <text class="empty-icon">💬</text>
        <text class="empty-text">有什么育儿问题可以问我哦</text>
      </view>

      <view v-for="(msg, index) in messages" :key="index" class="message-item" :class="msg.role">
        <view class="message-avatar">
          <text>{{ msg.role === 'user' ? '👨‍👩‍👧' : '🤖' }}</text>
        </view>
        <view class="message-content">
          <text class="message-text">{{ msg.content }}</text>
          <view v-if="msg.role === 'assistant' && msg.suggestions?.length" class="suggestions">
            <text class="suggestion-title">建议：</text>
            <text v-for="s in msg.suggestions" :key="s" class="suggestion-item">{{ s }}</text>
          </view>
        </view>
      </view>

      <view v-if="isTyping" class="message-item assistant">
        <view class="message-avatar"><text>🤖</text></view>
        <view class="message-content typing">
          <text class="typing-text">AI 正在思考...</text>
        </view>
      </view>
    </scroll-view>

    <view class="input-area">
      <input v-model="question" placeholder="输入您的育儿问题..." class="chat-input" confirm-type="send" @confirm="handleSend" />
      <button class="send-btn" :disabled="!question.trim() || isTyping" @click="handleSend">发送</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { submitQuestion } from '@/api/helper'
import { showError } from '@/utils'

const userStore = useUserStore()
const question = ref('')
const messages = ref<{ role: 'user' | 'assistant'; content: string; suggestions?: string[] }[]>([])
const isTyping = ref(false)
const scrollTop = ref(0)

const handleSend = async () => {
  if (!question.value.trim() || isTyping.value) return
  if (!userStore.currentChildId) { showError('请先选择孩子'); return }

  const q = question.value.trim()
  messages.value.push({ role: 'user', content: q })
  question.value = ''
  isTyping.value = true
  scrollTop.value++

  try {
    const res = await submitQuestion({ childId: userStore.currentChildId, question: q })
    isTyping.value = false
    if (res.code === 200) {
      messages.value.push({
        role: 'assistant',
        content: res.data.answer.answer,
        suggestions: res.data.answer.actionPlans,
      })
    } else {
      messages.value.push({ role: 'assistant', content: '抱歉，我暂时无法回答这个问题。' })
    }
  } catch {
    isTyping.value = false
    messages.value.push({ role: 'assistant', content: '网络错误，请重试。' })
  }
  scrollTop.value++
}
</script>

<style lang="scss" scoped>
.helper-page { display: flex; flex-direction: column; height: 100vh; background: #F8F8F8; }
.page-header { padding: 32rpx; background: #fff; }
.page-title { font-size: 40rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 8rpx; }
.page-desc { font-size: 28rpx; color: #6B7280; }
.chat-list { flex: 1; padding: 32rpx; }
.empty-chat { text-align: center; padding: 100rpx 0; }
.empty-icon { font-size: 100rpx; display: block; margin-bottom: 24rpx; }
.empty-text { font-size: 28rpx; color: #6B7280; }
.message-item { display: flex; margin-bottom: 32rpx; }
.message-item.user { flex-direction: row-reverse; }
.message-avatar { width: 80rpx; height: 80rpx; border-radius: 50%; background: #F3F4F6; display: flex; align-items: center; justify-content: center; font-size: 40rpx; flex-shrink: 0; margin: 0 16rpx; }
.message-content { max-width: 70%; background: #fff; border-radius: 20rpx; padding: 20rpx 24rpx; }
.message-item.user .message-content { background: #3B82F6; }
.message-text { font-size: 28rpx; color: #1F2937; line-height: 1.6; }
.message-item.user .message-text { color: #fff; }
.suggestions { margin-top: 16rpx; padding-top: 16rpx; border-top: 1px solid #E5E7EB; }
.suggestion-title { font-size: 24rpx; color: #6B7280; display: block; margin-bottom: 8rpx; }
.suggestion-item { font-size: 24rpx; color: #3B82F6; display: block; margin-bottom: 4rpx; }
.typing-text { color: #9CA3AF; font-size: 28rpx; }
.input-area { display: flex; padding: 24rpx 32rpx; background: #fff; padding-bottom: calc(24rpx + env(safe-area-inset-bottom)); }
.chat-input { flex: 1; height: 88rpx; background: #F3F4F6; border-radius: 44rpx; padding: 0 32rpx; font-size: 28rpx; }
.send-btn { margin-left: 20rpx; width: 120rpx; height: 88rpx; background: #3B82F6; color: #fff; border-radius: 44rpx; font-size: 28rpx; display: flex; align-items: center; justify-content: center; }
.send-btn[disabled] { background: #D1D5DB; }
</style>
