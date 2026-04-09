<template>
  <view class="settings-page">
    <view class="settings-section">
      <view class="settings-item">
        <text class="settings-name">消息通知</text>
        <switch :checked="settings.notifications" @change="(e: any) => settings.notifications = e.detail.value" />
      </view>
      <view class="settings-item">
        <text class="settings-name">每日提醒</text>
        <switch :checked="settings.dailyReminder" @change="(e: any) => settings.dailyReminder = e.detail.value" />
      </view>
    </view>

    <view class="settings-section">
      <view class="settings-item" @click="clearCache">
        <text class="settings-name">清除缓存</text>
        <text class="settings-value">0 MB ›</text>
      </view>
      <view class="settings-item">
        <text class="settings-name">检查更新</text>
        <text class="settings-value">已是最新版本 ›</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { showSuccess, showError } from '@/utils'

const settings = reactive({
  notifications: true,
  dailyReminder: true,
})

const clearCache = () => {
  uni.showModal({
    title: '提示',
    content: '确定要清除缓存吗？',
    success: (res) => {
      if (res.confirm) {
        uni.clearStorageSync()
        showSuccess('缓存已清除')
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.settings-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.settings-section { background: #fff; border-radius: 20rpx; margin-bottom: 24rpx; }
.settings-item { display: flex; align-items: center; justify-content: space-between; padding: 32rpx; border-bottom: 1px solid #F3F4F6; }
.settings-item:last-child { border-bottom: none; }
.settings-name { font-size: 30rpx; color: #1F2937; }
.settings-value { font-size: 28rpx; color: #9CA3AF; }
</style>
