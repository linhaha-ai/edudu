import request from '@/utils/request'

// 查询每日任务分配列表
export function listDaily(query) {
  return request({
    url: '/edu/daily/list',
    method: 'get',
    params: query
  })
}

// 查询每日任务分配详细
export function getDaily(id) {
  return request({
    url: '/edu/daily/' + id,
    method: 'get'
  })
}

// 新增每日任务分配
export function addDaily(data) {
  return request({
    url: '/edu/daily',
    method: 'post',
    data: data
  })
}

// 修改每日任务分配
export function updateDaily(data) {
  return request({
    url: '/edu/daily',
    method: 'put',
    data: data
  })
}

// 删除每日任务分配
export function delDaily(id) {
  return request({
    url: '/edu/daily/' + id,
    method: 'delete'
  })
}
