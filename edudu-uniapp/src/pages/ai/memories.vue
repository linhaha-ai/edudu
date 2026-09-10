
<template>
  <view class="memories-page">
    <view class="page-header">
      <text class="page-title">AI记忆管理</text>
      <button class="add-btn" @click="showAddDialog = true">+ 添加记忆</button>
    </view>

    <view class="summary-card">
      <text class="summary-title">🤖 孩子画像</text>
      <text v-if="summary" class="summary-content">{{ summary }}</text>
      <text v-else class="summary-empty">还没有生成孩子画像，多和AI聊聊天吧~</text>
    </view>

    <view class="category-tabs">
      <view
        v-for="cat in categories"
        :key="cat.key"
        class="category-tab"
        :class="{ active: currentCategory === cat.key }"
        @click="currentCategory = cat.key"
      >
        <text class="tab-icon">{{ cat.icon }}</text>
        <text class="tab-label">{{ cat.label }}</text>
      </view>
    </view>

    <view class="memories-list">
      <view v-for="memory in filteredMemories" :key="memory.id" class="memory-item">
        <view class="memory-header">
          <text class="memory-key">{{ memory.key }}</text>
          <view class="memory-meta">
            <text v-for="i in memory.importance" :key="i" class="star">⭐</text>
            <text class="memory-time">{{ formatDate(memory.createdAt) }}</text>
          </view>
        </view>
        <text class="memory-value">{{ memory.value }}</text>
        <view class="memory-actions">
          <text class="delete-btn" @click="deleteMemory(memory.id)">删除</text>
        </view>
      </view>

      <view v-if="filteredMemories.length === 0" class="empty-state">
        <text class="empty-icon">💭</text>
        <text class="empty-text">这里还没有记忆</text>
      </view>
    </view>

    <view v-if="showAddDialog" class="dialog-overlay" @click="showAddDialog = false">
      <view class="dialog-content" @click.stop>
        <text class="dialog-title">添加记忆</text>

        <view class="form-item">
          <text class="form-label">分类</text>
          <picker mode="selector" :range="categoryOptions" @change="onCategoryChange">
            <view class="picker">
              <text>{{ categoryOptions[newMemory.categoryIndex] }}</text>
              <text class="arrow">›</text>
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="form-label">标签</text>
          <input class="form-input" v-model="newMemory.key" placeholder="例如：喜欢的颜色" />
        </view>

        <view class="form-item">
          <text class="form-label">内容</text>
          <textarea class="form-textarea" v-model="newMemory.value" placeholder="描述这个记忆..." :maxlength="200" />
        </view>

        <view class="form-item">
          <text class="form-label">重要性：{{ newMemory.importance }}星</text>
          <view class="importance-selector">
            <text
              v-for="i in 5"
              :key="i"
              class="star-btn"
              :class="{ active: i <= newMemory.importance }"
              @click="newMemory.importance = i"
            >
              {{ i <= newMemory.importance ? '⭐' : '☆' }}
            </text>
          </view>
        </view>

        <view class="dialog-actions">
          <button class="btn btn-cancel" @click="showAddDialog = false">取消</button>
          <button class="btn btn-primary" @click="addMemory">添加</button>
        </view>
      </view>
    </view>

    <view class="bottom-safe"></view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { getUserMemories, getConversationSummary, createUserMemory, deleteUserMemory as deleteMemoryApi } from '@/api/ai'
import type { UserMemory } from '@/api/ai'
import { showSuccess, showConfirm, showError } from '@/utils'

const userStore = useUserStore()
const childStore = useChildStore()

const currentCategory = ref('all')
const showAddDialog = ref(false)
const summary = ref('')
const memories = ref<UserMemory[]>([])

const categories = [
  { key: 'all', label: '全部', icon: '📋' },
  { key: 'preference', label: '喜好', icon: '❤️' },
  { key: 'skill', label: '能力', icon: '✨' },
  { key: 'routine', label: '习惯', icon: '📅' },
  { key: 'health', label: '健康', icon: '🏥' },
  { key: 'other', label: '其他', icon: '📝' }
]

const categoryOptions = categories.filter(c => c.key !== 'all').map(c => c.label)

const newMemory = ref({
  categoryIndex: 0,
  key: '',
  value: '',
  importance: 3
})

const filteredMemories = computed(() => {
  if (currentCategory.value === 'all') return memories.value
  return memories.value.filter(m => m.category === currentCategory.value)
})

const formatDate = (dateStr: string) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const onCategoryChange = (e: any) => {
  newMemory.value.categoryIndex = e.detail.value
}

const loadData = async () => {
  if (!userStore.currentChildId) return

  try {
    const [memoriesRes, summaryRes] = await Promise.all([
      getUserMemories(userStore.currentChildId),
      getConversationSummary(userStore.currentChildId)
    ])

    if (memoriesRes.code === 200) {
      memories.value = memoriesRes.data || []
    }
    if (summaryRes.code === 200 && summaryRes.data) {
      summary.value = summaryRes.data.summary
    }
  } catch (error) {
    // 加载失败时使用模拟数据
    memories.value = [
      {
        id: 1,
        childId: userStore.currentChildId,
        key: '喜欢的故事主题',
        value: '喜欢小兔子和公主的故事，对魔法元素特别感兴趣',
        category: 'preference',
        importance: 5,
        createdAt: new Date().toISOString()
      },
      {
        id: 2,
        childId: userStore.currentChildId,
        key: '擅长的活动',
        value: '拼图能力很强，可以独立完成24片拼图',
        category: 'skill',
        importance: 4,
        createdAt: new Date(Date.now() - 86400000).toISOString()
      }
    ]
    summary.value = '孩子喜欢小兔子和公主的故事，对颜色和数字很感兴趣，最近在学习分享和情绪管理。'
  }
}

