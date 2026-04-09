<template>
  <view class="profile-page">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="user-avatar-wrap">
        <image v-if="userStore.avatar" :src="userStore.avatar" class="user-avatar" />
        <view v-else class="user-avatar-placeholder">{{ userStore.nickname?.charAt(0) || '我' }}</view>
      </view>
      <text class="user-name">{{ userStore.nickname }}</text>
      <text class="user-phone">{{ maskPhone(userStore.userInfo?.phone || '') }}</text>
    </view>

    <!-- 菜单列表 -->
    <view class="menu-section">
      <view class="menu-item" @click="goToChildren">
        <text class="menu-icon">👶</text>
        <text class="menu-name">孩子管理</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToSettings">
        <text class="menu-icon">⚙️</text>
        <text class="menu-name">设置</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToAbout">
        <text class="menu-icon">ℹ️</text>
        <text class="menu-name">关于</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleLogout">
        <text class="menu-icon">🚪</text>
        <text class="menu-name logout">退出登录</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <view class="version-info">
      <text>版本 1.0.0</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { maskPhone } from '@/utils'
import { showConfirm } from '@/utils'

const userStore = useUserStore()

const goToChildren = () => uni.navigateTo({ url: '/pages/profile/children' })
const goToSettings = () => uni.navigateTo({ url: '/pages/profile/settings' })
const goToAbout = () => uni.navigateTo({ url: '/pages/profile/about' })

const handleLogout = async () => {
  const confirm = await showConfirm('提示', '确定要退出登录吗？')
  if (confirm) {
    await userStore.logoutAction()
    uni.reLaunch({ url: '/pages/user/login' })
  }
}
</script>

<style lang="scss" scoped>
.profile-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.user-card { background: linear-gradient(135deg, #3B82F6 0%, #6366F1 100%); border-radius: 24rpx; padding: 48rpx 32rpx; text-align: center; margin-bottom: 32rpx; }
.user-avatar-wrap { margin-bottom: 20rpx; }
.user-avatar { width: 120rpx; height: 120rpx; border-radius: 50%; border: 6rpx solid rgba(255,255,255,0.3); }
.user-avatar-placeholder { width: 120rpx; height: 120rpx; border-radius: 50%; background: rgba(255,255,255,0.2); color: #fff; font-size: 48rpx; display: flex; align-items: center; justify-content: center; margin: 0 auto; }
.user-name { font-size: 36rpx; font-weight: bold; color: #fff; display: block; margin-bottom: 8rpx; }
.user-phone { font-size: 26rpx; color: rgba(255,255,255,0.8); }
.menu-section { background: #fff; border-radius: 24rpx; overflow: hidden; margin-bottom: 32rpx; }
.menu-item { display: flex; align-items: center; padding: 32rpx; border-bottom: 1px solid #F3F4F6; }
.menu-item:last-child { border-bottom: none; }
.menu-icon { font-size: 36rpx; margin-right: 20rpx; }
.menu-name { flex: 1; font-size: 30rpx; color: #1F2937; }
.menu-name.logout { color: #EF4444; }
.menu-arrow { font-size: 32rpx; color: #D1D5DB; }
.version-info { text-align: center; padding: 32rpx; font-size: 24rpx; color: #9CA3AF; }
</style>
