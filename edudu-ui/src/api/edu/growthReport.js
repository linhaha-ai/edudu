import request from '@/utils/request'

// 查询成长报告列表
export function listGrowthReport(query) {
  return request({
    url: '/edu/growthReport/list',
    method: 'get',
    params: query
  })
}

// 查询成长报告详细
export function getGrowthReport(id) {
  return request({
    url: '/edu/growthReport/' + id,
    method: 'get'
  })
}

// 新增成长报告
export function addGrowthReport(data) {
  return request({
    url: '/edu/growthReport',
    method: 'post',
    data: data
  })
}

// 修改成长报告
export function updateGrowthReport(data) {
  return request({
    url: '/edu/growthReport',
    method: 'put',
    data: data
  })
}

// 删除成长报告
export function delGrowthReport(id) {
  return request({
    url: '/edu/growthReport/' + id,
    method: 'delete'
  })
}
