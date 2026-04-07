import request from '@/utils/request'

// 查询故事场景字典列表
export function listScene(query) {
  return request({
    url: '/edu/scene/list',
    method: 'get',
    params: query
  })
}

// 查询故事场景字典详细
export function getScene(id) {
  return request({
    url: '/edu/scene/' + id,
    method: 'get'
  })
}

// 新增故事场景字典
export function addScene(data) {
  return request({
    url: '/edu/scene',
    method: 'post',
    data: data
  })
}

// 修改故事场景字典
export function updateScene(data) {
  return request({
    url: '/edu/scene',
    method: 'put',
    data: data
  })
}

// 删除故事场景字典
export function delScene(id) {
  return request({
    url: '/edu/scene/' + id,
    method: 'delete'
  })
}
