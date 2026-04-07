import request from '@/utils/request'

// 查询成长记录类型字典列表
export function listType(query) {
  return request({
    url: '/edu/type/list',
    method: 'get',
    params: query
  })
}

// 查询成长记录类型字典详细
export function getType(id) {
  return request({
    url: '/edu/type/' + id,
    method: 'get'
  })
}

// 新增成长记录类型字典
export function addType(data) {
  return request({
    url: '/edu/type',
    method: 'post',
    data: data
  })
}

// 修改成长记录类型字典
export function updateType(data) {
  return request({
    url: '/edu/type',
    method: 'put',
    data: data
  })
}

// 删除成长记录类型字典
export function delType(id) {
  return request({
    url: '/edu/type/' + id,
    method: 'delete'
  })
}
