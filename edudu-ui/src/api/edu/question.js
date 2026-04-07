import request from '@/utils/request'

// 查询家长问题列表
export function listQuestion(query) {
  return request({
    url: '/edu/question/list',
    method: 'get',
    params: query
  })
}

// 查询家长问题详细
export function getQuestion(id) {
  return request({
    url: '/edu/question/' + id,
    method: 'get'
  })
}

// 新增家长问题
export function addQuestion(data) {
  return request({
    url: '/edu/question',
    method: 'post',
    data: data
  })
}

// 修改家长问题
export function updateQuestion(data) {
  return request({
    url: '/edu/question',
    method: 'put',
    data: data
  })
}

// 删除家长问题
export function delQuestion(id) {
  return request({
    url: '/edu/question/' + id,
    method: 'delete'
  })
}
