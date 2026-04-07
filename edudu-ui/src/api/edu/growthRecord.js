import request from '@/utils/request'

// 查询成长记录列表
export function listGrowthRecord(query) {
  return request({
    url: '/edu/growthRecord/list',
    method: 'get',
    params: query
  })
}

// 查询成长记录详细
export function getGrowthRecord(id) {
  return request({
    url: '/edu/growthRecord/' + id,
    method: 'get'
  })
}

// 新增成长记录
export function addGrowthRecord(data) {
  return request({
    url: '/edu/growthRecord',
    method: 'post',
    data: data
  })
}

// 修改成长记录
export function updateGrowthRecord(data) {
  return request({
    url: '/edu/growthRecord',
    method: 'put',
    data: data
  })
}

// 删除成长记录
export function delGrowthRecord(id) {
  return request({
    url: '/edu/growthRecord/' + id,
    method: 'delete'
  })
}
