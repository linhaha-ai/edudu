import request from '@/utils/request'

// 查询孩子信息列表
export function listChild(query) {
  return request({
    url: '/edu/child/list',
    method: 'get',
    params: query
  })
}

// 查询孩子信息详细
export function getChild(childId) {
  return request({
    url: '/edu/child/' + childId,
    method: 'get'
  })
}

// 新增孩子信息
export function addChild(data) {
  return request({
    url: '/edu/child',
    method: 'post',
    data: data
  })
}

// 修改孩子信息
export function updateChild(data) {
  return request({
    url: '/edu/child',
    method: 'put',
    data: data
  })
}

// 删除孩子信息
export function delChild(childId) {
  return request({
    url: '/edu/child/' + childId,
    method: 'delete'
  })
}
