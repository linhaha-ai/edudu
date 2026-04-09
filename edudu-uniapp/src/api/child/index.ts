/**
 * 孩子管理 API
 */
import { get, post } from '../request'
import type { ApiResponse } from '../user/types'
import type { Child } from '../user/types'

/**
 * 获取孩子列表
 */
export const getChildrenList = () => {
  return get<ApiResponse<Child[]>>('/child/list')
}

/**
 * 添加孩子
 */
export const addChild = (params: Omit<Child, 'id' | 'createdAt'>) => {
  return post<ApiResponse<Child>>('/child/add', params)
}

/**
 * 更新孩子信息
 */
export const updateChild = (id: number, params: Partial<Child>) => {
  return post<ApiResponse<null>>(`/child/${id}/update`, params)
}

/**
 * 删除孩子
 */
export const deleteChild = (id: number) => {
  return post<ApiResponse<null>>(`/child/${id}/delete`)
}
