import request from '@/utils/request'

// 查询AI对话摘要列表
export function listConversationSummary(query) {
  return request({
    url: '/edu/conversationSummary/list',
    method: 'get',
    params: query
  })
}

// 查询AI对话摘要详细
export function getConversationSummary(id) {
  return request({
    url: '/edu/conversationSummary/' + id,
    method: 'get'
  })
}

// 新增AI对话摘要
export function addConversationSummary(data) {
  return request({
    url: '/edu/conversationSummary',
    method: 'post',
    data: data
  })
}

// 修改AI对话摘要
export function updateConversationSummary(data) {
  return request({
    url: '/edu/conversationSummary',
    method: 'put',
    data: data
  })
}

// 删除AI对话摘要
export function delConversationSummary(id) {
  return request({
    url: '/edu/conversationSummary/' + id,
    method: 'delete'
  })
}
