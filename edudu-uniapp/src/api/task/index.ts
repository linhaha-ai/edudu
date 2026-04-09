/**
 * 任务 API
 */
import { get, post } from '../request'
import type { ApiResponse } from '../user/types'

export interface DailyTask {
  id: number
  childId: number
  title: string
  description: string
  type: 'cognitive' | 'language' | 'motor' | 'art' // 认知/语言/运动/艺术
  duration: number // 分钟
  speakingScript: string // 互动话术
  steps: string[] // 操作步骤
  isCompleted: boolean
  completedAt?: string
  completedNote?: string
}

export interface TaskRecord {
  id: number
  taskId: number
  childId: number
  completedAt: string
  note?: string
}

/**
 * 获取今日任务
 */
export const getTodayTasks = (childId: number) => {
  return get<ApiResponse<DailyTask[]>>('/api/task/today', { childId })
}

/**
 * 获取任务详情
 */
export const getTaskDetail = (id: number) => {
  return get<ApiResponse<DailyTask>>(`/api/task/${id}`)
}

/**
 * 完成任务
 */
export const completeTask = (id: number, note?: string) => {
  return post<ApiResponse<null>>(`/api/task/${id}/complete`, { note })
}

/**
 * 获取任务统计
 */
export const getTaskStats = (childId: number) => {
  return get<ApiResponse<TaskStatsData>>('/api/task/stats', { childId })
}

export interface TaskStatsData {
  todayTotal: number
  todayCompleted: number
  weekStats: { date: string; completed: number; total: number }[]
  typeStats: { type: string; completed: number; total: number }[]
  streakDays: number // 连续完成天数
}