const addMemory = async () => {
  if (!newMemory.value.key.trim() || !newMemory.value.value.trim()) {
    showError('请填写完整信息')
    return
  }

  const categoryKeys = categories.filter(c => c.key !== 'all').map(c => c.key)
  const category = categoryKeys[newMemory.value.categoryIndex]

  try {
    await createUserMemory({
      childId: userStore.currentChildId!,
      key: newMemory.value.key,
      value: newMemory.value.value,
      category,
      importance: newMemory.value.importance
    })

    showSuccess('添加成功')
    showAddDialog.value = false
    newMemory.value = { categoryIndex: 0, key: '', value: '', importance: 3 }
    loadData()
  } catch (error) {
    showError('添加失败')
  }
}

const deleteMemory = async (id: number) => {
  const confirm = await showConfirm('提示', '确定要删除这条记忆吗？')
  if (!confirm) return

  try {
    await deleteMemoryApi(id)
    showSuccess('删除成功')
    loadData()
  } catch (error) {
    showError('删除失败')
  }
}

onMounted(() => {
  if (!userStore.isLoggedIn) {
    uni.reLaunch({ url: '/pages/user/login' })
    return
  }
  childStore.fetchChildren()
  loadData()
})
</script>

<style lang="scss" scoped>
.memories-page {
  min-height: 100vh;
  background: #F8F8F8;
  padding: 32rpx;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.page-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #1F2937;
}

.add-btn {
  padding: 12rpx 24rpx;
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  color: #fff;
  border-radius: 40rpx;
  font-size: 26rpx;
  border: none;
}

.summary-card {
  background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
}

.summary-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #92400E;
  display: block;
  margin-bottom: 16rpx;
}

.summary-content {
  font-size: 26rpx;
  color: #78350F;
  line-height: 1.8;
}

.summary-empty {
  font-size: 26rpx;
  color: #B45309;
}

.category-tabs {
  display: flex;
  gap: 12rpx;
  margin-bottom: 24rpx;
  overflow-x: auto;
}

.category-tab {
  background: #fff;
  padding: 16rpx 24rpx;
  border-radius: 40rpx;
  display: flex;
  align-items: center;
  gap: 8rpx;
  flex-shrink: 0;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.category-tab.active {
  background: linear-gradient(135deg, #3B82F6, #6366F1);
}

.tab-icon {
  font-size: 24rpx;
}

.tab-label {
  font-size: 24rpx;
  color: #1F2937;
}

.category-tab.active .tab-label {
  color: #fff;
}

.memories-list {
  margin-bottom: 32rpx;
}

.memory-item {
  background: #fff;
  border-radius: 20rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.memory-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.memory-key {
  font-size: 28rpx;
  font-weight: bold;
  color: #1F2937;
}

.memory-meta {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.star {
  font-size: 20rpx;
}

.memory-time {
  font-size: 22rpx;
  color: #9CA3AF;
}

.memory-value {
  font-size: 26rpx;
  color: #4B5563;
  line-height: 1.6;
  display: block;
  margin-bottom: 16rpx;
}

.memory-actions {
  display: flex;
  justify-content: flex-end;
}

.delete-btn {
  font-size: 24rpx;
  color: #EF4444;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  display: block;
  margin-bottom: 24rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #9CA3AF;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog-content {
  background: #fff;
  border-radius: 24rpx;
  padding: 40rpx;
  width: 600rpx;
  max-height: 80vh;
  overflow-y: auto;
}

.dialog-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 32rpx;
  text-align: center;
}

.form-item {
  margin-bottom: 24rpx;
}

.form-label {
  font-size: 26rpx;
  color: #4B5563;
  display: block;
  margin-bottom: 12rpx;
}

.form-input, .form-textarea, .picker {
  width: 100%;
  background: #F3F4F6;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.picker {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.arrow {
  color: #9CA3AF;
  font-size: 32rpx;
}

.form-textarea {
  min-height: 120rpx;
}

.importance-selector {
  display: flex;
  gap: 16rpx;
  margin-top: 12rpx;
}

.star-btn {
  font-size: 40rpx;
  padding: 8rpx;
}

.star-btn.active {
  transform: scale(1.2);
}

.dialog-actions {
  display: flex;
  gap: 16rpx;
  margin-top: 32rpx;
}

.btn {
  flex: 1;
  padding: 24rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.btn-cancel {
  background: #F3F4F6;
  color: #4B5563;
}

.btn-primary {
  background: linear-gradient(135deg, #3B82F6, #6366F1);
  color: #fff;
}

.bottom-safe {
  height: 120rpx;
}
</style>
