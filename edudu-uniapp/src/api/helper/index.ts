/**
 * 家长助手 API
 */
import { get, post } from '../request'
import type { ApiResponse } from '../user/types'

export interface ParentQuestion {
  id: number
  userId: number
  childId: number
  question: string
  createdAt: string
}

export interface ParentAnswer {
  id: number
  questionId: number
  answer: string
  ageSuggestions?: string[]
  actionPlans?: string[]
  createdAt: string
}

/**
 * 提交问题
 */
export const submitQuestion = (params: { childId: number; question: string }) => {
  return post<ApiResponse<{ question: ParentQuestion; answer: ParentAnswer }>>('/api/parent/question', params)
}

/**
 * 获取问答历史
 */
export const getQuestionHistory = (params?: { page?: number; pageSize?: number }) => {
  return get<ApiResponse<{ list: (ParentQuestion & { answer: ParentAnswer })[]; total: number }>>(
    '/api/parent/history',
    params
  )
}

/**
 * 获取问答详情
 */
export const getQuestionDetail = (id: number) => {
  return get<ApiResponse<ParentQuestion & { answer: ParentAnswer }>>(`/api/parent/question/${id}`)
}
