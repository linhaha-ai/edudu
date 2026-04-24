<template>
  <view class="task-generate-page">
    <!-- 页面标题 -->
    <view class="page-header">
      <view class="header-nav" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <view class="header-content">
        <text class="page-title">生成早教任务</text>
        <text class="page-desc">AI 定制专属早教内容</text>
      </view>
    </view>

    <!-- 年龄选择 -->
    <view class="form-card">
      <view class="form-item">
        <text class="form-label">孩子年龄</text>
        <view class="age-selector">
          <view
            v-for="age in ages"
            :key="age"
            class="age-item"
            :class="{ active: form.age === age }"
            @click="form.age = age"
          >
            {{ age }}岁
          </view>
        </view>
      </view>
    </view>

    <!-- 主题选择 -->
    <view class="theme-section">
      <text class="section-title">选择早教主题</text>
      <view class="theme-grid">
        <view
          v-for="theme in themeOptions"
          :key="theme.value"
          class="theme-card"
          :class="{ active: form.theme === theme.value }"
          @click="form.theme = theme.value"
        >
          <text class="theme-icon">{{ theme.icon }}</text>
          <text class="theme-name">{{ theme.name }}</text>
          <text class="theme-desc">{{ theme.desc }}</text>
        </view>
      </view>
    </view>

    <!-- 推荐任务预览 -->
    <view v-if="previewTask" class="preview-card">
      <view class="preview-header">
        <text class="preview-title">推荐任务</text>
      </view>
      <view class="preview-content">
        <text class="preview-task-title">{{ previewTask.title }}</text>
        <text class="preview-task-goal">{{ previewTask.goal }}</text>
        <view class="preview-steps">
          <text
            v-for="(step, index) in previewTask.steps"
            :key="index"
            class="preview-step"
          >
            {{ index + 1 }}. {{ step }}
          </text>
        </view>
        <text class="preview-duration">建议时长：{{ previewTask.duration }}</text>
      </view>
    </view>

    <!-- 生成按钮 -->
    <button
      class="btn btn-primary btn-block btn-lg generate-btn"
      :disabled="isGenerating"
      @click="handleGenerate"
    >
      {{ isGenerating ? 'AI 正在生成任务...' : '生成任务' }}
    </button>

    <!-- 查看历史 -->
    <view class="history-link" @click="goToHistory">
      <text>查看我的任务</text>
      <text class="arrow">›</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { generateDailyTask } from '@/api/task'
import { showError, showLoading, hideLoading } from '@/utils'

const userStore = useUserStore()
const childStore = useChildStore()

// 年龄选项
const ages = [0, 1, 2, 3, 4, 5, 6]

// 主题选项
const themeOptions = [
  { value: 'color', name: '颜色认知', icon: '🎨', desc: '认识红黄蓝绿' },
  { value: 'animal', name: '动物世界', icon: '🦁', desc: '认识小动物' },
  { value: 'number', name: '数学启蒙', icon: '🔢', desc: '数数与简单计算' },
  { value: 'language', name: '语言发展', icon: '🗣️', desc: '听说能力培养' },
  { value: 'emotion', name: '情绪管理', icon: '😊', desc: '识别和表达情绪' },
  { value: 'motor', name: '运动协调', icon: '🏃', desc: '大运动与精细动作' },
  { value: 'social', name: '社交能力', icon: '🤝', desc: '分享与合作' },
  { value: 'art', name: '艺术创作', icon: '🎭', desc: '绘画与音乐启蒙' },
]

// 表单数据
const form = reactive({
  age: 3,
  theme: '',
})

// 预览任务
const previewTask = ref<{
  title: string
  goal: string
  steps: string[]
  duration: string
} | null>(null)

const isGenerating = ref(false)

// 返回上一页
const goBack = () => {
  uni.navigateBack()
}

