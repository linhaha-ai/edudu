
&lt;template&gt;
  &lt;view class="child-detail-page"&gt;
    &lt;view class="header-section"&gt;
      &lt;view class="child-avatar-large"&gt;
        &lt;image v-if="child?.avatar" :src="child.avatar" /&gt;
        &lt;text v-else&gt;{{ child?.name?.charAt(0) || '宝' }}&lt;/text&gt;
      &lt;/view&gt;
      &lt;text class="child-name"&gt;{{ child?.name }}&lt;/text&gt;
      &lt;text class="child-age"&gt;{{ calculateAge(child?.birthDate) }}岁 · {{ getGenderText(child?.gender) }}&lt;/text&gt;
    &lt;/view&gt;

    &lt;view class="stats-grid"&gt;
      &lt;view class="stat-item"&gt;
        &lt;text class="stat-value"&gt;{{ stats.totalStories }}&lt;/text&gt;
        &lt;text class="stat-label"&gt;AI故事&lt;/text&gt;
      &lt;/view&gt;
      &lt;view class="stat-item"&gt;
        &lt;text class="stat-value"&gt;{{ stats.completedTasks }}&lt;/text&gt;
        &lt;text class="stat-label"&gt;完成任务&lt;/text&gt;
      &lt;/view&gt;
      &lt;view class="stat-item"&gt;
        &lt;text class="stat-value"&gt;{{ stats.streakDays }}&lt;/text&gt;
        &lt;text class="stat-label"&gt;连续天数&lt;/text&gt;
      &lt;/view&gt;
      &lt;view class="stat-item"&gt;
        &lt;text class="stat-value"&gt;{{ stats.totalConversations }}&lt;/text&gt;
        &lt;text class="stat-label"&gt;AI对话&lt;/text&gt;
      &lt;/view&gt;
    &lt;/view&gt;

    &lt;view class="quick-actions"&gt;
      &lt;view class="action-card" @click="goToCreateStory"&gt;
        &lt;text class="action-icon"&gt;📖&lt;/text&gt;
        &lt;text class="action-title"&gt;生成故事&lt;/text&gt;
      &lt;/view&gt;
      &lt;view class="action-card" @click="goToTasks"&gt;
        &lt;text class="action-icon"&gt;📋&lt;/text&gt;
        &lt;text class="action-title"&gt;今日任务&lt;/text&gt;
      &lt;/view&gt;
      &lt;view class="action-card" @click="goToAiChat"&gt;
        &lt;text class="action-icon"&gt;💬&lt;/text&gt;
        &lt;text class="action-title"&gt;AI对话&lt;/text&gt;
      &lt;/view&gt;
      &lt;view class="action-card" @click="goToEdit"&gt;
        &lt;text class="action-icon"&gt;✏️&lt;/text&gt;
        &lt;text class="action-title"&gt;编辑资料&lt;/text&gt;
      &lt;/view&gt;
    &lt;/view&gt;

    &lt;view class="section"&gt;
      &lt;view class="section-header"&gt;
        &lt;text class="section-title"&gt;最近故事&lt;/text&gt;
        &lt;text class="section-more" @click="goToStories"&gt;查看全部 ›&lt;/text&gt;
      &lt;/view&gt;
      &lt;view class="story-list"&gt;
        &lt;view v-for="story in recentStories" :key="story.id" class="story-item" @click="goToStoryDetail(story.id)"&gt;
          &lt;text class="story-emoji"&gt;{{ story.theme === 'bedtime' ? '🌙' : story.theme === 'emotion' ? '🌈' : '📖' }}&lt;/text&gt;
          &lt;view class="story-content"&gt;
            &lt;text class="story-title"&gt;{{ story.title }}&lt;/text&gt;
            &lt;text class="story-time"&gt;{{ formatTime(story.createdAt) }}&lt;/text&gt;
          &lt;/view&gt;
        &lt;/view&gt;
        &lt;view v-if="recentStories.length === 0" class="empty-mini"&gt;
          &lt;text class="empty-text"&gt;还没有故事&lt;/text&gt;
        &lt;/view&gt;
      &lt;/view&gt;
    &lt;/view&gt;

    &lt;view class="section"&gt;
      &lt;view class="section-header"&gt;
        &lt;text class="section-title"&gt;AI记忆摘要&lt;/text&gt;
        &lt;text class="section-more" @click="goToMemories"&gt;管理记忆 ›&lt;/text&gt;
      &lt;/view&gt;
      &lt;view class="memory-card"&gt;
        &lt;text v-if="memorySummary" class="memory-content"&gt;{{ memorySummary }}&lt;/text&gt;
        &lt;text v-else class="memory-empty"&gt;暂无记忆，开始与AI对话吧~&lt;/text&gt;
      &lt;/view&gt;
    &lt;/view&gt;

    &lt;view class="bottom-safe"&gt;&lt;/view&gt;
  &lt;/view&gt;
&lt;/template&gt;

&lt;script setup lang="ts"&gt;
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { calculateAge } from '@/utils'

