# AI早教陪伴平台 - 项目设计文档

## 一、项目概述

### 1.1 项目定位

**AI早教陪伴 + 家长工具 App**

核心价值：帮家长"不会教"的问题，而不是替代幼儿园

### 1.2 目标用户

- 0-6岁孩子的家长（尤其25-40岁）
- 不会系统育儿 / 没时间
- 需要育儿指导的用户

### 1.3 核心功能（MVP）

| 功能 | 说明 |
|------|------|
| AI讲故事 | 输入年龄+性格+场景，输出睡前故事、情绪引导故事、品德故事 |
| 每日早教任务 | 自动生成每日任务，包含互动话术、操作步骤、时长控制 |
| 成长记录 | 睡眠/饮食/学习记录，AI自动生成报告 |
| 家长助手 | 家长输入问题，AI给出分年龄建议和可执行方案 |

---

## 二、技术架构

### 2.1 技术栈选型

| 模块 | 技术方案 |
|------|----------|
| 后端 | Java + Spring Boot (RuoYi) |
| 数据库 | MySQL + Redis |
| AI能力 | OpenAI API / 国内大模型 + LangChain4j |
| 前端 | Flutter（跨平台App）或 Vue + Tauri（桌面工具） |
| 移动端 | 微信小程序支持 |

### 2.2 项目结构

```
ruoyi
├── ruoyi-admin         # 启动入口
├── ruoyi-system        # 系统模块（用户管理、权限）
├── ruoyi-common        # 工具类
├── ruoyi-framework     # 权限/安全
├── ruoyi-education     # 核心业务模块
└── ruoyi-ai            # AI能力模块
```

### 2.3 模块划分

#### ruoyi-education（业务核心）

```
ruoyi-education
└── com.ruoyi.education
    ├── controller
    │   ├── StoryController.java      # 故事接口
    │   ├── TaskController.java       # 任务接口
    │   ├── GrowthController.java     # 成长记录接口
    │   └── ParentController.java     # 家长助手接口
    │
    ├── service
    │   ├── IStoryService.java
    │   ├── ITaskService.java
    │   ├── IGrowthService.java
    │   └── IParentService.java
    │
    ├── mapper
    │   ├── StoryMapper.java
    │   ├── TaskMapper.java
    │   ├── GrowthMapper.java
    │   └── ParentMapper.java
    │
    ├── domain
    │   ├── StoryRecord.java
    │   ├── DailyTask.java
    │   ├── TaskRecord.java
    │   ├── GrowthRecord.java
    │   ├── ParentQuestion.java
    │   └── ParentAnswer.java
    │
    ├── dto
    │   ├── StoryRequestDTO.java
    │   ├── TaskRequestDTO.java
    │   └── ParentQuestionDTO.java
    │
    └── vo
        ├── StoryVO.java
        ├── TaskVO.java
        └── GrowthVO.java
```

#### ruoyi-ai（AI模块）

```
ruoyi-ai
└── com.ruoyi.ai
    ├── service
    │   ├── AiService.java
    │   ├── StoryAiService.java
    │   ├── TaskAiService.java
    │   └── ParentAiService.java
    │
    ├── prompt
    │   ├── StoryPromptBuilder.java
    │   ├── TaskPromptBuilder.java
    │   └── ParentPromptBuilder.java
    │
    ├── config
    │   └── AiConfig.java
    │
    ├── client
    │   └── OpenAiClient.java
    │
    └── dto
        ├── AiRequest.java
        └── AiResponse.java
```

---

## 三、数据库设计

### 3.1 核心表结构

#### 3.1.1 用户表 (user)

```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone VARCHAR(20),
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);
```

#### 3.1.2 孩子表 (child)

```sql
CREATE TABLE child (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(50),
    gender TINYINT,        -- 1男 2女
    birth_date DATE,
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_user_id (user_id)
);
```

> 注意：年龄不要存，用 birth_date 动态计算

#### 3.1.3 故事记录表 (story_record)

```sql
CREATE TABLE story_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    child_id BIGINT,
    age INT,
    scene VARCHAR(50),
    goal VARCHAR(100),

    title VARCHAR(255),
    content TEXT,
    moral VARCHAR(255),

    created_at DATETIME,
    INDEX idx_user_child (user_id, child_id)
);
```

#### 3.1.4 故事互动问题表 (story_interaction)

```sql
CREATE TABLE story_interaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    story_id BIGINT,
    question VARCHAR(255)
);
```

#### 3.1.5 每日任务模板表 (daily_task)

