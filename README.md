# AI早教陪伴平台

<p align="center">
	<img alt="logo" src="https://oscimg.oschina.net/oscnet/up-d3d0a9303e11d522a06cd263f3079027715.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">AI早教陪伴平台</h1>
<h4 align="center">基于 SpringBoot + Vue 的 AI 智能早教解决方案</h4>
<p align="center">
	<a href="https://gitee.com/y_project/EduDu-Vue"><img src="https://img.shields.io/badge/Powered-EduDu-brightgreen.svg"></a>
	<a href="https://gitee.com/y_project/EduDu-Vue/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>

## 平台简介

AI早教陪伴平台是一款面向0-6岁儿童家长的智能早教助手，核心价值是帮助家长解决"不会教"的问题。

**核心亮点：**
- AI智能生成个性化故事和早教内容
- 每日早教任务自动推送
- 成长记录与智能分析报告
- 家长育儿问答助手

## 目标用户

- 0-6岁孩子的家长（尤其25-40岁）
- 缺乏系统育儿经验或时间有限的家长
- 需要专业育儿指导的用户

## 核心功能

| 功能 | 说明 |
|------|------|
| AI讲故事 | 输入年龄+性格+场景，输出睡前故事、情绪引导故事、品德故事 |
| 每日早教任务 | 自动生成每日任务，包含互动话术、操作步骤、时长控制 |
| 成长记录 | 睡眠/饮食/学习记录，AI自动生成成长报告 |
| 家长助手 | 家长输入问题，AI给出分年龄建议和可执行方案 |

## 技术架构

| 模块 | 技术方案 |
|------|----------|
| 后端 | Java + Spring Boot (EduDu) |
| 数据库 | MySQL + Redis |
| AI能力 | OpenAI API / 国内大模型 + LangChain4j |
| 前端 | Vue + Element UI |
| 移动端 | 微信小程序支持 |

## 项目结构

```
dudu-edu
├── edudu-admin         # 启动入口
├── edudu-system        # 系统模块（用户管理、权限）
├── edudu-common        # 工具类
├── edudu-framework     # 权限/安全
├── edudu-quartz        # 定时任务
├── edudu-generator     # 代码生成器
├── edudu-ui            # 前端Vue项目
├── edudu-api           # 核心业务模块（孩子、故事、任务、成长记录）
└── edudu-ai            # AI能力模块
```

### 模块详细结构

#### edudu-api（业务核心）

```
edudu-api
└── com.edudu.education
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

#### edudu-ai（AI模块）

```
edudu-ai
└── com.edudu.ai
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

## 内置功能

1. **用户管理**：用户是系统操作者，该功能主要完成系统用户配置。
2. **孩子管理**：管理多个孩子档案，支持按年龄段提供个性化内容。
3. **AI故事**：智能生成适合不同年龄段的儿童故事，支持多种场景。
4. **每日任务**：自动生成适合孩子年龄的早教任务，包含操作步骤。
5. **成长记录**：记录睡眠、饮食、学习等数据，生成AI分析报告。
6. **家长助手**：回答家长育儿问题，提供分年龄建议和执行方案。
7. **订阅管理**：支持月度/年度订阅，灵活设置会员权益。
8. **角色管理**：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
9. **菜单管理**：配置系统菜单，操作权限，按钮权限标识等。
10. **字典管理**：对系统中经常使用的一些较为固定的数据进行维护。
11. **参数管理**：对系统动态配置常用参数。
12. **通知公告**：系统通知公告信息发布维护。
13. **操作日志**：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
14. **登录日志**：系统登录日志记录查询包含登录异常。
15. **定时任务**：在线（添加、修改、删除）任务调度包含执行结果日志。
16. **代码生成**：前后端代码的生成（java、html、xml、sql）支持CRUD下载。
17. **系统接口**：根据业务代码自动生成相关的api接口文档。
18. **服务监控**：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
19. **缓存监控**：对系统的缓存信息查询，命令统计等。
20. **在线构建器**：拖动表单元素生成相应的HTML代码。
21. **连接池监视**：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 数据库设计

> 数据库脚本位于 `sql/` 目录，核心表结构如下：

