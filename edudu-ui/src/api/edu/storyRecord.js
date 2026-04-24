import request from '@/utils/request'

// 查询故事记录列表
export function listStoryRecord(query) {
  return request({
    url: '/edu/storyRecord/list',
    method: 'get',
    params: query
  })
}

// 查询故事记录详细
export function getStoryRecord(id) {
  return request({
    url: '/edu/storyRecord/' + id,
    method: 'get'
  })
}

// 新增故事记录
export function addStoryRecord(data) {
  return request({
    url: '/edu/storyRecord',
    method: 'post',
    data: data
  })
}

// 修改故事记录
export function updateStoryRecord(data) {
  return request({
    url: '/edu/storyRecord',
    method: 'put',
    data: data
  })
}

// 删除故事记录
export function delStoryRecord(id) {
  return request({
    url: '/edu/storyRecord/' + id,
    method: 'delete'
  })
}
