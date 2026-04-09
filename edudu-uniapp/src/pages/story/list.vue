<template>
  <view class="story-list">
    <view class="header">
      <text class="title">我的故事</text>
    </view>

    <!-- 空状态 -->
    <view v-if="stories.length === 0" class="empty-state">
      <image class="empty-icon" src="/static/images/empty-story.png" mode="aspectFit" />
      <text class="empty-text">暂无故事记录</text>
      <button class="create-btn" @click="goToCreate">开始创作</button>
    </view>

    <!-- 故事列表 -->
    <scroll-view v-else scroll-y class="story-scroll">
      <view class="story-card" v-for="item in stories" :key="item.id" @click="goToDetail(item.id)">
        <view class="card-header">
          <text class="story-title">{{ item.title }}</text>
          <text class="story-date">{{ formatDate(item.createTime) }}</text>
        </view>
        <view class="card-content">
          <text class="story-summary">{{ item.content }}</text>
        </view>
        <view class="card-footer">
          <view class="tags">
            <text class="tag">{{ item.ageRange }}岁</text>
            <text class="tag">{{ item.personality }}</text>
          </view>
          <view class="actions">
            <text class="action-icon" @click.stop="toggleFavorite(item)">
              {{ item.isFavorite ? '❤️' : '🤍' }}
            </text>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 悬浮按钮 -->
    <view class="fab" @click="goToCreate">
      <text class="fab-icon">+</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onLoad } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { get, del } from '@/api/request'

interface Story {
  id: number
  title: string
  content: string
  ageRange: string
  personality: string
  createTime: string
  isFavorite: boolean
}

const stories = ref<Story[]>([])

onShow(() => {
  loadStories()
})

const loadStories = async () => {
  try {
    const res: any = await get('/story/my')
    stories.value = res.data || []
  } catch (e) {
    console.error('加载故事列表失败', e)
  }
}

const goToDetail = (id: number) => {
  uni.navigateTo({
    url: `/pages/story/detail?id=${id}`
  })
}

const goToCreate = () => {
  uni.navigateTo({
    url: '/pages/story/create'
  })
}

const toggleFavorite = async (item: Story) => {
  try {
    if (item.isFavorite) {
      await del(`/story/${item.id}/unfavorite`)
    } else {
      await post(`/story/${item.id}/favorite`)
    }
    item.isFavorite = !item.isFavorite
  } catch (e) {
    console.error('操作失败', e)
  }
}

const post = async (url: string) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: import.meta.env.VITE_API_BASE_URL + url,
      method: 'POST',
      success: (res: any) => {
        if (res.statusCode === 200) resolve(res.data)
        else reject(res)
      },
      fail: reject
    })
  })
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}
</script>

<style lang="scss" scoped>
.story-list {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;

  .header {
    padding: 20rpx;
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;

    .title {
      font-size: 36rpx;
      font-weight: 600;
      color: #333;
    }
  }

  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 100rpx 0;

    .empty-icon {
      width: 200rpx;
      height: 200rpx;
      margin-bottom: 30rpx;
      opacity: 0.5;
    }

    .empty-text {
      color: #999;
      font-size: 28rpx;
      margin-bottom: 40rpx;
    }

    .create-btn {
      background: #3b82f6;
      color: #fff;
      padding: 20rpx 60rpx;
      border-radius: 50rpx;
      font-size: 28rpx;
    }
  }

  .story-scroll {
    height: calc(100vh - 200rpx);
  }

  .story-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;

      .story-title {
        font-size: 32rpx;
        font-weight: 600;
        color: #333;
      }

      .story-date {
        font-size: 24rpx;
        color: #999;
      }
    }

    .card-content {
      .story-summary {
        font-size: 28rpx;
        color: #666;
        line-height: 1.6;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
    }

    .card-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 20rpx;
      padding-top: 20rpx;
      border-top: 1px solid #f0f0f0;

      .tags {
        display: flex;
        gap: 16rpx;

        .tag {
          background: #f0f7ff;
          color: #3b82f6;
          padding: 8rpx 16rpx;
          border-radius: 8rpx;
          font-size: 22rpx;
        }
      }

      .action-icon {
        font-size: 36rpx;
      }
    }
  }

  .fab {
    position: fixed;
    right: 40rpx;
    bottom: 100rpx;
    width: 100rpx;
    height: 100rpx;
    background: #3b82f6;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 20rpx rgba(59, 130, 246, 0.4);

    .fab-icon {
      font-size: 60rpx;
      color: #fff;
      font-weight: 300;
    }
  }
}
</style>
