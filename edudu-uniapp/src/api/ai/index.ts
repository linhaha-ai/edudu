import { get, post } from '../request'
import type { ApiResponse } from '../user/types'

export interface ChatMessage {
  id?: number
  role: 'user' | 'assistant'
  content: string
  timestamp?: string
}

export interface ChatRequest {
  message: string
  childId?: number
  conversationId?: string
}

export interface ChatResponse {
  content: string
  conversationId: string
}

export interface ConversationSummary {
  id: number
  childId: number
  summary: string
  createdAt: string
}

export interface UserMemory {
  id: number
  childId: number
  key: string
  value: string
  category: string
  importance: number
  createdAt: string
}

export const sendChatMessage = (data: ChatRequest) => {
  return post<ApiResponse<ChatResponse>>('/ai/chat', data)
}

export const sendParentMessage = (data: ChatRequest) => {
  return post<ApiResponse<ChatResponse>>('/ai/parent', data)
}

export const getConversationHistory = (conversationId: string) => {
  return get<ApiResponse<ChatMessage[]>>(`/ai/history/${conversationId}`)
}

export const getRecentConversations = (childId: number) => {
  return get<ApiResponse<ConversationSummary[]>>('/ai/conversations', { childId })
}

export const getUserMemories = (childId: number) => {
  return get<ApiResponse<UserMemory[]>>('/ai/memories', { childId })
}

export const createUserMemory = (data: Omit<UserMemory, 'id' | 'createdAt'>) => {
  return post<ApiResponse<UserMemory>>('/ai/memories', data)
}

export const deleteUserMemory = (id: number) => {
  return post<ApiResponse<null>>(`/ai/memories/${id}/delete`, {})
}

export const getConversationSummary = (childId: number) => {
  return get<ApiResponse<ConversationSummary>>('/ai/summary', { childId })
}