// 常量定义
const MAX_RECENT_DAYS = 7

// 接口定义
interface StatsData {
  totalStories: number
  completedTasks: number
  streakDays: number
  totalConversations: number
}

interface StoryItem {
  id: number
  title: string
  theme: string
  createdAt: string
}

const userStore = useUserStore()
const childStore = useChildStore()

const stats = ref({
  totalStories: 0,
  completedTasks: 0,
  streakDays: 0,
  totalConversations: 0
})

const recentStories = ref&lt;StoryItem[]&gt;([])
const memorySummary = ref('')

const child = computed(() =&gt; {
  if (!userStore.currentChildId) return null
  return childStore.children.find(c =&gt; c.id === userStore.currentChildId)
})

const getGenderText = (gender?: string) =&gt; {
  if (gender === 'male') return '男孩'
  if (gender === 'female') return '女孩'
  return '未知'
}

const formatTime = (time?: string) =&gt; {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(hours / 24)
  if (days &gt; MAX_RECENT_DAYS) return date.toLocaleDateString()
  if (days &gt; 0) return `${days}天前`
  if (hours &gt; 0) return `${hours}小时前`
  return '刚刚'
}

const goToCreateStory = () =&gt; uni.navigateTo({ url: '/pages/story/create' })
const goToTasks = () =&gt; uni.switchTab({ url: '/pages/task/index' })
const goToAiChat = () =&gt; uni.navigateTo({ url: '/pages/ai/chat' })
const goToEdit = () =&gt; {
  if (child.value) {
    uni.navigateTo({ url: `/pages/profile/children-edit?id=${child.value.id}` })
  }
}
const goToStories = () =&gt; uni.navigateTo({ url: '/pages/story/list' })
const goToStoryDetail = (id: number) =&gt; uni.navigateTo({ url: `/pages/story/detail?id=${id}` })
const goToMemories = () =&gt; uni.navigateTo({ url: '/pages/ai/memories' })

onMounted(() =&gt; {
  if (!userStore.isLoggedIn) {
    uni.reLaunch({ url: '/pages/user/login' })
    return
  }
  childStore.fetchChildren()
  // 模拟加载统计数据
  stats.value = {
    totalStories: 12,
    completedTasks: 45,
    streakDays: 7,
    totalConversations: 23
  }
  // 模拟最近故事
  recentStories.value = [
    { id: 1, title: '小兔子的冒险', theme: 'bedtime', createdAt: new Date(Date.now() - 3600000).toISOString() },
    { id: 2, title: '分享的快乐', theme: 'emotion', createdAt: new Date(Date.now() - 86400000).toISOString() }
  ]
  memorySummary.value = '孩子喜欢小兔子和公主的故事，对颜色和数字很感兴趣，最近在学习分享和情绪管理。'
})
&lt;/script&gt;

&lt;style lang="scss" scoped&gt;
.child-detail-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #EEF2FF 0%, #F8F8F8 50%);
}

.header-section {
  padding: 80rpx 32rpx 48rpx;
  text-align: center;
}

.child-avatar-large {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24rpx;
  border: 6rpx solid #fff;
  box-shadow: 0 8rpx 24rpx rgba(59, 130, 246, 0.2);
}

.child-avatar-large image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.child-avatar-large text {
  color: #fff;
  font-size: 56rpx;
  font-weight: bold;
}

.child-name {
  font-size: 40rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 12rpx;
}

.child-age {
  font-size: 28rpx;
  color: #6B7280;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  padding: 32rpx;
  background: #fff;
  margin: 0 32rpx 24rpx;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 40rpx;
  font-weight: bold;
  color: #3B82F6;
  display: block;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #6B7280;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  padding: 0 32rpx 24rpx;
  gap: 16rpx;
}

.action-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 32rpx 16rpx;
  text-align: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.action-icon {
  font-size: 40rpx;
  display: block;
  margin-bottom: 12rpx;
}

.action-title {
  font-size: 24rpx;
  color: #1F2937;
}

.section {
  margin: 0 32rpx 24rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
}

.section-more {
  font-size: 26rpx;
  color: #6B7280;
}

.story-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F3F4F6;
}

.story-item:last-child {
  border-bottom: none;
}

.story-emoji {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.story-content {
  flex: 1;
}

.story-title {
  font-size: 28rpx;
  color: #1F2937;
  display: block;
  margin-bottom: 6rpx;
}

.story-time {
  font-size: 24rpx;
  color: #9CA3AF;
}

.empty-mini {
  text-align: center;
  padding: 40rpx 0;
}

.empty-text {
  font-size: 26rpx;
  color: #9CA3AF;
}

.memory-card {
  background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
  border-radius: 16rpx;
  padding: 24rpx;
}

.memory-content {
  font-size: 26rpx;
  color: #92400E;
  line-height: 1.8;
}

.memory-empty {
  font-size: 26rpx;
  color: #B45309;
}

.bottom-safe {
  height: 120rpx;
}
&lt;/style&gt;
