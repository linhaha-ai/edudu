<template>
  <view class="history-page">
    <view class="page-header">
      <text class="page-title">提问历史</text>
    </view>
    <view class="history-list" v-if="history.length > 0">
      <view v-for="item in history" :key="item.id" class="history-item" @click="goToDetail(item.id)">
        <text class="question">{{ item.question }}</text>
        <text class="answer">{{ item.answer?.answer?.slice(0, 50) }}...</text>
        <text class="date">{{ formatDate(item.createdAt) }}</text>
      </view>
    </view>
    <view class="empty-state" v-else>
      <text class="empty-icon">💬</text>
      <text class="empty-text">暂无提问记录</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getQuestionHistory } from '@/api/helper'
import { formatDate } from '@/utils'

const userStore = useUserStore()
const history = ref<any[]>([])

const loadHistory = async () => {
  const res = await getQuestionHistory()
  if (res.code === 200) history.value = res.data.list
}

const goToDetail = (id: number) => {}

onMounted(() => {
  if (!userStore.isLoggedIn) { uni.reLaunch({ url: '/pages/user/login' }); return }
  loadHistory()
})
</script>

<style lang="scss" scoped>
.history-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.page-header { margin-bottom: 24rpx; }
.page-title { font-size: 40rpx; font-weight: bold; color: #1F2937; }
.history-list { }
.history-item { background: #fff; border-radius: 20rpx; padding: 24rpx; margin-bottom: 20rpx; }
.question { font-size: 30rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 12rpx; }
.answer { font-size: 26rpx; color: #6B7280; display: block; margin-bottom: 12rpx; }
.date { font-size: 24rpx; color: #9CA3AF; }
.empty-state { text-align: center; padding: 100rpx 0; }
.empty-icon { font-size: 100rpx; display: block; margin-bottom: 24rpx; }
.empty-text { font-size: 28rpx; color: #6B7280; }
</style>
