import request from '@/utils/request'

// 查询AI配置列表
export function listConfig(query) {
  return request({
    url: '/edu/config/list',
    method: 'get',
    params: query
  })
}

// 查询AI配置详细
export function getConfig(id) {
  return request({
    url: '/edu/config/' + id,
    method: 'get'
  })
}

// 新增AI配置
export function addConfig(data) {
  return request({
    url: '/edu/config',
    method: 'post',
    data: data
  })
}

// 修改AI配置
export function updateConfig(data) {
  return request({
    url: '/edu/config',
    method: 'put',
    data: data
  })
}

// 删除AI配置
export function delConfig(id) {
  return request({
    url: '/edu/config/' + id,
    method: 'delete'
  })
}