```sql
CREATE TABLE daily_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    age_min INT,
    age_max INT,
    theme VARCHAR(50),

    title VARCHAR(255),
    goal VARCHAR(255),
    steps TEXT,
    tips VARCHAR(255),
    duration VARCHAR(50),

    created_at DATETIME
);
```

#### 3.1.6 用户任务记录表 (task_record)

```sql
CREATE TABLE task_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    child_id BIGINT,
    task_id BIGINT,

    status TINYINT,        -- 0未完成 1完成
    feedback VARCHAR(255),

    created_at DATETIME,
    INDEX idx_user_child (user_id, child_id)
);
```

#### 3.1.7 成长记录表 (growth_record)

```sql
CREATE TABLE growth_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    child_id BIGINT,

    type VARCHAR(50),      -- sleep / diet / learning

    value VARCHAR(255),
    note VARCHAR(255),

    record_date DATE,
    created_at DATETIME,

    INDEX idx_child_date (child_id, record_date)
);
```

#### 3.1.8 家长问题表 (parent_question)

```sql
CREATE TABLE parent_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    child_id BIGINT,

    question TEXT,
    created_at DATETIME
);
```

#### 3.1.9 AI回答表 (parent_answer)

```sql
CREATE TABLE parent_answer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT,

    analysis TEXT,
    solutions TEXT,        -- 建议存JSON
    example TEXT,

    created_at DATETIME
);
```

#### 3.1.10 订阅表 (subscription)

```sql
CREATE TABLE subscription (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,

    plan VARCHAR(50),      -- monthly / yearly
    status TINYINT,        -- 0未激活 1有效 2过期

    start_time DATETIME,
    end_time DATETIME,

    created_at DATETIME
);
```

### 3.2 设计要点

1. **AI内容必须落库**：可复用、可做推荐、可做训练数据
2. **JSON字段使用**：适合存 steps、solutions、interaction（MySQL 5.7+用JSON类型）
3. **高频索引必须加**：
   - user_id + child_id
   - child_id + date

---

## 四、AI Prompt 设计

### 4.1 核心设计思路

Prompt必须做到三点：
- 年龄分层（必须）
- 输出结构固定（方便前端渲染）
- 强约束（避免AI乱发挥）

### 4.2 AI讲故事 Prompt

```
你是一位专业的儿童早教专家，熟悉蒙特梭利（Montessori Method）教育理念。

请根据以下信息，为儿童创作一个适合的故事：

【输入信息】
- 年龄：{age}
- 性别：{gender}
- 场景：{scene}（例如：睡前 / 吃饭 / 发脾气）
- 教育目标：{goal}（例如：学会分享 / 不哭闹 / 勇敢）

【要求】
1. 故事必须适合该年龄理解（非常重要）
2. 用简单语言，句子短
3. 情节温和，不要有恐怖或冲突
4. 必须有正向引导
5. 控制在 300-500 字

【输出格式（必须严格JSON）】
{
  "title": "故事标题",
  "content": "故事正文",
  "moral": "教育意义",
  "interaction": [
    "家长可以问孩子的问题1",
    "家长可以问孩子的问题2"
  ]
}

请严格按照JSON格式输出，不要添加任何额外说明。
```

### 4.3 每日早教任务 Prompt

```
你是一名资深幼儿教育专家，请根据儿童发展规律设计一个家庭早教任务。

【输入】
- 年龄：{age}
- 今日主题：{theme}（颜色 / 动物 / 情绪 / 语言）
- 时间限制：10分钟以内

【要求】
1. 必须简单，家长可执行
2. 不需要复杂道具
3. 有互动环节
4. 符合儿童认知发展

【输出 JSON】
{
  "title": "任务名称",
  "goal": "训练目标",
  "steps": [
    "步骤1",
    "步骤2",
    "步骤3"
  ],
  "tips": "家长注意事项",
  "duration": "建议时长"
}
```

### 4.4 家长问题助手 Prompt

```
你是一位儿童心理学专家，请帮助家长解决育儿问题。

【输入】
- 儿童年龄：{age}
- 问题描述：{question}

【要求】
1. 回答必须可执行（非常重要）
2. 不要讲大道理
3. 结合儿童心理发展特点
4. 分步骤说明

【输出 JSON】
{
  "analysis": "问题原因分析",
  "solutions": [
    "解决方案1",
    "解决方案2",
    "解决方案3"
  ],
  "example": "具体沟通示例（家长可以直接照说）"
}
```

