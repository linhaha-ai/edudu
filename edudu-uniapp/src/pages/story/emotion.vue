<template>
  <view class="emotion-story-page">
    <!-- 页面标题 -->
    <view class="page-header">
      <view class="header-nav" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <view class="header-content">
        <text class="page-title">情绪引导故事</text>
        <text class="page-desc">帮助孩子理解和处理情绪</text>
      </view>
    </view>

    <!-- 情绪选择 -->
    <view class="emotion-section">
      <text class="section-title">孩子现在是什么情绪？</text>
      <view class="emotion-grid">
        <view
          v-for="emotion in emotionOptions"
          :key="emotion.value"
          class="emotion-card"
          :class="{ active: form.emotion === emotion.value }"
          @click="form.emotion = emotion.value"
        >
          <text class="emotion-icon">{{ emotion.icon }}</text>
          <text class="emotion-name">{{ emotion.name }}</text>
          <text class="emotion-desc">{{ emotion.desc }}</text>
        </view>
      </view>
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
    </view>

    <!-- 生成按钮 -->
    <button
      class="btn btn-primary btn-block btn-lg generate-btn"
      :disabled="isGenerating || !form.emotion"
      @click="handleGenerate"
    >
      {{ isGenerating ? 'AI 正在创作中...' : '生成故事' }}
    </button>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { useChildStore } from '@/stores/child'
import { generateEmotionStory } from '@/api/story'
import { showError, showLoading, hideLoading } from '@/utils'

const userStore = useUserStore()
const childStore = useChildStore()

// 年龄选项
const ages = [1, 2, 3, 4, 5, 6]

// 情绪选项
const emotionOptions = [
  { value: 'fear', name: '害怕', icon: '😨', desc: '恐惧、胆怯' },
  { value: 'anger', name: '生气', icon: '😤', desc: '愤怒、发脾气' },
  { value: 'sadness', name: '难过', icon: '😢', desc: '伤心、沮丧' },
  { value: 'anxiety', name: '焦虑', icon: '😰', desc: '紧张、不安' },
  { value: 'jealousy', name: '嫉妒', icon: '😒', desc: '羡慕、攀比' },
  { value: 'loneliness', name: '孤独', icon: '😔', desc: '孤单、寂寞' },
]

// 表单数据
const form = reactive({
  age: 3,
  gender: 'boy',
  emotion: '',
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
  if (!form.emotion) {
    showError('请选择孩子的情绪')
    return
  }

  isGenerating.value = true
  showLoading('AI 正在创作故事...')

  try {
    const res = await generateEmotionStory({
      age: form.age,
      gender: form.gender,
      emotion: form.emotion,
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

// 页面加载
onMounted(() => {
  loadChildInfo()
})

</script>

<style lang="scss" scoped>
.emotion-story-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #FEF3C7 0%, #F8F8F8 30%);
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

.emotion-section {
  margin-bottom: 32rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 24rpx;
}

.emotion-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16rpx;
}

.emotion-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 24rpx 16rpx;
  text-align: center;
  border: 4rpx solid transparent;
  transition: all 0.3s;

  &.active {
    border-color: #F59E0B;
    background: rgba(245, 158, 11, 0.1);
  }
}

.emotion-icon {
  font-size: 56rpx;
  display: block;
  margin-bottom: 12rpx;
}

.emotion-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #1F2937;
  display: block;
  margin-bottom: 4rpx;
}

.emotion-desc {
  font-size: 20rpx;
  color: #9CA3AF;
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
    background: #F59E0B;
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
    border-color: #F59E0B;
    background: rgba(245, 158, 11, 0.1);
    color: #1F2937;
  }
}

.gender-icon {
  font-size: 40rpx;
}

.generate-btn {
  background: linear-gradient(135deg, #F59E0B 0%, #F97316 100%);
  box-shadow: 0 8rpx 24rpx rgba(245, 158, 11, 0.3);
}
</style>
