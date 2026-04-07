import request from '@/utils/request'

// 查询前端用户列表
export function listUser(query) {
  return request({
    url: '/edu/user/list',
    method: 'get',
    params: query
  })
}

// 查询前端用户详细
export function getUser(userId) {
  return request({
    url: '/edu/user/' + userId,
    method: 'get'
  })
}

// 新增前端用户
export function addUser(data) {
  return request({
    url: '/edu/user',
    method: 'post',
    data: data
  })
}

// 修改前端用户
export function updateUser(data) {
  return request({
    url: '/edu/user',
    method: 'put',
    data: data
  })
}

// 删除前端用户
export function delUser(userId) {
  return request({
    url: '/edu/user/' + userId,
    method: 'delete'
  })
}
