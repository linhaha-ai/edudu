/**
 * 用户 API
 */
import { get, post } from '../request'
import type { UserInfo, LoginParams, SmsParams, WechatLoginParams, RegisterParams } from './types'

/**
 * 获取用户信息
 */
export const getUserInfo = () => {
  return get<{ code: number; data: UserInfo; message: string }>('/auth/info')
}

/**
 * 发送验证码
 */
export const sendSmsCode = (params: SmsParams) => {
  return post<{ code: number; message: string }>('/auth/sms/send', params)
}

/**
 * 短信登录
 */
export const loginBySms = (params: LoginParams) => {
  return post<{ code: number; data: { token: string }; message: string }>('/auth/login/sms', params)
}

/**
 * 用户注册
 */
export const register = (params: RegisterParams) => {
  return post<{ code: number; data: { token: string }; message: string }>('/auth/register', params)
}

/**
 * 微信登录
 */
export const loginByWechat = (params: WechatLoginParams) => {
  return post<{ code: number; data: { token: string }; message: string }>('/auth/login/wechat', params)
}

/**
 * 退出登录
 */
export const logout = () => {
  return post<{ code: number; message: string }>('/auth/logout')
}

/**
 * 更新用户信息
 */
export const updateUserInfo = (params: Partial<UserInfo>) => {
  return post<{ code: number; message: string }>('/auth/user/update', params)
}
