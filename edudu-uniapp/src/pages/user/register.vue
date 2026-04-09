<template>
  <view class="register-container">
    <!-- 背景装饰 -->
    <view class="register-bg">
      <view class="bg-circle circle-1"></view>
      <view class="bg-circle circle-2"></view>
    </view>

    <!-- Logo 区域 -->
    <view class="logo-section">
      <image class="logo" src="/static/images/logo.png" mode="aspectFit" />
      <text class="app-name">AI早教陪伴</text>
      <text class="app-slogan">为孩子创建一个账号</text>
    </view>

    <!-- 注册表单 -->
    <view class="register-form">
      <!-- 孩子姓名 -->
      <view class="form-item">
        <input
          v-model="childName"
          type="text"
          maxlength="20"
          placeholder="请输入孩子姓名"
          class="input"
        />
      </view>

      <!-- 手机号输入 -->
      <view class="form-item">
        <input
          v-model="phone"
          type="number"
          maxlength="11"
          placeholder="请输入手机号"
          class="input"
        />
      </view>

      <!-- 验证码输入 -->
      <view class="form-item code-item">
        <input
          v-model="code"
          type="number"
          maxlength="6"
          placeholder="验证码"
          class="input"
        />
        <button
          class="code-btn"
          :disabled="countdown > 0"
          @click="handleSendCode"
        >
          {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
        </button>
      </view>

      <!-- 密码 -->
      <view class="form-item password-item">
        <input
          v-model="password"
          :password="!showPassword"
          placeholder="设置登录密码（6-20位）"
          class="input"
        />
        <text class="password-toggle" @click="showPassword = !showPassword">{{ showPassword ? '隐藏' : '显示' }}</text>
      </view>

      <!-- 孩子性别 -->
      <view class="form-item">
        <text class="label">孩子性别</text>
        <view class="gender-select">
          <view
            class="gender-option"
            :class="{ active: gender === 'boy' }"
            @click="gender = 'boy'"
          >
            <text>👦 男</text>
          </view>
          <view
            class="gender-option"
            :class="{ active: gender === 'girl' }"
            @click="gender = 'girl'"
          >
            <text>👧 女</text>
          </view>
        </view>
      </view>

      <!-- 孩子生日 -->
      <view class="form-item">
        <text class="label">孩子生日</text>
        <picker
          mode="date"
          :value="birthday"
          :end="today"
          @change="handleBirthdayChange"
        >
          <view class="picker-value">
            {{ birthday || '请选择生日' }}
          </view>
        </picker>
      </view>

      <!-- 注册按钮 -->
      <button class="btn btn-primary btn-block btn-lg register-btn" @click="handleRegister">
        立即注册
      </button>

      <!-- 返回登录 -->
      <view class="back-login">
        已有账号？
        <text class="link" @click="goLogin">立即登录</text>
      </view>
    </view>

    <!-- 用户协议 -->
    <view class="agreement">
      <checkbox-group @change="handleAgreementChange">
        <label class="agreement-label">
          <checkbox :checked="agreed" color="#3B82F6" style="transform: scale(0.8)" />
          <text class="text-secondary text-sm">
            我已阅读并同意
            <text class="link" @click.stop="openAgreement('user')">《用户协议》</text>
            和
            <text class="link" @click.stop="openAgreement('privacy')">《隐私政策》</text>
          </text>
        </label>
      </checkbox-group>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { validatePhone, validateCode, showError, showSuccess } from '@/utils'

const userStore = useUserStore()

// 表单数据
const childName = ref('')
const phone = ref('')
const code = ref('')
const password = ref('')
const gender = ref<'boy' | 'girl'>('boy')
const birthday = ref('')
const agreed = ref(false)
const countdown = ref(0)
const showPassword = ref(false)

// 今天日期（用于限制生日选择）
const today = new Date().toISOString().split('T')[0]

// 发送验证码
const handleSendCode = async () => {
  if (!validatePhone(phone.value)) {
    showError('请输入正确的手机号')
    return
  }

  const success = await userStore.sendSms({ phone: phone.value, type: 'register' })
  if (success) {
    showSuccess('验证码已发送')
    // 开始倒计时
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  }
}

// 选择生日
const handleBirthdayChange = (e: any) => {
  birthday.value = e.detail.value
}

// 注册
const handleRegister = async () => {
  if (!childName.value.trim()) {
    showError('请输入孩子姓名')
    return
  }

  if (!validatePhone(phone.value)) {
    showError('请输入正确的手机号')
    return
  }

  if (!validateCode(code.value)) {
    showError('请输入4-6位验证码')
    return
  }

  if (password.value.length < 6 || password.value.length > 20) {
    showError('密码长度为6-20位')
    return
  }

  if (!birthday.value) {
    showError('请选择孩子生日')
    return
  }

  if (!agreed.value) {
    showError('请先同意用户协议')
    return
  }

  uni.showLoading({ title: '注册中...' })

  try {
    const success = await userStore.register({
      phone: phone.value,
      code: code.value,
      password: password.value,
      childName: childName.value,
      gender: gender.value,
      birthday: birthday.value
    })

    uni.hideLoading()

    if (success) {
      showSuccess('注册成功')
      // 延迟跳转，让用户看到成功提示
      setTimeout(() => {
        uni.redirectTo({ url: '/pages/user/login' })
      }, 1500)
    }
  } catch (error) {
    uni.hideLoading()
    showError('注册失败，请重试')
  }
}

// 返回登录
const goLogin = () => {
  uni.redirectTo({ url: '/pages/user/login' })
}

// 同意协议
const handleAgreementChange = (e: any) => {
  agreed.value = e.detail.value.length > 0
}

// 查看协议
const openAgreement = (type: 'user' | 'privacy') => {
  const url = type === 'user' ? '/pages/webview?url=xxx' : '/pages/webview?url=xxx'
  uni.navigateTo({ url })
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #EEF2FF 0%, #FFFFFF 100%);
  padding: 0 48rpx;
  position: relative;
  overflow: hidden;
}

.register-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.5;
}

