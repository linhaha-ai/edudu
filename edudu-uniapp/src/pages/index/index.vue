<template>
  <view class="index-container">
    <!-- 顶部欢迎区域 -->
    <view class="header">
      <view class="header-left">
        <text class="greeting">{{ greeting }}</text>
        <view class="child-info" @click="goToChildren">
          <image
            v-if="currentChild?.avatar"
            :src="currentChild.avatar"
            class="child-avatar"
          />
          <view v-else class="child-avatar-placeholder">
            <text>{{ currentChild?.name?.charAt(0) || '宝' }}</text>
          </view>
          <text class="child-name">{{ currentChild?.name || '添加孩子' }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
      <view class="header-right" @click="goToProfile">
        <image
          v-if="userStore.avatar"
          :src="userStore.avatar"
          class="user-avatar"
        />
        <view v-else class="user-avatar-placeholder">
          <text>{{ userStore.nickname?.charAt(0) || '我' }}</text>
        </view>
      </view>
    </view>

    <!-- 今日任务进度 -->
    <view class="today-progress" @click="goToTasks">
      <view class="progress-left">
        <text class="progress-title">今日任务</text>
        <text class="progress-desc">{{ todayProgress.completed }}/{{ todayProgress.total }} 已完成</text>
      </view>
      <view class="progress-right">
        <view class="progress-circle">
          <view class="progress-text">
            <text class="percent">{{ progressPercent }}%</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 快捷故事入口 -->
    <view class="quick-story-section">
      <text class="section-title">快捷入口</text>
      <view class="quick-story-grid">
        <view class="quick-story-card bedtime" @click="goToBedtime">
          <text class="quick-story-icon">🌙</text>
          <view class="quick-story-content">
            <text class="quick-story-title">睡前故事</text>
            <text class="quick-story-desc">温馨哄睡</text>
          </view>
          <text class="quick-story-arrow">›</text>
        </view>
        <view class="quick-story-card emotion" @click="goToEmotion">
          <text class="quick-story-icon">🌈</text>
          <view class="quick-story-content">
            <text class="quick-story-title">情绪引导</text>
            <text class="quick-story-desc">情绪管理</text>
          </view>
          <text class="quick-story-arrow">›</text>
        </view>
        <view class="quick-story-card generate" @click="goToGenerateTask">
          <text class="quick-story-icon">🎯</text>
          <view class="quick-story-content">
            <text class="quick-story-title">自定义任务</text>
            <text class="quick-story-desc">生成专属早教</text>
          </view>
          <text class="quick-story-arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 功能入口卡片 -->
    <view class="feature-section">
      <text class="section-title">核心功能</text>
      <view class="feature-grid">
        <view class="feature-card" @click="goToStory">
          <view class="feature-icon story-icon">
            <text class="icon">📖</text>
          </view>
          <text class="feature-title">AI讲故事</text>
          <text class="feature-desc">为孩子定制专属故事</text>
        </view>

        <view class="feature-card" @click="goToTasks">
          <view class="feature-icon task-icon">
            <text class="icon">📋</text>
          </view>
          <text class="feature-title">每日任务</text>
          <text class="feature-desc">科学早教每一天</text>
        </view>

        <view class="feature-card" @click="goToGrowth">
          <view class="feature-icon growth-icon">
            <text class="icon">📊</text>
          </view>
          <text class="feature-title">成长记录</text>
          <text class="feature-desc">记录每一步成长</text>
        </view>

        <view class="feature-card" @click="goToHelper">
          <view class="feature-icon helper-icon">
            <text class="icon">💬</text>
          </view>
          <text class="feature-title">家长助手</text>
          <text class="feature-desc">AI育儿问答</text>
        </view>
      </view>
    </view>

    <!-- 快捷操作 -->
    <view class="quick-actions">
      <view class="action-item" @click="goToStory">
        <view class="action-content">
          <text class="action-title">快速讲故事</text>
          <text class="action-desc">输入场景即可生成</text>
        </view>
        <view class="action-arrow">›</view>
      </view>
    </view>

    <!-- 底部占位 -->
    <view class="bottom-safe"></view>
  </view>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { getTodayTasks } from '@/api/task'
import type { DailyTask } from '@/api/task'

const userStore = useUserStore()
const childStore = useChildStore()

// 今日任务数据
const todayTasks = ref<DailyTask[]>([])

// 当前孩子
const currentChild = computed(() => {
  if (!userStore.currentChildId) return null
  return childStore.children.find((c) => c.id === userStore.currentChildId)
})

// 问候语
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return '早上好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

// 今日进度
const todayProgress = computed(() => {
  const total = todayTasks.value.length
  const completed = todayTasks.value.filter((t) => t.isCompleted).length
  return { total, completed }
})

// 进度百分比
const progressPercent = computed(() => {
  if (todayProgress.value.total === 0) return 0
  return Math.round((todayProgress.value.completed / todayProgress.value.total) * 100)
})

// 加载今日任务
const loadTodayTasks = async () => {
  if (!userStore.currentChildId) return
  try {
    const res = await getTodayTasks(userStore.currentChildId)
    if (res.code === 200) {
      todayTasks.value = res.data
    }
  } catch (e) {
    console.error('加载任务失败', e)
  }
}

// 页面跳转
const goToChildren = () => {
  uni.navigateTo({ url: '/pages/profile/children' })
}

const goToProfile = () => {
  uni.switchTab({ url: '/pages/profile/index' })
}

const goToStory = () => {
  uni.navigateTo({ url: '/pages/story/create' })
}

const goToBedtime = () => {
  uni.navigateTo({ url: '/pages/story/bedtime' })
}

const goToEmotion = () => {
  uni.navigateTo({ url: '/pages/story/emotion' })
}

const goToGenerateTask = () => {
  uni.navigateTo({ url: '/pages/task/generate' })
}

const goToTasks = () => {
  uni.switchTab({ url: '/pages/task/index' })
}

const goToGrowth = () => {
  uni.switchTab({ url: '/pages/growth/index' })
}

const goToHelper = () => {
  uni.navigateTo({ url: '/pages/helper/index' })
}

onMounted(async () => {
  // 检查登录
  if (!userStore.isLoggedIn) {
    uni.reLaunch({ url: '/pages/user/login' })
    return
  }

  // 加载孩子列表
  await childStore.fetchChildren()

  // 如果没有设置当前孩子，设置为第一个
  if (!userStore.currentChildId && childStore.children.length > 0) {
    userStore.setCurrentChildId(childStore.children[0].id)
  }

  // 加载今日任务
  await loadTodayTasks()
})
</script>

<style lang="scss" scoped>
.index-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #EEF2FF 0%, #FFFFFF 50%);
  padding: 0 32rpx;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding-top: 120rpx;
  margin-bottom: 40rpx;
}

