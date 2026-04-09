<template>
  <view class="about-page">
    <view class="logo-section">
      <image src="/static/images/logo.png" class="logo" />
      <text class="app-name">AI早教陪伴</text>
      <text class="version">版本 1.0.0</text>
    </view>

    <view class="about-content">
      <view class="about-item">
        <text class="about-title">关于我们</text>
        <text class="about-text">AI早教陪伴是一款专注于0-6岁儿童早期教育的智能应用，帮助家长科学育儿，让孩子健康快乐成长。</text>
      </view>

      <view class="about-item">
        <text class="about-title">联系方式</text>
        <text class="about-text">客服热线：400-000-0000</text>
        <text class="about-text">邮箱：support@edudu.com</text>
      </view>

      <view class="about-item">
        <text class="about-title">用户协议</text>
        <text class="about-link">查看用户协议 ›</text>
      </view>

      <view class="about-item">
        <text class="about-title">隐私政策</text>
        <text class="about-link">查看隐私政策 ›</text>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.about-page { min-height: 100vh; background: #F8F8F8; }
.logo-section { background: linear-gradient(135deg, #3B82F6 0%, #6366F1 100%); padding: 80rpx 0; text-align: center; }
.logo { width: 160rpx; height: 160rpx; margin-bottom: 24rpx; }
.app-name { font-size: 40rpx; font-weight: bold; color: #fff; display: block; margin-bottom: 12rpx; }
.version { font-size: 26rpx; color: rgba(255,255,255,0.8); }
.about-content { padding: 32rpx; }
.about-item { background: #fff; border-radius: 20rpx; padding: 32rpx; margin-bottom: 24rpx; }
.about-title { font-size: 30rpx; font-weight: bold; color: #1F2937; display: block; margin-bottom: 16rpx; }
.about-text { font-size: 28rpx; color: #6B7280; display: block; margin-bottom: 8rpx; line-height: 1.6; }
.about-link { font-size: 28rpx; color: #3B82F6; }
</style>
