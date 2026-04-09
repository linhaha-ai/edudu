<template>
  <view class="task-detail">
    <view class="task-header">
      <view class="task-type-tag" :class="task.type">{{ getTypeName(task.type) }}</view>
      <text class="task-title">{{ task.title }}</text>
      <text class="task-duration">预计时长: {{ task.duration }}分钟</text>
    </view>

    <view class="task-section">
      <text class="section-title">互动话术</text>
      <view class="speaking-script">
        <text>{{ task.speakingScript }}</text>
      </view>
    </view>

    <view class="task-section">
      <text class="section-title">操作步骤</text>
      <view v-for="(step, index) in task.steps" :key="index" class="step-item">
        <view class="step-number">{{ index + 1 }}</view>
        <text class="step-text">{{ step }}</text>
      </view>
    </view>

    <view class="task-section">
      <text class="section-title">完成记录</text>
      <textarea v-model="note" placeholder="记录完成情况（选填）" class="note-input" />
    </view>

    <button class="btn btn-primary btn-block btn-lg" @click="handleComplete">完成</button>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getTaskDetail, completeTask } from '@/api/task'
import type { DailyTask } from '@/api/task'
import { showSuccess, showError } from '@/utils'

const taskId = ref(0)
const task = ref<DailyTask>({} as DailyTask)
const note = ref('')

const taskTypes = [
  { value: 'cognitive', name: '认知' },
  { value: 'language', name: '语言' },
  { value: 'motor', name: '运动' },
  { value: 'art', name: '艺术' },
]

const getTypeName = (type: string) => taskTypes.find(t => t.value === type)?.name || type

const loadTask = async () => {
  const res = await getTaskDetail(taskId.value)
  if (res.code === 200) task.value = res.data
}

const handleComplete = async () => {
  const res = await completeTask(taskId.value, note.value)
  if (res.code === 200) {
    showSuccess('任务完成')
    uni.navigateBack()
  } else {
    showError('操作失败')
  }
}

onMounted(() => {
  uni.getSystemInfo({ success: (info) => {
    const pages = getCurrentPages()
    const currentPage = pages[pages.length - 1] as any
    taskId.value = parseInt(currentPage?.options?.id || '0')
    loadTask()
  }})
})
</script>

<style lang="scss" scoped>
.task-detail { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.task-header { background: #fff; border-radius: 24rpx; padding: 32rpx; margin-bottom: 24rpx; }
.task-type-tag { display: inline-block; padding: 8rpx 20rpx; border-radius: 8rpx; font-size: 24rpx; margin-bottom: 16rpx; }
.task-type-tag.cognitive { background: rgba(59,130,246,0.1); color: #3B82F6; }
.task-type-tag.language { background: rgba(16,185,129,0.1); color: #10B981; }
.task-type-tag.motor { background: rgba(245,158,11,0.1); color: #F59E0B; }
.task-type-tag.art { background: rgba(139,92,246,0.1); color: #8B5CF6; }
.task-title { font-size: 36rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 12rpx; }
.task-duration { font-size: 26rpx; color: #6B7280; }
.task-section { background: #fff; border-radius: 24rpx; padding: 32rpx; margin-bottom: 24rpx; }
.section-title { font-size: 30rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 20rpx; }
.speaking-script { background: #F3F4F6; padding: 24rpx; border-radius: 16rpx; font-size: 28rpx; line-height: 1.8; color: #374151; }
.step-item { display: flex; align-items: flex-start; margin-bottom: 20rpx; }
.step-number { width: 48rpx; height: 48rpx; background: #3B82F6; color: #fff; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 24rpx; margin-right: 16rpx; flex-shrink: 0; }
.step-text { font-size: 28rpx; color: #374151; line-height: 1.6; flex: 1; }
.note-input { width: 100%; height: 160rpx; background: #F3F4F6; border-radius: 16rpx; padding: 24rpx; font-size: 28rpx; }
</style>
