<template>
  <view class="story-create">
    <!-- 页面标题 -->
    <view class="page-header">
      <text class="page-title">AI 讲故事</text>
      <text class="page-desc">为孩子定制专属故事</text>
    </view>

    <!-- 故事参数表单 -->
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

      <!-- 性格特点 -->
      <view class="form-item">
        <text class="form-label">性格特点</text>
        <view class="tag-selector">
          <view
            v-for="tag in personalityTags"
            :key="tag"
            class="tag-item"
            :class="{ active: form.personality.includes(tag) }"
            @click="toggleTag(tag)"
          >
            {{ tag }}
          </view>
        </view>
      </view>

      <!-- 故事场景 -->
      <view class="form-item">
        <text class="form-label">故事场景</text>
        <view class="scene-selector">
          <view
            v-for="scene in sceneOptions"
            :key="scene.value"
            class="scene-item"
            :class="{ active: form.scene === scene.value }"
            @click="form.scene = scene.value"
          >
            <text class="scene-icon">{{ scene.icon }}</text>
            <text class="scene-name">{{ scene.name }}</text>
          </view>
        </view>
        <input
          v-model="form.customScene"
          type="text"
          placeholder="或者自定义场景，如：去海边玩"
          class="input scene-input"
        />
      </view>

      <!-- 故事类型 -->
      <view class="form-item">
        <text class="form-label">故事类型</text>
        <view class="type-selector">
          <view
            v-for="type in storyTypes"
            :key="type.value"
            class="type-item"
            :class="{ active: form.storyType === type.value }"
            @click="form.storyType = type.value"
          >
            {{ type.name }}
          </view>
        </view>
      </view>
    </view>

    <!-- 生成按钮 -->
    <button
      class="btn btn-primary btn-block btn-lg generate-btn"
      :disabled="isGenerating"
      @click="handleGenerate"
    >
      {{ isGenerating ? 'AI 正在创作中...' : '生成故事' }}
    </button>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { generateStory } from '@/api/story'
import { showError, showLoading, hideLoading } from '@/utils'

const userStore = useUserStore()

// 年龄选项
const ages = [1, 2, 3, 4, 5, 6]

// 性格标签
const personalityTags = ['活泼', '内向', '好奇', '勇敢', '善良', '调皮', '敏感', '独立']

// 场景选项
const sceneOptions = [
  { value: 'sleep', name: '睡前', icon: '🌙' },
  { value: 'morning', name: '早起', icon: '🌅' },
  { value: 'playground', name: '游乐场', icon: '🎠' },
  { value: 'school', name: '上学', icon: '🏫' },
]

// 故事类型
const storyTypes = [
  { value: 'adventure', name: '冒险故事' },
  { value: 'education', name: '教育故事' },
  { value: 'emotion', name: '情绪引导' },
  { value: 'moral', name: '品德故事' },
]

// 表单数据
const form = reactive({
  age: 3,
  personality: [] as string[],
  scene: 'sleep',
  customScene: '',
  storyType: 'adventure',
})

const isGenerating = ref(false)

// 切换标签
const toggleTag = (tag: string) => {
  const index = form.personality.indexOf(tag)
  if (index > -1) {
    form.personality.splice(index, 1)
  } else {
    if (form.personality.length < 3) {
      form.personality.push(tag)
    }
  }
}

// 生成故事
const handleGenerate = async () => {
  if (!userStore.currentChildId) {
    showError('请先选择孩子')
    uni.navigateTo({ url: '/pages/profile/children' })
    return
  }

  const scene = form.customScene || form.scene

  if (!scene) {
    showError('请选择或输入故事场景')
    return
  }

  isGenerating.value = true
  showLoading('AI 正在创作故事...')

  try {
    const res = await generateStory({
      childId: userStore.currentChildId,
      age: form.age,
      personality: form.personality.join(','),
      scene,
      storyType: form.storyType,
    })

    hideLoading()

    if (res.code === 200) {
      // 跳转到故事详情页
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
</script>

<style lang="scss" scoped>
.story-create {
  min-height: 100vh;
  background: #F8F8F8;
  padding: 32rpx;
}

.page-header {
  margin-bottom: 32rpx;
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
    background: #3B82F6;
    color: #fff;
  }
}

.tag-selector {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.tag-item {
  padding: 12rpx 24rpx;
  background: #F3F4F6;
  border-radius: 40rpx;
  font-size: 26rpx;
  color: #6B7280;

  &.active {
    background: rgba(59, 130, 246, 0.1);
    color: #3B82F6;
    border: 2rpx solid #3B82F6;
  }
}

.scene-selector {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16rpx;
  margin-bottom: 16rpx;
}

.scene-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx;
  background: #F3F4F6;
  border-radius: 16rpx;

  &.active {
    background: rgba(59, 130, 246, 0.1);
    border: 2rpx solid #3B82F6;
  }

  .scene-icon {
    font-size: 40rpx;
    margin-bottom: 8rpx;
  }

  .scene-name {
    font-size: 24rpx;
    color: #6B7280;
  }
}

.scene-input {
  height: 88rpx;
  background: #F9FAFB;
  border: 2rpx solid #E5E7EB;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.type-selector {
  display: flex;
  gap: 16rpx;
  flex-wrap: wrap;
}

.type-item {
  padding: 16rpx 28rpx;
  background: #F3F4F6;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #6B7280;

  &.active {
    background: #3B82F6;
    color: #fff;
  }
}

.generate-btn {
  background: linear-gradient(135deg, #3B82F6 0%, #6366F1 100%);
  box-shadow: 0 8rpx 24rpx rgba(59, 130, 246, 0.3);
}
</style>