// 获取当前孩子的年龄
const loadChildInfo = () => {
  if (userStore.currentChildId) {
    const child = childStore.children.find(c => c.id === userStore.currentChildId)
    if (child && child.birthDate) {
      form.age = calculateAge(child.birthDate)
    }
  }
}

// 计算年龄
const calculateAge = (birthDate: string): number => {
  if (!birthDate) return 3
  const birth = new Date(birthDate)
  const now = new Date()
  let age = now.getFullYear() - birth.getFullYear()
  const monthDiff = now.getMonth() - birth.getMonth()
  if (monthDiff < 0 || (monthDiff === 0 && now.getDate() < birth.getDate())) {
    age--
  }
  return Math.max(0, Math.min(age, 6))
}

// 生成任务
const handleGenerate = async () => {
  if (!form.theme) {
    showError('请选择早教主题')
    return
  }

  isGenerating.value = true
  showLoading('AI 正在生成任务...')

  try {
    const res = await generateDailyTask({
      age: form.age,
      theme: form.theme,
    })

    hideLoading()

    if (res.code === 200) {
      uni.navigateTo({
        url: `/pages/task/detail?id=${res.data.id}`,
      })
    } else {
      showError(res.message || '生成失败')
    }
  } catch (e) {
    hideLoading()
    showError('生成失败，请重试')
  } finally {
    isGenerating.value = false
  }
}

// 查看历史
const goToHistory = () => {
  uni.switchTab({ url: '/pages/task/index' })
}

onMounted(() => {
  loadChildInfo()
})
</script>

<style lang="scss" scoped>
.task-generate-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #ECFDF5 0%, #F8F8F8 30%);
  padding: 32rpx;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}

.header-nav {
  width: 64rpx;
  height: 64rpx;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.back-icon {
  font-size: 48rpx;
  color: #1F2937;
  font-weight: bold;
}

.header-content {
  flex: 1;
}

.page-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 8rpx;
}

.page-desc {
  font-size: 28rpx;
  color: #6B7280;
}

.form-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
}

.form-item {
  margin-bottom: 0;
}

.form-label {
  font-size: 30rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 16rpx;
}

.age-selector {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.age-item {
  padding: 16rpx 28rpx;
  background: #F3F4F6;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #6B7280;

  &.active {
    background: #10B981;
    color: #fff;
  }
}

.theme-section {
  margin-bottom: 32rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 24rpx;
}

.theme-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.theme-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
  text-align: center;
  border: 4rpx solid transparent;
  transition: all 0.3s;

  &.active {
    border-color: #10B981;
    background: rgba(16, 185, 129, 0.1);
  }
}

.theme-icon {
  font-size: 56rpx;
  display: block;
  margin-bottom: 12rpx;
}

.theme-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 4rpx;
}

.theme-desc {
  font-size: 22rpx;
  color: #9CA3AF;
}

.preview-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.preview-header {
  margin-bottom: 20rpx;
}

.preview-title {
  font-size: 26rpx;
  color: #10B981;
  font-weight: bold;
}

.preview-content {
  border-left: 6rpx solid #10B981;
  padding-left: 20rpx;
}

.preview-task-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 8rpx;
}

.preview-task-goal {
  font-size: 26rpx;
  color: #6B7280;
  display: block;
  margin-bottom: 16rpx;
}

.preview-steps {
  margin-bottom: 16rpx;
}

.preview-step {
  font-size: 26rpx;
  color: #4B5563;
  display: block;
  margin-bottom: 8rpx;
  line-height: 1.5;
}

.preview-duration {
  font-size: 24rpx;
  color: #9CA3AF;
}

.generate-btn {
  background: linear-gradient(135deg, #10B981 0%, #059669 100%);
  box-shadow: 0 8rpx 24rpx rgba(16, 185, 129, 0.3);
}

.history-link {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  margin-top: 24rpx;
  padding: 24rpx;
  font-size: 28rpx;
  color: #6B7280;
}

.arrow {
  font-size: 32rpx;
  color: #9CA3AF;
}
</style>
