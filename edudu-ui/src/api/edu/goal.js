import request from '@/utils/request'

// 查询教育目标字典列表
export function listGoal(query) {
  return request({
    url: '/edu/goal/list',
    method: 'get',
    params: query
  })
}

// 查询教育目标字典详细
export function getGoal(id) {
  return request({
    url: '/edu/goal/' + id,
    method: 'get'
  })
}

// 新增教育目标字典
export function addGoal(data) {
  return request({
    url: '/edu/goal',
    method: 'post',
    data: data
  })
}

// 修改教育目标字典
export function updateGoal(data) {
  return request({
    url: '/edu/goal',
    method: 'put',
    data: data
  })
}

// 删除教育目标字典
export function delGoal(id) {
  return request({
    url: '/edu/goal/' + id,
    method: 'delete'
  })
}
