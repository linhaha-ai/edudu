<template>
  <view class="children-page">
    <view class="page-header">
      <text class="page-title">孩子管理</text>
      <button class="add-btn" @click="goToAdd">+ 添加孩子</button>
    </view>

    <view class="children-list">
      <view v-for="child in childStore.children" :key="child.id" class="child-item" :class="{ active: userStore.currentChildId === child.id }" @click="selectChild(child.id)">
        <view class="child-avatar">
          <image v-if="child.avatar" :src="child.avatar" />
          <text v-else>{{ child.name?.charAt(0) || '宝' }}</text>
        </view>
        <view class="child-info">
          <text class="child-name">{{ child.name }}</text>
          <text class="child-age">{{ calculateAge(child.birthDate) }}岁</text>
        </view>
        <view class="child-actions">
          <text v-if="userStore.currentChildId === child.id" class="current-tag">当前</text>
          <text class="edit-btn" @click.stop="goToEdit(child.id)">编辑</text>
          <text class="delete-btn" @click.stop="deleteChild(child.id)">删除</text>
        </view>
      </view>

      <view v-if="childStore.children.length === 0" class="empty-state">
        <text class="empty-icon">👶</text>
        <text class="empty-text">还没有添加孩子</text>
        <button class="btn btn-primary" @click="goToAdd">添加孩子</button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { calculateAge } from '@/utils'
import { showSuccess, showConfirm } from '@/utils'

const userStore = useUserStore()
const childStore = useChildStore()

const selectChild = (id: number) => {
  userStore.setCurrentChildId(id)
  showSuccess('已切换')
}

const goToAdd = () => uni.navigateTo({ url: '/pages/profile/children-edit' })
const goToEdit = (id: number) => uni.navigateTo({ url: `/pages/profile/children-edit?id=${id}` })

const deleteChild = async (id: number) => {
  const confirm = await showConfirm('提示', '确定要删除该孩子吗？')
  if (confirm) {
    await childStore.deleteChildById(id)
    if (userStore.currentChildId === id) {
      userStore.setCurrentChildId(childStore.children[0]?.id || null)
    }
    showSuccess('删除成功')
  }
}

onMounted(() => {
  if (!userStore.isLoggedIn) { uni.reLaunch({ url: '/pages/user/login' }); return }
  childStore.fetchChildren()
})
</script>

<style lang="scss" scoped>
.children-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 32rpx; }
.page-title { font-size: 40rpx; font-weight: bold; color: #1F2937; }
.add-btn { padding: 12rpx 24rpx; background: #3B82F6; color: #fff; border-radius: 40rpx; font-size: 26rpx; }
.child-item { background: #fff; border-radius: 20rpx; padding: 24rpx; margin-bottom: 20rpx; display: flex; align-items: center; }
.child-item.active { border: 2rpx solid #3B82F6; }
.child-avatar { width: 100rpx; height: 100rpx; border-radius: 50%; background: linear-gradient(135deg, #3B82F6, #6366F1); display: flex; align-items: center; justify-content: center; margin-right: 20rpx; }
.child-avatar image { width: 100%; height: 100%; border-radius: 50%; }
.child-avatar text { color: #fff; font-size: 36rpx; }
.child-info { flex: 1; }
.child-name { font-size: 32rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 8rpx; }
.child-age { font-size: 26rpx; color: #6B7280; }
.child-actions { display: flex; align-items: center; gap: 16rpx; }
.current-tag { padding: 8rpx 16rpx; background: #3B82F6; color: #fff; border-radius: 20rpx; font-size: 22rpx; }
.edit-btn, .delete-btn { font-size: 26rpx; }
.edit-btn { color: #3B82F6; }
.delete-btn { color: #EF4444; }
.empty-state { text-align: center; padding: 100rpx 0; }
.empty-icon { font-size: 100rpx; display: block; margin-bottom: 24rpx; }
.empty-text { font-size: 28rpx; color: #6B7280; margin-bottom: 32rpx; }
</style>
