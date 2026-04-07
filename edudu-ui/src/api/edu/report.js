import request from '@/utils/request'

// 查询成长报告列表
export function listReport(query) {
  return request({
    url: '/edu/report/list',
    method: 'get',
    params: query
  })
}

// 查询成长报告详细
export function getReport(id) {
  return request({
    url: '/edu/report/' + id,
    method: 'get'
  })
}

// 新增成长报告
export function addReport(data) {
  return request({
    url: '/edu/report',
    method: 'post',
    data: data
  })
}

// 修改成长报告
export function updateReport(data) {
  return request({
    url: '/edu/report',
    method: 'put',
    data: data
  })
}

// 删除成长报告
export function delReport(id) {
  return request({
    url: '/edu/report/' + id,
    method: 'delete'
  })
}
