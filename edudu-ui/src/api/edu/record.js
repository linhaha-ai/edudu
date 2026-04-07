import request from '@/utils/request'

// 查询故事记录列表
export function listRecord(query) {
  return request({
    url: '/edu/record/list',
    method: 'get',
    params: query
  })
}

// 查询故事记录详细
export function getRecord(id) {
  return request({
    url: '/edu/record/' + id,
    method: 'get'
  })
}

// 新增故事记录
export function addRecord(data) {
  return request({
    url: '/edu/record',
    method: 'post',
    data: data
  })
}

// 修改故事记录
export function updateRecord(data) {
  return request({
    url: '/edu/record',
    method: 'put',
    data: data
  })
}

// 删除故事记录
export function delRecord(id) {
  return request({
    url: '/edu/record/' + id,
    method: 'delete'
  })
}
