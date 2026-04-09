<template>
  <view class="stats-page">
    <view class="page-header">
      <text class="page-title">任务统计</text>
    </view>
    <!-- 占位页面 -->
    <view class="empty-state">
      <text class="empty-icon">📊</text>
      <text class="empty-text">任务统计数据</text>
    </view>
  </view>
</template>

<script setup lang="ts">
</script>

<style lang="scss" scoped>
.stats-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.page-header { margin-bottom: 24rpx; }
.page-title { font-size: 40rpx; font-weight: bold; color: #1F2937; }
.empty-state { text-align: center; padding: 100rpx 0; }
.empty-icon { font-size: 100rpx; display: block; margin-bottom: 24rpx; }
.empty-text { font-size: 28rpx; color: #6B7280; }
</style>
