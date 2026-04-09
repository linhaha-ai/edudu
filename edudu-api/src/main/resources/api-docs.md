# 嘟嘟AI早教 API 文档

## 概述

- **基础 URL**: `http://localhost:8081/api`
- **认证方式**: Bearer Token (JWT)
- **请求格式**: JSON
- **响应格式**: `{ "code": 200, "data": {}, "msg": "操作成功" }`

---

## 目录

1. [认证模块](#1-认证模块)
2. [用户模块](#2-用户模块)
3. [孩子管理](#3-孩子管理)
4. [AI故事](#4-ai故事)
5. [每日任务](#5-每日任务)
6. [成长记录](#6-成长记录)
7. [家长助手](#7-家长助手)

---

## 1. 认证模块

### 1.1 密码登录
```
POST /auth/login
```
**请求参数**:
```json
{
  "phone": "13800138000",
  "password": "12345"
}
```
**响应**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 1.2 注册
```
POST /auth/register
```
**请求参数**:
```json
{
  "phone": "13800138000",
  "password": "12345",
  "nickname": "用户13800"
}
```

### 1.3 短信登录
```
POST /auth/login/sms
```
**请求参数**:
```json
{
  "phone": "13800138000",
  "code": "123456"
}
```
**响应**:
```json
{
  "code": 200,
  "msg": "登录成功",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 1.4 发送验证码
```
POST /sms/send
```
**请求参数**:
```json
{
  "phone": "13800138000",
  "type": "login|register"
}
```
**响应**:
```json
{
  "code": 200,
  "msg": "验证码发送成功"
}
```

### 1.5 微信登录
```
POST /auth/login/wechat
```
**请求参数**:
```json
{
  "code": "微信授权code",
  "nickname": "用户昵称",
  "avatar": "头像URL"
}
```

### 1.6 退出登录
```
POST /auth/logout
```

---

## 2. 用户模块

### 2.1 获取用户信息
```
GET /auth/info
```
**响应**:
```json
{
  "code": 200,
  "data": {
    "userId": 1,
    "phone": "13800138000",
    "nickname": "测试家长",
    "avatar": "",
    "gender": "1",
    "birthday": null
  }
}
```

### 2.2 更新用户信息
```
POST /user/update
```
**请求参数**:
```json
{
  "nickname": "新昵称",
  "avatar": "新头像URL",
  "gender": "1",
  "birthday": "1990-01-01"
}
```

---

## 3. 孩子管理

### 3.1 获取孩子列表
```
GET /child/list
```

### 3.2 获取孩子详情
```
GET /child/{childId}
```

### 3.3 添加孩子
```
POST /child/add
```
**请求参数**:
```json
{
  "name": "小明",
  "gender": "0",
  "birthDate": "2022-01-15",
  "personality": "活泼好动",
  "avatar": ""
}
```

### 3.4 更新孩子信息
```
POST /child/{childId}/update
```

### 3.5 删除孩子
```
POST /child/{childId}/delete
```

---

## 4. AI故事

### 4.1 生成故事
```
POST /story/generate
```
**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| childId | long | 是 | 孩子ID |
| age | int | 是 | 孩子年龄 |
| scene | string | 是 | 场景(bedtime/mealtime/tantrum/morning/education) |
| goal | string | 否 | 教育目标(share/brave/calm/honest/kind) |

### 4.2 获取我的故事列表
```
GET /story/my
```
**参数**: `page`, `pageSize`, `childId`

### 4.3 获取故事详情
```
GET /story/{storyId}
```

### 4.4 收藏故事
```
POST /story/{storyId}/favorite
```

### 4.5 取消收藏
```
POST /story/{storyId}/unfavorite
```

### 4.6 获取收藏列表
```
GET /story/favorites
```
**参数**: `page`, `pageSize`

---

## 5. 每日任务

### 5.1 获取今日任务
```
GET /task/today
```
**参数**: `childId`

### 5.2 获取任务详情
```
GET /task/{taskId}
```

### 5.3 完成任务
```
POST /task/{taskId}/complete
```
**请求参数**:
```json
{
  "note": "完成得很好！"
}
```

### 5.4 获取任务统计
```
GET /task/stats
```
**参数**: `childId`

**响应**:
```json
{
  "code": 200,
  "data": {
    "todayTotal": 5,
    "todayCompleted": 3,
    "weekStats": [
      { "date": "2026-04-01", "completed": 3, "total": 5 },
      { "date": "2026-04-02", "completed": 4, "total": 5 }
    ],
    "typeStats": [
      { "type": "color", "completed": 5, "total": 10 },
      { "type": "animal", "completed": 8, "total": 10 }
    ],
    "streakDays": 7
  }
}
```

---

## 6. 成长记录

### 6.1 获取成长记录列表
```
GET /growth/records
```
**参数**: `childId`, `type`(sleep/diet/learning/health), `page`, `pageSize`

### 6.2 添加成长记录
```
POST /growth/record/add
```
**请求参数**:
```json
{
  "childId": 1,
  "type": "sleep",
  "value": "10",
  "unit": "小时",
  "note": "睡得很香",
  "recordDate": "2026-04-01"
}
```

### 6.3 更新成长记录
```
POST /growth/record/{recordId}/update
```

### 6.4 删除成长记录
```
POST /growth/record/{recordId}/delete
```

### 6.5 获取成长统计
```
GET /growth/stats
```
**参数**: `childId`, `period`(week/month)

### 6.6 生成AI成长报告
```
POST /growth/report/generate
```
**请求参数**:
```json
{
  "childId": 1,
  "period": "week"
}
```

### 6.7 获取成长报告列表
```
GET /growth/reports
```
**参数**: `childId`

---

## 7. 家长助手

### 7.1 提交育儿问题
```
POST /parent/question
```
**请求参数**:
```json
{
  "childId": 1,
  "question": "孩子最近总是发脾气怎么办？"
}
```

### 7.2 获取问答历史
```
GET /parent/history
```
**参数**: `page`, `pageSize`

### 7.3 获取问答详情
```
GET /parent/question/{questionId}
```

---

## 错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 401 | 未授权/Token失效 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 更新日志

- **v1.1** (2026-04-09): 新增移动端专用接口
  - 短信登录/验证码
  - 微信登录
  - 故事收藏管理
  - 任务完成与统计
  - 成长记录 CRUD
  - 孩子管理完善
