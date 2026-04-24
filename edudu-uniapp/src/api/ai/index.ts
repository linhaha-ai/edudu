
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

export const sendChatMessage = (data: ChatRequest) =&gt; {
  return post&lt;ApiResponse&lt;ChatResponse&gt;&gt;('/ai/chat', data)
}

export const sendParentMessage = (data: ChatRequest) =&gt; {
  return post&lt;ApiResponse&lt;ChatResponse&gt;&gt;('/ai/parent', data)
}

export const getConversationHistory = (conversationId: string) =&gt; {
  return get&lt;ApiResponse&lt;ChatMessage[]&gt;&gt;(`/ai/history/${conversationId}`)
}

export const getRecentConversations = (childId: number) =&gt; {
  return get&lt;ApiResponse&lt;ConversationSummary[]&gt;&gt;('/ai/conversations', { childId })
}

export const getUserMemories = (childId: number) =&gt; {
  return get&lt;ApiResponse&lt;UserMemory[]&gt;&gt;('/ai/memories', { childId })
}

export const createUserMemory = (data: Omit&lt;UserMemory, 'id' | 'createdAt'&gt;) =&gt; {
  return post&lt;ApiResponse&lt;UserMemory&gt;&gt;('/ai/memories', data)
}

export const deleteUserMemory = (id: number) =&gt; {
  return post&lt;ApiResponse&lt;null&gt;&gt;(`/ai/memories/${id}/delete`, {})
}

export const getConversationSummary = (childId: number) =&gt; {
  return get&lt;ApiResponse&lt;ConversationSummary&gt;&gt;('/ai/summary', { childId })
}