### 4.5 成长报告生成 Prompt

```
你是一名儿童成长分析专家，请根据以下数据生成报告。

【输入】
- 年龄：{age}
- 行为数据：{data}

【要求】
1. 用温和语气
2. 不要批评孩子
3. 给出具体建议

【输出 JSON】
{
  "summary": "总体评价",
  "strengths": ["优点1", "优点2"],
  "improvements": ["建议1", "建议2"],
  "advice": "家长建议"
}
```

---

## 五、接口设计

### 5.1 AI模块接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /ai/story/generate | 生成故事 |
| POST | /ai/task/generate | 生成每日任务 |
| POST | /ai/parent/ask | 家长提问 |

### 5.2 教育模块接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /task/today | 获取今日任务 |
| POST | /task/complete | 完成任务 |
| GET | /growth/report | 获取成长报告 |

### 5.3 核心代码示例

#### StoryController

```java
@RestController
@RequestMapping("/ai/story")
public class StoryController {

    @Autowired
    private IStoryService storyService;

    @PostMapping("/generate")
    public AjaxResult generate(@RequestBody StoryRequestDTO dto) {
        return AjaxResult.success(storyService.generateStory(dto));
    }
}
```

#### StoryServiceImpl

```java
@Service
public class StoryServiceImpl implements IStoryService {

    @Autowired
    private StoryAiService storyAiService;

    @Autowired
    private StoryMapper storyMapper;

    @Override
    public StoryVO generateStory(StoryRequestDTO dto) {
        // 1. 调AI生成
        StoryVO story = storyAiService.generate(dto);

        // 2. 落库
        StoryRecord record = new StoryRecord();
        record.setTitle(story.getTitle());
        record.setContent(story.getContent());
        storyMapper.insert(record);

        return story;
    }
}
```

#### StoryAiService

```java
@Service
public class StoryAiService {

    @Autowired
    private OpenAiClient openAiClient;

    public StoryVO generate(StoryRequestDTO dto) {
        String prompt = StoryPromptBuilder.build(dto);
        String result = openAiClient.chat(prompt);
        return parse(result);
    }

    private StoryVO parse(String json) {
        return JsonUtils.parseObject(json, StoryVO.class);
    }
}
```

#### StoryPromptBuilder

```java
public class StoryPromptBuilder {

    public static String build(StoryRequestDTO dto) {
        return String.format(
            "年龄:%d, 场景:%s, 目标:%s ... 输出JSON",
            dto.getAge(),
            dto.getScene(),
            dto.getGoal()
        );
    }
}
```

---

## 六、内容体系设计

### 6.1 年龄分层

| 年龄段 | 教育重点 |
|--------|----------|
| 0-2岁 | 感官刺激、语言输入 |
| 2-4岁 | 认知（颜色/动物）、简单逻辑 |
| 4-6岁 | 情绪管理、社交能力 |

### 6.2 教育理论参考

- Montessori Method（蒙特梭利）
- Reggio Emilia Approach

---

## 七、变现模式

| 模式 | 说明 |
|------|------|
| 订阅制（推荐） | ¥19/月，解锁AI故事+全部课程 |
| 课程包 | 情绪管理专题、语言启蒙专题 |
| 硬件结合（进阶） | AI故事机（类似火火兔） |

---

## 八、开发计划

### 8.1 开发周期（2-3个月）

| 阶段 | 时间 | 内容 |
|------|------|------|
| 第1阶段 | 2-3周 | 用户系统、AI故事、简单前端 |
| 第2阶段 | 3-5周 | 每日任务、成长记录 |
| 第3阶段 | 4周 | 内容优化、商业化 |

### 8.2 实施步骤

1. 搭建 RuoYi 框架
2. 建表（使用SQL脚本）
3. 写核心接口
4. 接入 AI
5. 持续优化

---

## 九、配置建议

### application.yml

```yaml
ai:
  api-key: xxx
  model: gpt-4o-mini
  timeout: 30s
```

---

## 十、注意事项

### 10.1 关键成功因素

1. **不要做"大而全"**：先做AI讲故事 + 每日任务
2. **内容比技术更重要**：真正核心是话术设计、教育逻辑
3. **留存点**：每日任务 = 关键

### 10.2 进阶优化

- 加"角色设定"（提升质量）
- 加"few-shot"（更稳定）
- 内容安全控制（禁止暴力、恐惧、惩罚性语言）

---

*文档创建时间：2026-04-02*
*来源：ChatGPT 对话整理*
