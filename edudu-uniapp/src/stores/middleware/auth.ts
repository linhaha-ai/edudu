/**
 * 路由守卫中间件
 */
import type { NavigationGuardNext, Route } from '@/types/router'

/**
 * 登录守卫
 */
export const requireAuth = (to: any, from: any, next: any) => {
  const token = uni.getStorageSync('edudu-user')?.token

  if (!token) {
    uni.reLaunch({
      url: '/pages/user/login',
    })
    return
  }

  next()
}

/**
 * 访客守卫（已登录则跳转首页）
 */
export const guestOnly = (to: any, from: any, next: any) => {
  const token = uni.getStorageSync('edudu-user')?.token

  if (token) {
    uni.switchTab({
      url: '/pages/index/index',
    })
    return
  }

  next()
}
