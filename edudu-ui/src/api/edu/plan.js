import request from '@/utils/request'

// 查询订阅计划配置列表
export function listPlan(query) {
  return request({
    url: '/edu/plan/list',
    method: 'get',
    params: query
  })
}

// 查询订阅计划配置详细
export function getPlan(id) {
  return request({
    url: '/edu/plan/' + id,
    method: 'get'
  })
}

// 新增订阅计划配置
export function addPlan(data) {
  return request({
    url: '/edu/plan',
    method: 'post',
    data: data
  })
}

// 修改订阅计划配置
export function updatePlan(data) {
  return request({
    url: '/edu/plan',
    method: 'put',
    data: data
  })
}

// 删除订阅计划配置
export function delPlan(id) {
  return request({
    url: '/edu/plan/' + id,
    method: 'delete'
  })
}
