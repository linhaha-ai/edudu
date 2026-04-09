<template>
  <view class="login-container">
    <!-- 背景装饰 -->
    <view class="login-bg">
      <view class="bg-circle circle-1"></view>
      <view class="bg-circle circle-2"></view>
    </view>

    <!-- Logo 区域 -->
    <view class="logo-section">
      <image class="logo" src="/static/images/logo.png" mode="aspectFit" />
      <text class="app-name">AI早教陪伴</text>
      <text class="app-slogan">让每个孩子都能快乐成长</text>
    </view>

    <!-- 登录表单 -->
    <view class="login-form">
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
      <view class="form-item">
        <input
          v-model="code"
          type="number"
          maxlength="6"
          placeholder="请输入验证码"
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

      <!-- 登录按钮 -->
      <button class="btn btn-primary btn-block btn-lg login-btn" @click="handleLogin">
        登录
      </button>

      <!-- 微信登录 -->
      #ifdef MP-WEIXIN
      <button class="btn btn-default btn-block wechat-btn" @click="handleWechatLogin">
        <image class="wechat-icon" src="/static/images/wechat.png" />
        微信一键登录
      </button>
      #endif
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
const phone = ref('')
const code = ref('')
const agreed = ref(false)
const countdown = ref(0)

// 发送验证码
const handleSendCode = async () => {
  if (!validatePhone(phone.value)) {
    showError('请输入正确的手机号')
    return
  }

  const success = await userStore.sendSms({ phone: phone.value, type: 'login' })
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

// 登录
const handleLogin = async () => {
  if (!validatePhone(phone.value)) {
    showError('请输入正确的手机号')
    return
  }

  if (!validateCode(code.value)) {
    showError('请输入4-6位验证码')
    return
  }

  if (!agreed.value) {
    showError('请先同意用户协议')
    return
  }

  uni.showLoading({ title: '登录中...' })
  const success = await userStore.loginWithSms({ phone: phone.value, code: code.value })
  uni.hideLoading()

  if (success) {
    showSuccess('登录成功')
    // 跳转到首页
    uni.switchTab({ url: '/pages/index/index' })
  } else {
    showError('登录失败，请重试')
  }
}

// 微信登录
const handleWechatLogin = () => {
  // #ifdef MP-WEIXIN
  uni.getUserProfile({
    desc: '用于完善用户资料',
    success: async (res) => {
      // 获取登录凭证
      uni.login({
        provider: 'weixin',
        success: async (loginRes) => {
          const success = await userStore.loginWithWechat(loginRes.code)
          if (success) {
            showSuccess('登录成功')
            uni.switchTab({ url: '/pages/index/index' })
          }
        },
        fail: () => {
          showError('微信登录失败')
        },
      })
    },
    fail: () => {
      showError('获取用户信息失败')
    },
  })
  // #endif
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
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #EEF2FF 0%, #FFFFFF 100%);
  padding: 0 48rpx;
  position: relative;
  overflow: hidden;
}

.login-bg {
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
  padding-top: 160rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo {
  width: 160rpx;
  height: 160rpx;
  margin-bottom: 24rpx;
}

.app-name {
  font-size: 48rpx;
  font-weight: bold;
  color: #1F2937;
  margin-bottom: 12rpx;
}

.app-slogan {
  font-size: 28rpx;
  color: #6B7280;
}

.login-form {
  margin-top: 80rpx;
}

.form-item {
  display: flex;
  align-items: center;
  margin-bottom: 32rpx;

  .input {
    flex: 1;
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

  .code-btn {
    margin-left: 24rpx;
    width: 200rpx;
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

.login-btn {
  margin-top: 48rpx;
  background: linear-gradient(135deg, #3B82F6 0%, #6366F1 100%);
  box-shadow: 0 8rpx 24rpx rgba(59, 130, 246, 0.3);
}

.wechat-btn {
  margin-top: 32rpx;
  background: #07C160;
  color: #FFFFFF;
  display: flex;
  align-items: center;
  justify-content: center;

  .wechat-icon {
    width: 44rpx;
    height: 44rpx;
    margin-right: 12rpx;
  }
}

.agreement {
  margin-top: 48rpx;
  padding: 0 16rpx;
}

.agreement-label {
  display: flex;
  align-items: flex-start;
}

.link {
  color: #3B82F6;
}
</style>
