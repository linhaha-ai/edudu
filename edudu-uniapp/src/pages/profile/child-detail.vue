
<template>
  <view class="child-detail-page">
    <view class="header-section">
      <view class="child-avatar-large">
        <image v-if="child?.avatar" :src="child.avatar" />
        <text v-else>{{ child?.name?.charAt(0) || '宝' }}</text>
      </view>
      <text class="child-name">{{ child?.name }}</text>
      <text class="child-age">{{ calculateAge(child?.birthDate) }}岁 · {{ getGenderText(child?.gender) }}</text>
    </view>

    <view class="stats-grid">
      <view class="stat-item">
        <text class="stat-value">{{ stats.totalStories }}</text>
        <text class="stat-label">AI故事</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ stats.completedTasks }}</text>
        <text class="stat-label">完成任务</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ stats.streakDays }}</text>
        <text class="stat-label">连续天数</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ stats.totalConversations }}</text>
        <text class="stat-label">AI对话</text>
      </view>
    </view>

    <view class="quick-actions">
      <view class="action-card" @click="goToCreateStory">
        <text class="action-icon">📖</text>
        <text class="action-title">生成故事</text>
      </view>
      <view class="action-card" @click="goToTasks">
        <text class="action-icon">📋</text>
        <text class="action-title">今日任务</text>
      </view>
      <view class="action-card" @click="goToAiChat">
        <text class="action-icon">💬</text>
        <text class="action-title">AI对话</text>
      </view>
      <view class="action-card" @click="goToEdit">
        <text class="action-icon">✏️</text>
        <text class="action-title">编辑资料</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">最近故事</text>
        <text class="section-more" @click="goToStories">查看全部 ›</text>
      </view>
      <view class="story-list">
        <view v-for="story in recentStories" :key="story.id" class="story-item" @click="goToStoryDetail(story.id)">
          <text class="story-emoji">{{ story.theme === 'bedtime' ? '🌙' : story.theme === 'emotion' ? '🌈' : '📖' }}</text>
          <view class="story-content">
            <text class="story-title">{{ story.title }}</text>
            <text class="story-time">{{ formatTime(story.createdAt) }}</text>
          </view>
        </view>
        <view v-if="recentStories.length === 0" class="empty-mini">
          <text class="empty-text">还没有故事</text>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">AI记忆摘要</text>
        <text class="section-more" @click="goToMemories">管理记忆 ›</text>
      </view>
      <view class="memory-card">
        <text v-if="memorySummary" class="memory-content">{{ memorySummary }}</text>
        <text v-else class="memory-empty">暂无记忆，开始与AI对话吧~</text>
      </view>
    </view>

    <view class="bottom-safe"></view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { calculateAge } from '@/utils'

// 常量定义
const MAX_RECENT_DAYS = 7

// 接口定义
interface StatsData {
  totalStories: number
  completedTasks: number
  streakDays: number
  totalConversations: number
}

interface StoryItem {
  id: number
  title: string
  theme: string
  createdAt: string
}

const userStore = useUserStore()
const childStore = useChildStore()

const stats = ref({
  totalStories: 0,
  completedTasks: 0,
  streakDays: 0,
  totalConversations: 0
})

const recentStories = ref<StoryItem[]>([])
const memorySummary = ref('')

const child = computed(() => {
  if (!userStore.currentChildId) return null
  return childStore.children.find(c => c.id === userStore.currentChildId)
})

const getGenderText = (gender?: string) => {
  if (gender === 'male') return '男孩'
  if (gender === 'female') return '女孩'
  return '未知'
}

const formatTime = (time?: string) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(hours / 24)
  if (days > MAX_RECENT_DAYS) return date.toLocaleDateString()
  if (days > 0) return `${days}天前`
  if (hours > 0) return `${hours}小时前`
  return '刚刚'
}

const goToCreateStory = () => uni.navigateTo({ url: '/pages/story/create' })
const goToTasks = () => uni.switchTab({ url: '/pages/task/index' })
const goToAiChat = () => uni.navigateTo({ url: '/pages/ai/chat' })
const goToEdit = () => {
  if (child.value) {
    uni.navigateTo({ url: `/pages/profile/children-edit?id=${child.value.id}` })
  }
}
const goToStories = () => uni.navigateTo({ url: '/pages/story/list' })
const goToStoryDetail = (id: number) => uni.navigateTo({ url: `/pages/story/detail?id=${id}` })
const goToMemories = () => uni.navigateTo({ url: '/pages/ai/memories' })

onMounted(() => {
  if (!userStore.isLoggedIn) {
    uni.reLaunch({ url: '/pages/user/login' })
    return
  }
  childStore.fetchChildren()
  // 模拟加载统计数据
  stats.value = {
    totalStories: 12,
    completedTasks: 45,
    streakDays: 7,
    totalConversations: 23
  }
  // 模拟最近故事
  recentStories.value = [
    { id: 1, title: '小兔子的冒险', theme: 'bedtime', createdAt: new Date(Date.now() - 3600000).toISOString() },
    { id: 2, title: '分享的快乐', theme: 'emotion', createdAt: new Date(Date.now() - 86400000).toISOString() }
  ]
  memorySummary.value = '孩子喜欢小兔子和公主的故事，对颜色和数字很感兴趣，最近在学习分享和情绪管理。'
})
</script>

<style lang="scss" scoped>
.child-detail-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #EEF2FF 0%, #F8F8F8 50%);
}

.header-section {
  padding: 80rpx 32rpx 48rpx;
  text-align: center;
}

.child-avatar-large {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24rpx;
  border: 6rpx solid #fff;
  box-shadow: 0 8rpx 24rpx rgba(59, 130, 246, 0.2);
}

.child-avatar-large image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.child-avatar-large text {
  color: #fff;
  font-size: 56rpx;
  font-weight: bold;
}

.child-name {
  font-size: 40rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 12rpx;
}

.child-age {
  font-size: 28rpx;
  color: #6B7280;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  padding: 32rpx;
  background: #fff;
  margin: 0 32rpx 24rpx;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 40rpx;
  font-weight: bold;
  color: #3B82F6;
  display: block;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #6B7280;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  padding: 0 32rpx 24rpx;
  gap: 16rpx;
}

.action-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 32rpx 16rpx;
  text-align: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.action-icon {
  font-size: 40rpx;
  display: block;
  margin-bottom: 12rpx;
}

.action-title {
  font-size: 24rpx;
  color: #1F2937;
}

.section {
  margin: 0 32rpx 24rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
}

.section-more {
  font-size: 26rpx;
  color: #6B7280;
}

.story-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F3F4F6;
}

.story-item:last-child {
  border-bottom: none;
}

.story-emoji {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.story-content {
  flex: 1;
}

.story-title {
  font-size: 28rpx;
  color: #1F2937;
  display: block;
  margin-bottom: 6rpx;
}

.story-time {
  font-size: 24rpx;
  color: #9CA3AF;
}

.empty-mini {
  text-align: center;
  padding: 40rpx 0;
}

.empty-text {
  font-size: 26rpx;
  color: #9CA3AF;
}

.memory-card {
  background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
  border-radius: 16rpx;
  padding: 24rpx;
}

.memory-content {
  font-size: 26rpx;
  color: #92400E;
  line-height: 1.8;
}

.memory-empty {
  font-size: 26rpx;
  color: #B45309;
}

.bottom-safe {
  height: 120rpx;
}
</style>
