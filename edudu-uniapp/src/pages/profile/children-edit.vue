<template>
  <view class="edit-page">
    <view class="form-section">
      <view class="form-item">
        <text class="form-label">姓名</text>
        <input v-model="form.name" placeholder="请输入孩子姓名" class="input" />
      </view>
      <view class="form-item">
        <text class="form-label">性别</text>
        <view class="gender-selector">
          <view class="gender-item" :class="{ active: form.gender === 1 }" @click="form.gender = 1">👦 男</view>
          <view class="gender-item" :class="{ active: form.gender === 2 }" @click="form.gender = 2">👧 女</view>
        </view>
      </view>
      <view class="form-item">
        <text class="form-label">出生日期</text>
        <picker mode="date" :value="form.birthDate" @change="(e: any) => form.birthDate = e.detail.value">
          <view class="picker-value">{{ form.birthDate || '选择日期' }}</view>
        </picker>
      </view>
      <view class="form-item">
        <text class="form-label">性格特点（选填）</text>
        <input v-model="form.personality" placeholder="如：活泼、好奇" class="input" />
      </view>
    </view>

    <button class="btn btn-primary btn-block btn-lg" @click="handleSave">{{ isEdit ? '保存' : '添加' }}</button>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useChildStore } from '@/stores/child'
import { showSuccess, showError, showLoading, hideLoading } from '@/utils'

const childStore = useChildStore()
const childId = ref<number | null>(null)
const isEdit = ref(false)

const form = reactive({
  name: '',
  gender: 1 as 1 | 2,
  birthDate: '',
  personality: '',
  avatar: '',
})

const handleSave = async () => {
  if (!form.name.trim()) { showError('请输入姓名'); return }
  if (!form.birthDate) { showError('请选择出生日期'); return }

  showLoading('保存中...')
  try {
    let success: boolean
    if (isEdit.value && childId.value) {
      success = await childStore.updateChildInfo(childId.value, form)
    } else {
      const result = await childStore.addNewChild(form)
      success = !!result
    }
    hideLoading()
    if (success) { showSuccess('保存成功'); uni.navigateBack() } else { showError('保存失败') }
  } catch { hideLoading(); showError('保存失败') }
}

onMounted(() => {
  uni.getSystemInfo({
    success: (info) => {
      const pages = getCurrentPages()
      const currentPage = pages[pages.length - 1] as any
      const id = parseInt(currentPage?.options?.id || '0')
      if (id) {
        childId.value = id
        isEdit.value = true
        const child = childStore.children.find(c => c.id === id)
        if (child) {
          form.name = child.name
          form.gender = child.gender
          form.birthDate = child.birthDate
          form.personality = child.personality || ''
          form.avatar = child.avatar || ''
        }
      }
    }
  })
})
</script>

<style lang="scss" scoped>
.edit-page { min-height: 100vh; background: #F8F8F8; padding: 32rpx; }
.form-section { background: #fff; border-radius: 24rpx; padding: 32rpx; margin-bottom: 32rpx; }
.form-item { margin-bottom: 32rpx; }
.form-item:last-child { margin-bottom: 0; }
.form-label { font-size: 28rpx; color: #6B7280; display: block; margin-bottom: 12rpx; }
.input { height: 88rpx; background: #F3F4F6; border-radius: 12rpx; padding: 0 24rpx; font-size: 28rpx; }
.picker-value { height: 88rpx; background: #F3F4F6; border-radius: 12rpx; padding: 0 24rpx; display: flex; align-items: center; font-size: 28rpx; }
.gender-selector { display: flex; gap: 24rpx; }
.gender-item { flex: 1; height: 88rpx; background: #F3F4F6; border-radius: 12rpx; display: flex; align-items: center; justify-content: center; font-size: 28rpx; color: #6B7280; }
.gender-item.active { background: #3B82F6; color: #fff; }
</style>
