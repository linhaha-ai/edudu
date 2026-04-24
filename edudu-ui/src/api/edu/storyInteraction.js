import request from '@/utils/request'

// 查询故事互动列表
export function listStoryInteraction(query) {
  return request({
    url: '/edu/storyInteraction/list',
    method: 'get',
    params: query
  })
}

// 查询故事互动详细
export function getStoryInteraction(id) {
  return request({
    url: '/edu/storyInteraction/' + id,
    method: 'get'
  })
}

// 新增故事互动
export function addStoryInteraction(data) {
  return request({
    url: '/edu/storyInteraction',
    method: 'post',
    data: data
  })
}

// 修改故事互动
export function updateStoryInteraction(data) {
  return request({
    url: '/edu/storyInteraction',
    method: 'put',
    data: data
  })
}

// 删除故事互动
export function delStoryInteraction(id) {
  return request({
    url: '/edu/storyInteraction/' + id,
    method: 'delete'
  })
}
