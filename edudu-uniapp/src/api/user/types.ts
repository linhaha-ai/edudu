/**
 * 用户相关类型定义
 */
export interface UserInfo {
  id: number
  phone: string
  nickname: string
  avatar: string
  children?: Child[]
}

export interface Child {
  id: number
  name: string
  gender: 1 | 2 // 1男 2女
  birthDate: string
  avatar?: string
  personality?: string
  createdAt?: string
}

export interface LoginParams {
  phone: string
  code: string
}

export interface SmsParams {
  phone: string
  type: 'login' | 'register'
}

export interface WechatLoginParams {
  code: string
  encryptedData?: string
  iv?: string
}

export interface ApiResponse<T = any> {
  code: number
  data: T
  message: string
}
