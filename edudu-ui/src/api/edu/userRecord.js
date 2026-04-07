import request from '@/utils/request'

// 查询用户任务记录列表
export function listUserRecord(query) {
  return request({
    url: '/edu/userRecord/list',
    method: 'get',
    params: query
  })
}

// 查询用户任务记录详细
export function getUserRecord(id) {
  return request({
    url: '/edu/userRecord/' + id,
    method: 'get'
  })
}

// 新增用户任务记录
export function addUserRecord(data) {
  return request({
    url: '/edu/userRecord',
    method: 'post',
    data: data
  })
}

// 修改用户任务记录
export function updateUserRecord(data) {
  return request({
    url: '/edu/userRecord',
    method: 'put',
    data: data
  })
}

// 删除用户任务记录
export function delUserRecord(id) {
  return request({
    url: '/edu/userRecord/' + id,
    method: 'delete'
  })
}
