import request from '@/utils/request'

// 查询故事互动问题列表
export function listInteraction(query) {
  return request({
    url: '/edu/interaction/list',
    method: 'get',
    params: query
  })
}

// 查询故事互动问题详细
export function getInteraction(id) {
  return request({
    url: '/edu/interaction/' + id,
    method: 'get'
  })
}

// 新增故事互动问题
export function addInteraction(data) {
  return request({
    url: '/edu/interaction',
    method: 'post',
    data: data
  })
}

// 修改故事互动问题
export function updateInteraction(data) {
  return request({
    url: '/edu/interaction',
    method: 'put',
    data: data
  })
}

// 删除故事互动问题
export function delInteraction(id) {
  return request({
    url: '/edu/interaction/' + id,
    method: 'delete'
  })
}
