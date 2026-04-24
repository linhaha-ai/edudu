import request from '@/utils/request'

// 查询任务完成记录列表
export function listTaskRecord(query) {
  return request({
    url: '/edu/taskRecord/list',
    method: 'get',
    params: query
  })
}

// 查询任务完成记录详细
export function getTaskRecord(id) {
  return request({
    url: '/edu/taskRecord/' + id,
    method: 'get'
  })
}

// 新增任务完成记录
export function addTaskRecord(data) {
  return request({
    url: '/edu/taskRecord',
    method: 'post',
    data: data
  })
}

// 修改任务完成记录
export function updateTaskRecord(data) {
  return request({
    url: '/edu/taskRecord',
    method: 'put',
    data: data
  })
}

// 删除任务完成记录
export function delTaskRecord(id) {
  return request({
    url: '/edu/taskRecord/' + id,
    method: 'delete'
  })
}
