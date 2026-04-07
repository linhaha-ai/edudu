import request from '@/utils/request'

// 查询订阅列表
export function listSubscription(query) {
  return request({
    url: '/edu/subscription/list',
    method: 'get',
    params: query
  })
}

// 查询订阅详细
export function getSubscription(id) {
  return request({
    url: '/edu/subscription/' + id,
    method: 'get'
  })
}

// 新增订阅
export function addSubscription(data) {
  return request({
    url: '/edu/subscription',
    method: 'post',
    data: data
  })
}

// 修改订阅
export function updateSubscription(data) {
  return request({
    url: '/edu/subscription',
    method: 'put',
    data: data
  })
}

// 删除订阅
export function delSubscription(id) {
  return request({
    url: '/edu/subscription/' + id,
    method: 'delete'
  })
}
