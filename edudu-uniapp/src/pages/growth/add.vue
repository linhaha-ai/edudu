<template>
  <view class="add-record">
    <view class="page-header">
      <text class="page-title">添加记录</text>
    </view>

    <view class="type-selector">
      <view class="type-item" :class="{ active: recordType === 'sleep' }" @click="recordType = 'sleep'">
        <text class="type-icon">😴</text>
        <text class="type-name">睡眠</text>
      </view>
      <view class="type-item" :class="{ active: recordType === 'diet' }" @click="recordType = 'diet'">
        <text class="type-icon">🍽️</text>
        <text class="type-name">饮食</text>
      </view>
      <view class="type-item" :class="{ active: recordType === 'learning' }" @click="recordType = 'learning'">
        <text class="type-icon">📚</text>
        <text class="type-name">学习</text>
      </view>
    </view>

    <!-- 睡眠记录表单 -->
    <view v-if="recordType === 'sleep'" class="form-section">
      <view class="form-item">
        <text class="form-label">入睡时间</text>
        <picker mode="time" :value="sleepForm.sleepStart" @change="(e: any) => sleepForm.sleepStart = e.detail.value">
          <view class="picker-value">{{ sleepForm.sleepStart || '选择时间' }}</view>
        </picker>
      </view>
      <view class="form-item">
        <text class="form-label">起床时间</text>
        <picker mode="time" :value="sleepForm.sleepEnd" @change="(e: any) => sleepForm.sleepEnd = e.detail.value">
          <view class="picker-value">{{ sleepForm.sleepEnd || '选择时间' }}</view>
        </picker>
      </view>
    </view>

    <!-- 饮食记录表单 -->
    <view v-if="recordType === 'diet'" class="form-section">
      <view class="form-item">
        <text class="form-label">餐次</text>
        <picker :value="mealIndex" :range="mealTypes" @change="(e: any) => mealIndex = e.detail.value">
          <view class="picker-value">{{ mealTypes[mealIndex] || '选择餐次' }}</view>
        </picker>
      </view>
      <view class="form-item">
        <text class="form-label">食物描述</text>
        <input v-model="dietForm.foodDescription" placeholder="如：鸡蛋粥、小米饼" class="input" />
      </view>
      <view class="form-item">
        <text class="form-label">摄入量</text>
        <input v-model="dietForm.amount" placeholder="如：一小碗、半份" class="input" />
      </view>
    </view>

    <!-- 学习记录表单 -->
    <view v-if="recordType === 'learning'" class="form-section">
      <view class="form-item">
        <text class="form-label">学习内容</text>
        <input v-model="learningForm.learningContent" placeholder="如：认识颜色、简单计数" class="input" />
      </view>
      <view class="form-item">
        <text class="form-label">学习时长(分钟)</text>
        <input v-model="learningForm.learningDuration" type="number" placeholder="如：15" class="input" />
      </view>
      <view class="form-item">
        <text class="form-label">掌握程度</text>
        <view class="level-selector">
          <view v-for="i in 5" :key="i" class="level-item" :class="{ active: learningForm.masteryLevel >= i }" @click="learningForm.masteryLevel = i">
            ⭐
          </view>
        </view>
      </view>
    </view>

    <button class="btn btn-primary btn-block btn-lg" @click="handleSubmit">保存</button>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { addGrowthRecord } from '@/api/growth'
import type { RecordType } from '@/api/growth'
import { showSuccess, showError, showLoading, hideLoading } from '@/utils'

const userStore = useUserStore()
const recordType = ref<RecordType>('sleep')
const mealIndex = ref(0)
const mealTypes = ['早餐', '午餐', '晚餐', '加餐']
const sleepForm = ref({ sleepStart: '', sleepEnd: '' })
const dietForm = ref({ mealType: '早餐', foodDescription: '', amount: '' })
const learningForm = ref({ learningContent: '', learningDuration: '', masteryLevel: 3 })

const handleSubmit = async () => {
  if (!userStore.currentChildId) { showError('请先选择孩子'); return }
  showLoading('保存中...')
  try {
    let data: any = { childId: userStore.currentChildId, type: recordType.value, recordDate: new Date().toISOString() }
    if (recordType.value === 'sleep') {
      data = { ...data, ...sleepForm.value }
    } else if (recordType.value === 'diet') {
      data = { ...data, mealType: mealTypes[mealIndex.value], ...dietForm.value }
    } else {
      data = { ...data, ...learningForm.value, learningDuration: parseInt(learningForm.value.learningDuration) }
    }
    const res = await addGrowthRecord(data)
    hideLoading()
    if (res.code === 200) { showSuccess('保存成功'); uni.navigateBack() } else { showError('保存失败') }
  } catch { hideLoading(); showError('保存失败') }
}
</script>

<style lang="scss" scoped>
.add-record { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.page-header { margin-bottom: 32rpx; }
.page-title { font-size: 40rpx; font-weight: bold; color: #1F2937; }
.type-selector { display: flex; gap: 24rpx; margin-bottom: 32rpx; }
.type-item { flex: 1; background: #fff; border-radius: 24rpx; padding: 32rpx; text-align: center; }
.type-item.active { background: #3B82F6; }
.type-icon { font-size: 60rpx; display: block; margin-bottom: 12rpx; }
.type-name { font-size: 28rpx; color: #1F2937; }
.type-item.active .type-name { color: #fff; }
.form-section { background: #fff; border-radius: 24rpx; padding: 32rpx; margin-bottom: 32rpx; }
.form-item { margin-bottom: 32rpx; }
.form-item:last-child { margin-bottom: 0; }
.form-label { font-size: 28rpx; color: #6B7280; display: block; margin-bottom: 12rpx; }
.picker-value { height: 88rpx; background: #F3F4F6; border-radius: 12rpx; padding: 0 24rpx; display: flex; align-items: center; font-size: 28rpx; }
.input { height: 88rpx; background: #F3F4F6; border-radius: 12rpx; padding: 0 24rpx; font-size: 28rpx; }
.level-selector { display: flex; gap: 12rpx; }
.level-item { font-size: 40rpx; opacity: 0.3; }
.level-item.active { opacity: 1; }
</style>
