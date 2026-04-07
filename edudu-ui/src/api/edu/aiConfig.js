import request from '@/utils/request'

// 查询AI配置列表
export function listAiConfig(query) {
  return request({
    url: '/edu/aiConfig/list',
    method: 'get',
    params: query
  })
}

// 查询AI配置详细
export function getAiConfig(id) {
  return request({
    url: '/edu/aiConfig/' + id,
    method: 'get'
  })
}

// 新增AI配置
export function addAiConfig(data) {
  return request({
    url: '/edu/aiConfig',
    method: 'post',
    data: data
  })
}

// 修改AI配置
export function updateAiConfig(data) {
  return request({
    url: '/edu/aiConfig',
    method: 'put',
    data: data
  })
}

// 删除AI配置
export function delAiConfig(id) {
  return request({
    url: '/edu/aiConfig/' + id,
    method: 'delete'
  })
}
