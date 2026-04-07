import request from '@/utils/request'

// 查询AI回答列表
export function listAnswer(query) {
  return request({
    url: '/edu/answer/list',
    method: 'get',
    params: query
  })
}

// 查询AI回答详细
export function getAnswer(id) {
  return request({
    url: '/edu/answer/' + id,
    method: 'get'
  })
}

// 新增AI回答
export function addAnswer(data) {
  return request({
    url: '/edu/answer',
    method: 'post',
    data: data
  })
}

// 修改AI回答
export function updateAnswer(data) {
  return request({
    url: '/edu/answer',
    method: 'put',
    data: data
  })
}

// 删除AI回答
export function delAnswer(id) {
  return request({
    url: '/edu/answer/' + id,
    method: 'delete'
  })
}