.header-left {
  flex: 1;
}

.greeting {
  font-size: 40rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 16rpx;
}

.child-info {
  display: flex;
  align-items: center;
  padding: 12rpx 20rpx;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 40rpx;
  width: fit-content;
}

.child-avatar {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  margin-right: 12rpx;
}

.child-avatar-placeholder {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  margin-right: 12rpx;
}

.child-name {
  font-size: 28rpx;
  color: #1F2937;
  margin-right: 8rpx;
}

.arrow {
  font-size: 32rpx;
  color: #9CA3AF;
}

.header-right {
  padding-top: 8rpx;
}

.user-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  border: 4rpx solid #fff;
}

.user-avatar-placeholder {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #F59E0B, #F97316);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  border: 4rpx solid #fff;
}

.today-progress {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.progress-left {
  flex: 1;
}

.progress-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 8rpx;
}

.progress-desc {
  font-size: 26rpx;
  color: #6B7280;
}

.progress-circle {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: conic-gradient(#3B82F6 0deg, #E5E7EB 0deg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.progress-text {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.percent {
  font-size: 28rpx;
  font-weight: bold;
  color: #3B82F6;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
  margin-bottom: 32rpx;
}

.feature-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

  &:active {
    transform: scale(0.98);
  }
}

.feature-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;

  .icon {
    font-size: 40rpx;
  }
}

.story-icon { background: rgba(59, 130, 246, 0.1); }
.task-icon { background: rgba(16, 185, 129, 0.1); }
.growth-icon { background: rgba(245, 158, 11, 0.1); }
.helper-icon { background: rgba(139, 92, 246, 0.1); }

.feature-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 8rpx;
}

.feature-desc {
  font-size: 24rpx;
  color: #6B7280;
}

.quick-actions {
  background: #fff;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.action-item {
  display: flex;
  align-items: center;
  padding: 32rpx;
}

.action-content {
  flex: 1;
}

.action-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 8rpx;
}

.action-desc {
  font-size: 24rpx;
  color: #6B7280;
}

.action-arrow {
  font-size: 40rpx;
  color: #9CA3AF;
}

.bottom-safe {
  height: 120rpx;
}
</style>
