<template>
  <view class="story-detail">
    <!-- 故事内容 -->
    <view class="story-card">
      <view class="story-header">
        <text class="story-title">{{ story?.title || '故事加载中...' }}</text>
        <view class="story-meta">
          <text class="meta-item">{{ story?.storyType || '' }}</text>
          <text class="meta-item">{{ story?.age }}岁</text>
        </view>
      </view>

      <view class="story-content">
        <text class="content-text" v-if="story">{{ story.content }}</text>
        <text class="content-text" v-else>加载中...</text>
      </view>

      <!-- 操作按钮 -->
      <view class="story-actions">
        <view class="action-btn" @click="toggleFavorite">
          <text class="action-icon">{{ story?.isFavorite ? '❤️' : '🤍' }}</text>
          <text class="action-text">{{ story?.isFavorite ? '已收藏' : '收藏' }}</text>
        </view>
        <view class="action-btn" @click="shareStory">
          <text class="action-icon">📤</text>
          <text class="action-text">分享</text>
        </view>
      </view>
    </view>

    <!-- 语音播放控制 -->
    <view class="player-card">
      <view class="player-info">
        <text class="player-title">{{ isPlaying ? '正在播放' : '点击播放' }}</text>
        <text class="player-progress">{{ currentTime }} / {{ duration }}</text>
      </view>
      <view class="player-controls">
        <view class="control-btn replay-btn" @click="replay">
          <text>🔁</text>
        </view>
        <view class="control-btn play-btn" @click="togglePlay">
          <text>{{ isPlaying ? '⏸️' : '▶️' }}</text>
        </view>
        <view class="control-btn speed-btn" @click="changeSpeed">
          <text>{{ playSpeed }}x</text>
        </view>
      </view>
    </view>

    <!-- 相关推荐 -->
    <view class="recommend-section">
      <text class="section-title">更多故事</text>
      <view class="recommend-list">
        <view
          v-for="item in recommendations"
          :key="item.id"
          class="recommend-item"
          @click="goToDetail(item.id)"
        >
          <text class="recommend-title">{{ item.title }}</text>
          <text class="recommend-meta">{{ item.age }}岁 · {{ item.scene }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { onUnload } from '@dcloudio/uni-app'
import { getStoryDetail, favoriteStory, unfavoriteStory } from '@/api/story'
import { speakText, stopSpeak } from '@/utils'
import { showSuccess, showError } from '@/utils'

const storyId = ref<number>(0)
const story = ref<any>(null)
const recommendations = ref<any[]>([])
const isPlaying = ref(false)
const currentTime = ref('00:00')
const duration = ref('00:00')
const playSpeed = ref(1)

// 加载故事详情
const loadStory = async () => {
  const res = await getStoryDetail(storyId.value)
  if (res.code === 200) {
    story.value = res.data
  }
}

// 切换收藏
const toggleFavorite = async () => {
  if (!story.value) return
  const api = story.value.isFavorite ? unfavoriteStory : favoriteStory
  const res = await api(storyId.value)
  if (res.code === 200) {
    story.value.isFavorite = !story.value.isFavorite
    showSuccess(story.value.isFavorite ? '已收藏' : '已取消收藏')
  }
}

// 分享
const shareStory = () => {
  uni.showShareMenu({
    withShareTicket: true,
  })
}

// 播放控制
const togglePlay = () => {
  if (isPlaying.value) {
    stopSpeak()
    isPlaying.value = false
  } else {
    if (story.value) {
      speakText(story.value.content)
      isPlaying.value = true
    }
  }
}

const replay = () => {
  if (story.value) {
    stopSpeak()
    speakText(story.value.content)
    isPlaying.value = true
  }
}

const changeSpeed = () => {
  const speeds = [0.75, 1, 1.25, 1.5]
  const index = speeds.indexOf(playSpeed.value)
  playSpeed.value = speeds[(index + 1) % speeds.length]
}

const goToDetail = (id: number) => {
  uni.redirectTo({ url: `/pages/story/detail?id=${id}` })
}

onMounted(() => {
  uni.getSystemInfo({
    success: (info) => {
      const pages = getCurrentPages()
      const currentPage = pages[pages.length - 1]
      const options = (currentPage as any)?.options || {}
      storyId.value = parseInt(options.id)
      loadStory()
    },
  })
})

onUnload(() => {
  stopSpeak()
})
</script>

<style lang="scss" scoped>
.story-detail {
  min-height: 100vh;
  background: #F8F8F8;
  padding: 32rpx;
}

.story-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
}

.story-header {
  margin-bottom: 24rpx;
}

.story-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 12rpx;
}

.story-meta {
  display: flex;
  gap: 16rpx;
}

.meta-item {
  font-size: 24rpx;
  color: #6B7280;
  background: #F3F4F6;
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
}

.story-content {
  margin-bottom: 24rpx;
}

.content-text {
  font-size: 30rpx;
  color: #374151;
  line-height: 2;
  text-indent: 2em;
}

.story-actions {
  display: flex;
  gap: 48rpx;
  padding-top: 24rpx;
  border-top: 2rpx solid #F3F4F6;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.action-icon {
  font-size: 32rpx;
}

.action-text {
  font-size: 28rpx;
  color: #6B7280;
}

.player-card {
  background: linear-gradient(135deg, #3B82F6 0%, #6366F1 100%);
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
}

.player-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.player-title {
  font-size: 30rpx;
  color: #fff;
}

.player-progress {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.player-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 32rpx;
}

.control-btn {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: #fff;
}

.play-btn {
  width: 100rpx;
  height: 100rpx;
  background: #fff;
  font-size: 36rpx;
}

.recommend-section {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 24rpx;
}

.recommend-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.recommend-item {
  padding: 20rpx;
  background: #F9FAFB;
  border-radius: 12rpx;
}

.recommend-title {
  font-size: 28rpx;
  color: #1F2937;
  display: block;
  margin-bottom: 8rpx;
}

.recommend-meta {
  font-size: 24rpx;
  color: #9CA3AF;
}
</style>