.circle-1 {
  width: 400rpx;
  height: 400rpx;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(59, 130, 246, 0));
  top: -100rpx;
  right: -100rpx;
}

.circle-2 {
  width: 300rpx;
  height: 300rpx;
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.2), rgba(139, 92, 246, 0));
  bottom: 200rpx;
  left: -150rpx;
}

.logo-section {
  padding-top: 120rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo {
  width: 140rpx;
  height: 140rpx;
  margin-bottom: 24rpx;
}

.app-name {
  font-size: 44rpx;
  font-weight: bold;
  color: #1F2937;
  margin-bottom: 12rpx;
}

.app-slogan {
  font-size: 28rpx;
  color: #6B7280;
}

.register-form {
  margin-top: 60rpx;
}

.form-item {
  margin-bottom: 32rpx;

  .label {
    display: block;
    font-size: 28rpx;
    color: #374151;
    margin-bottom: 16rpx;
  }

  .input {
    width: 100%;
    height: 96rpx;
    background: #FFFFFF;
    border: 2rpx solid #E5E7EB;
    border-radius: 16rpx;
    padding: 0 32rpx;
    font-size: 30rpx;

    &:focus {
      border-color: #3B82F6;
    }
  }
}

// 验证码输入行
.code-item {
  display: flex;
  align-items: center;

  .input {
    flex: 1;
  }

  .code-btn {
    flex-shrink: 0;
    margin-left: 20rpx;
    width: 220rpx;
    height: 96rpx;
    background: #FFFFFF;
    border: 2rpx solid #3B82F6;
    border-radius: 16rpx;
    font-size: 26rpx;
    color: #3B82F6;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0;

    &[disabled] {
      color: #9CA3AF;
      border-color: #E5E7EB;
    }
  }
}

// 密码输入行
.password-item {
  display: flex;
  align-items: center;
  position: relative;

  .input {
    flex: 1;
    padding-right: 100rpx;
  }

  .password-toggle {
    position: absolute;
    right: 32rpx;
    font-size: 26rpx;
    color: #3B82F6;
  }
}

.gender-select {
  display: flex;
  gap: 24rpx;

  .gender-option {
    flex: 1;
    height: 96rpx;
    background: #FFFFFF;
    border: 2rpx solid #E5E7EB;
    border-radius: 16rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 30rpx;
    color: #6B7280;

    &.active {
      border-color: #3B82F6;
      background: #EBF5FF;
      color: #3B82F6;
    }
  }
}

.picker-value {
  width: 100%;
  height: 96rpx;
  background: #FFFFFF;
  border: 2rpx solid #E5E7EB;
  border-radius: 16rpx;
  padding: 0 32rpx;
  font-size: 30rpx;
  line-height: 96rpx;
  color: #374151;
}

.register-btn {
  margin-top: 48rpx;
  background: linear-gradient(135deg, #3B82F6 0%, #6366F1 100%);
  box-shadow: 0 8rpx 24rpx rgba(59, 130, 246, 0.3);
}

.back-login {
  margin-top: 32rpx;
  text-align: center;
  font-size: 28rpx;
  color: #6B7280;
}

.link {
  color: #3B82F6;
}

.agreement {
  margin-top: 32rpx;
  padding: 0 16rpx;
}

.agreement-label {
  display: flex;
  align-items: flex-start;
}
</style>
