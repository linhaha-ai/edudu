<template>
  <view class="report-page">
    <view class="page-header">
      <text class="page-title">成长报告</text>
      <button class="generate-btn" @click="handleGenerate">生成报告</button>
    </view>
    <view class="report-content" v-if="report">
      <view class="report-section">
        <text class="section-title">报告周期</text>
        <text class="report-period">{{ report.startDate }} - {{ report.endDate }}</text>
      </view>
      <view class="report-section">
        <text class="section-title">AI 分析</text>
        <text class="report-text">{{ report.content }}</text>
      </view>
      <view class="report-section">
        <text class="section-title">建议</text>
        <view v-for="(s, i) in report.aiSuggestions" :key="i" class="suggestion-item">
          <text>{{ s }}</text>
        </view>
      </view>
    </view>
    <view class="empty-state" v-else>
      <text class="empty-icon">📝</text>
      <text class="empty-text">暂无报告</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { generateGrowthReport } from '@/api/growth'
import { showSuccess, showError, showLoading, hideLoading } from '@/utils'
import type { GrowthReport } from '@/api/growth'

const userStore = useUserStore()
const report = ref<GrowthReport | null>(null)

const handleGenerate = async () => {
  if (!userStore.currentChildId) return
  showLoading('生成中...')
  try {
    const res = await generateGrowthReport(userStore.currentChildId, 'week')
    hideLoading()
    if (res.code === 200) report.value = res.data
    else showError('生成失败')
  } catch { hideLoading(); showError('生成失败') }
}
</script>

<style lang="scss" scoped>
.report-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24rpx; }
.page-title { font-size: 40rpx; font-weight: bold; color: #1F2937; }
.generate-btn { padding: 12rpx 24rpx; background: #3B82F6; color: #fff; border-radius: 40rpx; font-size: 26rpx; }
.report-section { background: #fff; border-radius: 20rpx; padding: 24rpx; margin-bottom: 20rpx; }
.section-title { font-size: 28rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 12rpx; }
.report-period { font-size: 26rpx; color: #6B7280; }
.report-text { font-size: 28rpx; color: #374151; line-height: 1.8; }
.suggestion-item { padding: 12rpx 0; border-bottom: 1px solid #F3F4F6; font-size: 28rpx; color: #374151; }
.suggestion-item:last-child { border-bottom: none; }
.empty-state { text-align: center; padding: 100rpx 0; }
.empty-icon { font-size: 100rpx; display: block; margin-bottom: 24rpx; }
.empty-text { font-size: 28rpx; color: #6B7280; }
</style>
