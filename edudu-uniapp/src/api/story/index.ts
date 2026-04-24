/**
 * 故事 API
 */
import { get, post } from '../request'
import type { ApiResponse } from '../user/types'

export interface StoryRequest {
  childId: number
  age: number
  personality: string
  scene: string
  storyType?: string
}

export interface BedtimeStoryRequest {
  age: number
  gender: string
  goal?: string
}

export interface EmotionStoryRequest {
  age: number
  gender: string
  emotion: string
}

export interface Story {
  id: number
  childId: number
  title: string
  content: string
  age: number
  personality: string
  scene: string
  storyType: string
  isFavorite: boolean
  createdAt: string
  moral?: string
  interaction?: string[]
}

/**
 * 生成故事
 */
export const generateStory = (params: StoryRequest) => {
  return post<ApiResponse<Story>>('/story/generate', params)
}

/**
 * 生成睡前故事
 */
export const generateBedtimeStory = (params: BedtimeStoryRequest) => {
  return post<ApiResponse<Story>>('/story/bedtime', params)
}

/**
 * 生成情绪引导故事
 */
export const generateEmotionStory = (params: EmotionStoryRequest) => {
  return post<ApiResponse<Story>>('/story/emotion', params)
}

/**
 * 获取故事详情
 */
export const getStoryDetail = (id: number) => {
  return get<ApiResponse<Story>>(`/story/${id}`)
}

/**
 * 获取我的故事列表
 */
export const getMyStories = (params?: { page?: number; pageSize?: number; childId?: number }) => {
  return get<ApiResponse<{ list: Story[]; total: number }>>('/story/my', params)
}

/**
 * 收藏故事
 */
export const favoriteStory = (id: number) => {
  return post<ApiResponse<null>>(`/story/${id}/favorite`)
}

/**
 * 取消收藏
 */
export const unfavoriteStory = (id: number) => {
  return post<ApiResponse<null>>(`/story/${id}/unfavorite`)
}

/**
 * 获取收藏列表
 */
export const getFavoriteStories = (params?: { page?: number; pageSize?: number }) => {
  return get<ApiResponse<{ list: Story[]; total: number }>>('/story/favorites', params)
}
