import request from '@/utils/request'

// 查询AI用户长期记忆列表
export function listUserMemory(query) {
  return request({
    url: '/edu/userMemory/list',
    method: 'get',
    params: query
  })
}

// 查询AI用户长期记忆详细
export function getUserMemory(id) {
  return request({
    url: '/edu/userMemory/' + id,
    method: 'get'
  })
}

// 新增AI用户长期记忆
export function addUserMemory(data) {
  return request({
    url: '/edu/userMemory',
    method: 'post',
    data: data
  })
}

// 修改AI用户长期记忆
export function updateUserMemory(data) {
  return request({
    url: '/edu/userMemory',
    method: 'put',
    data: data
  })
}

// 删除AI用户长期记忆
export function delUserMemory(id) {
  return request({
    url: '/edu/userMemory/' + id,
    method: 'delete'
  })
}
