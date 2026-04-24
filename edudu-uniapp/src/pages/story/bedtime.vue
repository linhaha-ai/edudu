<template>
  <view class="bedtime-story-page">
    <!-- 页面标题 -->
    <view class="page-header">
      <view class="header-nav" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <view class="header-content">
        <text class="page-title">睡前故事</text>
        <text class="page-desc">温馨故事陪伴入睡</text>
      </view>
    </view>

    <!-- 月亮装饰 -->
    <view class="moon-area">
      <text class="moon">🌙</text>
      <text class="stars">✨ ⭐ ✨</text>
    </view>

    <!-- 基础参数 -->
    <view class="form-card">
      <!-- 年龄选择 -->
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

      <!-- 性别选择 -->
      <view class="form-item">
        <text class="form-label">孩子性别</text>
        <view class="gender-selector">
          <view
            class="gender-item"
            :class="{ active: form.gender === 'boy' }"
            @click="form.gender = 'boy'"
          >
            <text class="gender-icon">👦</text>
            <text>男孩</text>
          </view>
          <view
            class="gender-item"
            :class="{ active: form.gender === 'girl' }"
            @click="form.gender = 'girl'"
          >
            <text class="gender-icon">👧</text>
            <text>女孩</text>
          </view>
        </view>
      </view>

      <!-- 教育目标 -->
      <view class="form-item">
        <text class="form-label">今晚想培养什么品质？（可选）</text>
        <view class="goal-selector">
          <view
            v-for="goal in goalOptions"
            :key="goal.value"
            class="goal-item"
            :class="{ active: form.goal === goal.value }"
            @click="form.goal = form.goal === goal.value ? '' : goal.value"
          >
            <text class="goal-icon">{{ goal.icon }}</text>
            <text class="goal-name">{{ goal.name }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 温馨提示 -->
    <view class="tips-card">
      <text class="tips-icon">💡</text>
      <view class="tips-content">
        <text class="tips-title">温馨小贴士</text>
        <text class="tips-text">
          睡前故事可以帮助孩子：\n• 安抚情绪，更好入睡\n• 培养想象力和语言能力\n• 增进亲子关系
        </text>
      </view>
    </view>

    <!-- 生成按钮 -->
    <button
      class="btn btn-primary btn-block btn-lg generate-btn"
      :disabled="isGenerating"
      @click="handleGenerate"
    >
      {{ isGenerating ? 'AI 正在创作中...' : '生成睡前故事' }}
    </button>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { generateBedtimeStory } from '@/api/story'
import { showError, showLoading, hideLoading } from '@/utils'

const userStore = useUserStore()
const childStore = useChildStore()

// 年龄选项
const ages = [1, 2, 3, 4, 5, 6]

// 教育目标选项
const goalOptions = [
  { value: 'brave', name: '勇敢', icon: '🦁' },
  { value: 'kind', name: '善良', icon: '❤️' },
  { value: 'share', name: '分享', icon: '🤝' },
  { value: 'honest', name: '诚实', icon: '🌟' },
  { value: 'patient', name: '耐心', icon: '🐢' },
  { value: 'calm', name: '平静', icon: '🧘' },
]

// 表单数据
const form = reactive({
  age: 3,
  gender: 'boy',
  goal: '',
})

const isGenerating = ref(false)

// 返回上一页
const goBack = () => {
  uni.navigateBack()
}

// 获取当前孩子的年龄和性别
const loadChildInfo = () => {
  if (userStore.currentChildId) {
    const child = childStore.children.find(c => c.id === userStore.currentChildId)
    if (child) {
      form.age = calculateAge(child.birthDate)
      form.gender = child.gender === '1' ? 'girl' : 'boy'
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
  return Math.max(1, Math.min(age, 6))
}

// 生成故事
const handleGenerate = async () => {
  isGenerating.value = true
  showLoading('AI 正在创作温馨故事...')

  try {
    const res = await generateBedtimeStory({
      age: form.age,
      gender: form.gender,
      goal: form.goal || undefined,
    })

    hideLoading()

    if (res.code === 200) {
      uni.navigateTo({
        url: `/pages/story/detail?id=${res.data.id}`,
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

onMounted(() => {
  loadChildInfo()
})
</script>

<style lang="scss" scoped>
.bedtime-story-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #1E1B4B 0%, #312E81 50%, #F8F8F8 70%);
  padding: 32rpx;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 32rpx;
}

.header-nav {
  width: 64rpx;
  height: 64rpx;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.back-icon {
  font-size: 48rpx;
  color: #fff;
  font-weight: bold;
}

.header-content {
  flex: 1;
}

.page-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
  display: block;
  margin-bottom: 8rpx;
}

.page-desc {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.7);
}

.moon-area {
  text-align: center;
  margin-bottom: 32rpx;
}

.moon {
  font-size: 120rpx;
  display: block;
  margin-bottom: 16rpx;
  animation: float 3s ease-in-out infinite;
}

.stars {
  font-size: 32rpx;
  letter-spacing: 20rpx;
  color: rgba(255, 255, 255, 0.6);
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10rpx); }
}

.form-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
}

.form-item {
  margin-bottom: 32rpx;

  &:last-child {
    margin-bottom: 0;
  }
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
    background: #6366F1;
    color: #fff;
  }
}

.gender-selector {
  display: flex;
  gap: 24rpx;
}

.gender-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  padding: 24rpx;
  background: #F3F4F6;
  border-radius: 16rpx;
  font-size: 28rpx;
  color: #6B7280;
  border: 4rpx solid transparent;

  &.active {
    border-color: #6366F1;
    background: rgba(99, 102, 241, 0.1);
    color: #1F2937;
  }
}

.gender-icon {
  font-size: 40rpx;
}

.goal-selector {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16rpx;
}

.goal-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 16rpx;
  background: #F3F4F6;
  border-radius: 16rpx;
  border: 4rpx solid transparent;

  &.active {
    border-color: #6366F1;
    background: rgba(99, 102, 241, 0.1);
  }
}

.goal-icon {
  font-size: 40rpx;
  margin-bottom: 8rpx;
}

.goal-name {
  font-size: 24rpx;
  color: #6B7280;

  .active & {
    color: #6366F1;
  }
}

.tips-card {
  background: rgba(99, 102, 241, 0.1);
  border-radius: 20rpx;
  padding: 24rpx;
  margin-bottom: 32rpx;
  display: flex;
  gap: 16rpx;
}

.tips-icon {
  font-size: 48rpx;
}

.tips-content {
  flex: 1;
}

.tips-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #6366F1;
  display: block;
  margin-bottom: 8rpx;
}

.tips-text {
  font-size: 24rpx;
  color: #6B7280;
  line-height: 1.6;
}

.generate-btn {
  background: linear-gradient(135deg, #6366F1 0%, #8B5CF6 100%);
  box-shadow: 0 8rpx 24rpx rgba(99, 102, 241, 0.3);
}
</style>
