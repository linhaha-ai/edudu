# edudu-uniapp

AI早教陪伴平台 UniApp 移动端应用

## 技术栈

- **框架**: UniApp + Vue3 + TypeScript
- **状态管理**: Pinia
- **UI**: 自定义组件 + uni-ui
- **编译目标**: 微信小程序、iOS App、Android App、H5

## 项目结构

```
edudu-uniapp/
├── src/
│   ├── api/              # API 请求封装
│   │   ├── request.ts    # 请求核心封装
│   │   ├── user/         # 用户相关 API
│   │   ├── story/        # 故事相关 API
│   │   ├── task/         # 任务相关 API
│   │   ├── growth/       # 成长记录 API
│   │   ├── helper/       # 家长助手 API
│   │   └── child/        # 孩子管理 API
│   ├── components/       # 公共组件
│   ├── pages/            # 页面
│   │   ├── index/        # 首页
│   │   ├── story/        # AI 讲故事
│   │   ├── task/          # 每日任务
│   │   ├── growth/       # 成长记录
│   │   ├── helper/       # 家长助手
│   │   ├── profile/      # 个人中心
│   │   └── user/         # 登录注册
│   ├── static/           # 静态资源
│   ├── stores/           # Pinia 状态管理
│   ├── styles/           # 全局样式
│   ├── utils/            # 工具函数
│   ├── App.vue
│   └── main.ts
├── package.json
├── manifest.json          # App 配置
├── pages.json              # 路由配置
├── vite.config.ts
└── tsconfig.json
```

## 功能模块

### 1. 用户认证
- 手机号验证码登录
- 微信一键登录（小程序环境）
- Token 持久化

### 2. 首页
- 欢迎信息展示
- 当前孩子切换
- 今日任务进度
- 四大功能入口

### 3. AI 讲故事
- 故事参数配置（年龄、性格、场景）
- AI 故事生成
- 故事播放（语音合成）
- 收藏管理

### 4. 每日任务
- 任务列表展示
- 类型筛选
- 任务详情（互动话术、操作步骤）
- 任务完成记录

### 5. 成长记录
- 睡眠/饮食/学习记录
- 成长数据统计
- AI 成长报告

### 6. 家长助手
- 育儿问题提问
- AI 智能回答
- 历史记录

### 7. 个人中心
- 孩子信息管理
- 设置
- 关于

## 开发

```bash
# 安装依赖
npm install

# H5 开发
npm run dev:h5

# 微信小程序开发
npm run dev:mp-weixin

# App 开发
npm run dev:app
```

## 构建

```bash
# 构建 H5
npm run build:h5

# 构建微信小程序
npm run build:mp-weixin

# 构建 App
npm run build:app
```

## API 配置

在 `vite.config.ts` 中配置后端地址：

```typescript
export default defineConfig({
  define: {
    'import.meta.env.VITE_API_BASE_URL': '"http://localhost:8080"'
  }
})
```

## 后端依赖

应用依赖以下后端 API：
- `/api/auth/login/sms` - 短信登录
- `/api/sms/send` - 发送验证码
- `/api/user/info` - 获取用户信息
- `/api/story/generate` - 生成故事
- `/api/task/today` - 获取今日任务
- `/api/growth/records` - 成长记录
- `/api/parent/question` - 家长提问
- `/api/child/*` - 孩子管理
