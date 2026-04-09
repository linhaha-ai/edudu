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
}

/**
 * 生成故事
 */
export const generateStory = (params: StoryRequest) => {
  return post<ApiResponse<Story>>('/api/story/generate', params)
}

/**
 * 获取故事详情
 */
export const getStoryDetail = (id: number) => {
  return get<ApiResponse<Story>>(`/api/story/${id}`)
}

/**
 * 获取我的故事列表
 */
export const getMyStories = (params?: { page?: number; pageSize?: number }) => {
  return get<ApiResponse<{ list: Story[]; total: number }>>('/api/story/my', params)
}

/**
 * 收藏故事
 */
export const favoriteStory = (id: number) => {
  return post<ApiResponse<null>>(`/api/story/${id}/favorite`)
}

/**
 * 取消收藏
 */
export const unfavoriteStory = (id: number) => {
  return post<ApiResponse<null>>(`/api/story/${id}/unfavorite`)
}

/**
 * 获取收藏列表
 */
export const getFavoriteStories = (params?: { page?: number; pageSize?: number }) => {
  return get<ApiResponse<{ list: Story[]; total: number }>>('/api/story/favorites', params)
}
