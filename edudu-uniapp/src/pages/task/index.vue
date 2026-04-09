<template>
  <view class="task-page">
    <view class="page-header">
      <text class="page-title">每日任务</text>
      <text class="page-desc">科学早教每一天</text>
    </view>

    <!-- 任务统计 -->
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-value">{{ stats.todayCompleted }}</text>
        <text class="stats-label">今日完成</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-value">{{ stats.todayTotal }}</text>
        <text class="stats-label">任务总数</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-value">{{ stats.streakDays }}</text>
        <text class="stats-label">连续天数</text>
      </view>
    </view>

    <!-- 筛选标签 -->
    <view class="filter-bar">
      <view
        v-for="type in taskTypes"
        :key="type.value"
        class="filter-item"
        :class="{ active: currentType === type.value }"
        @click="switchType(type.value)"
      >
        {{ type.name }}
      </view>
    </view>

    <!-- 任务列表 -->
    <scroll-view scroll-y class="task-list" @scrolltolower="loadMore">
      <view v-for="task in tasks" :key="task.id" class="task-item" @click="goToDetail(task.id)">
        <view class="task-info">
          <view class="task-type-tag" :class="task.type">{{ getTypeName(task.type) }}</view>
          <text class="task-title">{{ task.title }}</text>
          <text class="task-duration">{{ task.duration }}分钟</text>
        </view>
        <view class="task-status">
          <view v-if="task.isCompleted" class="status-done">已完成 ✓</view>
          <view v-else class="status-undo">去完成</view>
        </view>
      </view>

      <view v-if="tasks.length === 0" class="empty-state">
        <text class="empty-icon">📋</text>
        <text class="empty-text">今日暂无任务</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getTodayTasks, getTaskStats } from '@/api/task'
import type { DailyTask, TaskStatsData } from '@/api/task'

const userStore = useUserStore()
const tasks = ref<DailyTask[]>([])
const stats = ref<TaskStatsData>({ todayTotal: 0, todayCompleted: 0, weekStats: [], typeStats: [], streakDays: 0 })
const currentType = ref('all')

const taskTypes = [
  { value: 'all', name: '全部' },
  { value: 'cognitive', name: '认知' },
  { value: 'language', name: '语言' },
  { value: 'motor', name: '运动' },
  { value: 'art', name: '艺术' },
]

const getTypeName = (type: string) => taskTypes.find(t => t.value === type)?.name || type

const switchType = (type: string) => {
  currentType.value = type
}

const loadTasks = async () => {
  if (!userStore.currentChildId) return
  const res = await getTodayTasks(userStore.currentChildId)
  if (res.code === 200) {
    tasks.value = res.data
  }
}

const loadStats = async () => {
  if (!userStore.currentChildId) return
  const res = await getTaskStats(userStore.currentChildId)
  if (res.code === 200) {
    stats.value = res.data
  }
}

const loadMore = () => {}

const goToDetail = (id: number) => {
  uni.navigateTo({ url: `/pages/task/detail?id=${id}` })
}

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    uni.reLaunch({ url: '/pages/user/login' })
    return
  }
  await Promise.all([loadTasks(), loadStats()])
})
</script>

<style lang="scss" scoped>
.task-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.page-header { margin-bottom: 24rpx; }
.page-title { font-size: 40rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 8rpx; }
.page-desc { font-size: 28rpx; color: #6B7280; }
.stats-card { background: linear-gradient(135deg, #3B82F6 0%, #6366F1 100%); border-radius: 24rpx; padding: 32rpx; display: flex; justify-content: space-around; margin-bottom: 24rpx; }
.stats-item { text-align: center; }
.stats-value { font-size: 48rpx; font-weight: bold; color: #fff; display: block; }
.stats-label { font-size: 24rpx; color: rgba(255,255,255,0.8); }
.stats-divider { width: 2rpx; background: rgba(255,255,255,0.3); }
.filter-bar { display: flex; gap: 16rpx; margin-bottom: 24rpx; flex-wrap: wrap; }
.filter-item { padding: 12rpx 24rpx; background: #fff; border-radius: 40rpx; font-size: 26rpx; color: #6B7280; }
.filter-item.active { background: #3B82F6; color: #fff; }
.task-list { height: calc(100vh - 400rpx); }
.task-item { background: #fff; border-radius: 20rpx; padding: 24rpx; margin-bottom: 20rpx; display: flex; justify-content: space-between; align-items: center; }
.task-info { flex: 1; }
.task-type-tag { display: inline-block; padding: 4rpx 16rpx; border-radius: 8rpx; font-size: 22rpx; margin-bottom: 8rpx; }
.task-type-tag.cognitive { background: rgba(59,130,246,0.1); color: #3B82F6; }
.task-type-tag.language { background: rgba(16,185,129,0.1); color: #10B981; }
.task-type-tag.motor { background: rgba(245,158,11,0.1); color: #F59E0B; }
.task-type-tag.art { background: rgba(139,92,246,0.1); color: #8B5CF6; }
.task-title { font-size: 30rpx; color: #1F2937; display: block; margin-bottom: 8rpx; }
.task-duration { font-size: 24rpx; color: #9CA3AF; }
.task-status { }
.status-done { padding: 12rpx 24rpx; background: #10B981; color: #fff; border-radius: 40rpx; font-size: 24rpx; }
.status-undo { padding: 12rpx 24rpx; background: #3B82F6; color: #fff; border-radius: 40rpx; font-size: 24rpx; }
.empty-state { text-align: center; padding: 100rpx 0; }
.empty-icon { font-size: 100rpx; display: block; margin-bottom: 24rpx; }
.empty-text { font-size: 28rpx; color: #6B7280; }
</style>
