import request from '@/utils/request'

// 查询每日任务模板列表
export function listTask(query) {
  return request({
    url: '/edu/task/list',
    method: 'get',
    params: query
  })
}

// 查询每日任务模板详细
export function getTask(id) {
  return request({
    url: '/edu/task/' + id,
    method: 'get'
  })
}

// 新增每日任务模板
export function addTask(data) {
  return request({
    url: '/edu/task',
    method: 'post',
    data: data
  })
}

// 修改每日任务模板
export function updateTask(data) {
  return request({
    url: '/edu/task',
    method: 'put',
    data: data
  })
}

// 删除每日任务模板
export function delTask(id) {
  return request({
    url: '/edu/task/' + id,
    method: 'delete'
  })
}
