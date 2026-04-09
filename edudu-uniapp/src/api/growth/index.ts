/**
 * 成长记录 API
 */
import { get, post } from '../request'
import type { ApiResponse } from '../user/types'

export type RecordType = 'sleep' | 'diet' | 'learning'

export interface GrowthRecord {
  id: number
  childId: number
  type: RecordType
  recordDate: string
  createdAt: string
  // 睡眠记录
  sleepStart?: string
  sleepEnd?: string
  sleepDuration?: number
  // 饮食记录
  mealType?: string // 早餐/午餐/晚餐/加餐
  foodDescription?: string
  amount?: string
  // 学习记录
  learningContent?: string
  learningDuration?: number
  masteryLevel?: number // 掌握程度 1-5
}

export interface GrowthReport {
  id: number
  childId: number
  period: string // 周期：week/month
  startDate: string
  endDate: string
  content: string
  aiSuggestions: string[]
  createdAt: string
}

/**
 * 获取成长记录列表
 */
export const getGrowthRecords = (
  childId: number,
  params?: { type?: RecordType; page?: number; pageSize?: number }
) => {
  return get<ApiResponse<{ list: GrowthRecord[]; total: number }>>('/growth/records', {
    childId,
    ...params,
  })
}

/**
 * 添加成长记录
 */
export const addGrowthRecord = (params: Omit<GrowthRecord, 'id' | 'createdAt'>) => {
  return post<ApiResponse<GrowthRecord>>('/growth/record/add', params)
}

/**
 * 更新成长记录
 */
export const updateGrowthRecord = (id: number, params: Partial<GrowthRecord>) => {
  return post<ApiResponse<null>>(`/growth/record/${id}/update`, params)
}

/**
 * 删除成长记录
 */
export const deleteGrowthRecord = (id: number) => {
  return post<ApiResponse<null>>(`/growth/record/${id}/delete`)
}

/**
 * 获取成长统计
 */
export const getGrowthStats = (childId: number, period: 'week' | 'month' = 'week') => {
  return get<ApiResponse<GrowthStatsData>>('/growth/stats', { childId, period })
}

export interface GrowthStatsData {
  sleepAvg: number
  dietCount: number
  learningHours: number
  records: { date: string; type: RecordType; value: number }[]
}

/**
 * 生成 AI 成长报告
 */
export const generateGrowthReport = (childId: number, period: 'week' | 'month') => {
  return post<ApiResponse<GrowthReport>>('/growth/report/generate', { childId, period })
}

/**
 * 获取成长报告
 */
export const getGrowthReports = (childId: number) => {
  return get<ApiResponse<GrowthReport[]>>('/growth/reports', { childId })
}
