import request from '@/utils/request'

// 查询故事记录列表
export function listStoreRecord(query) {
  return request({
    url: '/edu/storeRecord/list',
    method: 'get',
    params: query
  })
}

// 查询故事记录详细
export function getStoreRecord(id) {
  return request({
    url: '/edu/storeRecord/' + id,
    method: 'get'
  })
}

// 新增故事记录
export function addStoreRecord(data) {
  return request({
    url: '/edu/storeRecord',
    method: 'post',
    data: data
  })
}

// 修改故事记录
export function updateStoreRecord(data) {
  return request({
    url: '/edu/storeRecord',
    method: 'put',
    data: data
  })
}

// 删除故事记录
export function delStoreRecord(id) {
  return request({
    url: '/edu/storeRecord/' + id,
    method: 'delete'
  })
}
