<template>
  <view class="growth-page">
    <view class="page-header">
      <text class="page-title">成长记录</text>
      <button class="add-btn" @click="goToAdd">+ 添加记录</button>
    </view>

    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-value">{{ stats.sleepAvg }}</text>
        <text class="stats-label">平均睡眠(小时)</text>
      </view>
      <view class="stats-item">
        <text class="stats-value">{{ stats.dietCount }}</text>
        <text class="stats-label">饮食记录</text>
      </view>
      <view class="stats-item">
        <text class="stats-value">{{ stats.learningHours }}</text>
        <text class="stats-label">学习时长(小时)</text>
      </view>
    </view>

    <view class="filter-bar">
      <view class="filter-item" :class="{ active: currentType === 'all' }" @click="switchType('all')">全部</view>
      <view class="filter-item" :class="{ active: currentType === 'sleep' }" @click="switchType('sleep')">睡眠</view>
      <view class="filter-item" :class="{ active: currentType === 'diet' }" @click="switchType('diet')">饮食</view>
      <view class="filter-item" :class="{ active: currentType === 'learning' }" @click="switchType('learning')">学习</view>
    </view>

    <scroll-view scroll-y class="record-list">
      <view v-for="record in records" :key="record.id" class="record-item" @click="goToDetail(record.id)">
        <view class="record-icon" :class="record.type">
          <text>{{ record.type === 'sleep' ? '😴' : record.type === 'diet' ? '🍽️' : '📚' }}</text>
        </view>
        <view class="record-info">
          <text class="record-type">{{ getTypeName(record.type) }}</text>
          <text class="record-content">{{ getRecordContent(record) }}</text>
          <text class="record-date">{{ formatDate(record.recordDate) }}</text>
        </view>
      </view>

      <view v-if="records.length === 0" class="empty-state">
        <text class="empty-icon">📊</text>
        <text class="empty-text">暂无记录</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getGrowthRecords, getGrowthStats } from '@/api/growth'
import type { GrowthRecord, GrowthStatsData, RecordType } from '@/api/growth'
import { formatDate } from '@/utils'

const userStore = useUserStore()
const records = ref<GrowthRecord[]>([])
const stats = ref<GrowthStatsData>({ sleepAvg: 0, dietCount: 0, learningHours: 0, records: [] })
const currentType = ref<RecordType | 'all'>('all')

const getTypeName = (type: RecordType) => ({ sleep: '睡眠', diet: '饮食', learning: '学习' }[type])
const getRecordContent = (record: GrowthRecord) => {
  if (record.type === 'sleep') return `睡眠 ${record.sleepDuration} 小时`
  if (record.type === 'diet') return `${record.mealType}：${record.foodDescription}`
  return `学习 ${record.learningDuration} 分钟`
}
const switchType = async (type: RecordType | 'all') => {
  currentType.value = type
  await loadRecords()
}
const loadRecords = async () => {
  if (!userStore.currentChildId) return
  const res = await getGrowthRecords(userStore.currentChildId, { type: currentType.value === 'all' ? undefined : currentType.value as RecordType })
  if (res.code === 200) records.value = res.data.list
}
const loadStats = async () => {
  if (!userStore.currentChildId) return
  const res = await getGrowthStats(userStore.currentChildId)
  if (res.code === 200) stats.value = res.data
}
const goToAdd = () => uni.navigateTo({ url: '/pages/growth/add' })
const goToDetail = (id: number) => {}

onMounted(async () => {
  if (!userStore.isLoggedIn) { uni.reLaunch({ url: '/pages/user/login' }); return }
  await Promise.all([loadRecords(), loadStats()])
})
</script>

<style lang="scss" scoped>
.growth-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24rpx; }
.page-title { font-size: 40rpx; font-weight: bold; color: #1F2937; }
.add-btn { padding: 12rpx 24rpx; background: #3B82F6; color: #fff; border-radius: 40rpx; font-size: 26rpx; }
.stats-card { background: #fff; border-radius: 24rpx; padding: 32rpx; display: flex; justify-content: space-around; margin-bottom: 24rpx; }
.stats-item { text-align: center; }
.stats-value { font-size: 40rpx; font-weight: bold; color: #3B82F6; display: block; }
.stats-label { font-size: 24rpx; color: #6B7280; }
.filter-bar { display: flex; gap: 16rpx; margin-bottom: 24rpx; }
.filter-item { padding: 12rpx 24rpx; background: #fff; border-radius: 40rpx; font-size: 26rpx; color: #6B7280; }
.filter-item.active { background: #3B82F6; color: #fff; }
.record-list { height: calc(100vh - 380rpx); }
.record-item { background: #fff; border-radius: 20rpx; padding: 24rpx; margin-bottom: 20rpx; display: flex; }
.record-icon { width: 80rpx; height: 80rpx; border-radius: 20rpx; background: #F3F4F6; display: flex; align-items: center; justify-content: center; margin-right: 20rpx; font-size: 40rpx; }
.record-icon.sleep { background: rgba(139,92,246,0.1); }
.record-icon.diet { background: rgba(245,158,11,0.1); }
.record-icon.learning { background: rgba(59,130,246,0.1); }
.record-info { flex: 1; }
.record-type { font-size: 28rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 8rpx; }
.record-content { font-size: 26rpx; color: #374151; display: block; margin-bottom: 8rpx; }
.record-date { font-size: 24rpx; color: #9CA3AF; }
.empty-state { text-align: center; padding: 100rpx 0; }
.empty-icon { font-size: 100rpx; display: block; margin-bottom: 24rpx; }
.empty-text { font-size: 28rpx; color: #6B7280; }
</style>
