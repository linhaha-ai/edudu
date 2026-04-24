import request from '@/utils/request'

// 查询AI知识库文档列表
export function listKnowledgeDoc(query) {
  return request({
    url: '/edu/knowledgeDoc/list',
    method: 'get',
    params: query
  })
}

// 查询AI知识库文档详细
export function getKnowledgeDoc(id) {
  return request({
    url: '/edu/knowledgeDoc/' + id,
    method: 'get'
  })
}

// 新增AI知识库文档
export function addKnowledgeDoc(data) {
  return request({
    url: '/edu/knowledgeDoc',
    method: 'post',
    data: data
  })
}

// 修改AI知识库文档
export function updateKnowledgeDoc(data) {
  return request({
    url: '/edu/knowledgeDoc',
    method: 'put',
    data: data
  })
}

// 删除AI知识库文档
export function delKnowledgeDoc(id) {
  return request({
    url: '/edu/knowledgeDoc/' + id,
    method: 'delete'
  })
}
