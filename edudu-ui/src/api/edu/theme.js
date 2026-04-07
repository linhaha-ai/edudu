import request from '@/utils/request'

// 查询任务主题字典列表
export function listTheme(query) {
  return request({
    url: '/edu/theme/list',
    method: 'get',
    params: query
  })
}

// 查询任务主题字典详细
export function getTheme(id) {
  return request({
    url: '/edu/theme/' + id,
    method: 'get'
  })
}

// 新增任务主题字典
export function addTheme(data) {
  return request({
    url: '/edu/theme',
    method: 'post',
    data: data
  })
}

// 修改任务主题字典
export function updateTheme(data) {
  return request({
    url: '/edu/theme',
    method: 'put',
    data: data
  })
}

// 删除任务主题字典
export function delTheme(id) {
  return request({
    url: '/edu/theme/' + id,
    method: 'delete'
  })
}