### 核心表说明

| 表名 | 说明 |
|------|------|
| edu_user | 前端用户表（家长用户） |
| edu_child | 孩子信息表 |
| edu_story_record | 故事记录表 |
| edu_story_interaction | 故事互动问题表 |
| edu_daily_task | 每日任务模板表 |
| edu_task_record | 用户任务记录表 |
| edu_growth_record | 成长记录表 |
| edu_growth_report | 成长报告表 |
| edu_parent_question | 家长问题表 |
| edu_parent_answer | AI回答表 |
| edu_subscription | 订阅表 |
| edu_subscription_plan | 订阅计划配置表 |
| edu_ai_config | AI配置表 |
| edu_dict_* | 字典表（场景、目标、主题、成长类型） |
| edu_task_daily | 每日任务分配表 |
| edu_favorite | 收藏表 |
| edu_ai_knowledge_doc | AI知识库文档表 |
| edu_ai_user_memory | AI用户长期记忆表 |
| edu_ai_conversation_summary | AI对话摘要表 |

### 设计要点

1. **AI内容必须落库**：可复用、可做推荐、可做训练数据
2. **JSON字段使用**：适合存 steps、solutions、interaction（MySQL 5.7+用JSON类型）
3. **高频索引必须加**：
   - user_id + child_id
   - child_id + date

## AI Prompt 设计

### 设计思路

Prompt必须做到三点：
- 年龄分层（必须）
- 输出结构固定（方便前端渲染）
- 强约束（避免AI乱发挥）

### AI讲故事 Prompt

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

### 每日早教任务 Prompt

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
  "steps": ["步骤1", "步骤2", "步骤3"],
  "tips": "家长注意事项",
  "duration": "建议时长"
}
```

### 家长问题助手 Prompt

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
  "solutions": ["解决方案1", "解决方案2", "解决方案3"],
  "example": "具体沟通示例（家长可以直接照说）"
}
```

### 成长报告生成 Prompt

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

## 接口设计

### AI模块接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /ai/story/generate | 生成故事 |
| POST | /ai/task/generate | 生成每日任务 |
| POST | /ai/parent/ask | 家长提问 |

### 教育模块接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /task/today | 获取今日任务 |
| POST | /task/complete | 完成任务 |
| GET | /growth/report | 获取成长报告 |

### 核心代码示例

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

## 年龄分层体系

| 年龄段 | 教育重点 |
|--------|----------|
| 0-2岁 | 感官刺激、语言输入 |
| 2-4岁 | 认知（颜色/动物）、简单逻辑 |
| 4-6岁 | 情绪管理、社交能力 |

### 教育理论参考

- Montessori Method（蒙特梭利）
- Reggio Emilia Approach

## 变现模式

| 模式 | 说明 |
|------|------|
| 订阅制（推荐） | ¥19/月，解锁AI故事+全部课程 |
| 课程包 | 情绪管理专题、语言启蒙专题 |
| 硬件结合（进阶） | AI故事机（类似火火兔） |

## 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis

## 快速开始

### 后端启动

```bash
# 修改数据库配置
# 编辑 edudu-admin/src/main/resources/application.yml

# 启动项目
cd edudu-admin
mvn spring-boot:run
```

### 前端启动

```bash
cd edudu-ui

# 安装依赖
npm install

# 启动项目
npm run dev
```

### 访问地址

- 前端：http://localhost:80
- 后端API：http://localhost:8080

## 开发计划

| 阶段 | 内容 |
|------|------|
| 第1阶段 | 用户系统、AI故事、简单前端 |
| 第2阶段 | 每日任务、成长记录 |
| 第3阶段 | 内容优化、商业化 |

## 注意事项

### 关键成功因素

1. **不要做"大而全"**：先做AI讲故事 + 每日任务
2. **内容比技术更重要**：真正核心是话术设计、教育逻辑
3. **留存点**：每日任务 = 关键

### 进阶优化

- 加"角色设定"（提升质量）
- 加"few-shot"（更稳定）
- 内容安全控制（禁止暴力、恐惧、惩罚性语言）

*文档创建时间：2026-04-02*
