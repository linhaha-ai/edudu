# Memory + RAG AI 服务实现计划

## 项目背景

在 `edudu-ai` 模块中构建企业级 AI 问答服务，支持：
1. **对话记忆（Memory）**：基于 Redis 的多轮对话上下文管理
2. **RAG（检索增强生成）**：基于向量检索的知识库问答
3. **SSE 流式响应**：实时流式输出 AI 回复
4. **知识库管理**：手动导入 + 网页爬虫导入

## 技术选型

| 组件 | 方案 |
|------|------|
| Memory | `MessageWindowChatMemory` + Redis 持久化 |
| RAG Embedding | `AllMiniLmL6V2EmbeddingModel` (本地) |
| Vector Store | `InMemoryEmbeddingStore` (JSON 文件持久化) |
| 流式响应 | SSE (`SseEmitter`) |
| 知识库存储 | MySQL (MyBatis-Plus) |
| 网页爬虫 | Jsoup |

## 架构设计

```
HTTP Request
    │
    ▼
┌─────────────────────────────────────────────────┐
│              edudu-api 模块                      │
│  RagChatController  │  KnowledgeController       │
│  (REST API)         │  (知识库管理)               │
└────────┬────────────┴───────────────┬────────────┘
         │                            │
         ▼                            ▼
┌─────────────────────────────────────────────────┐
│              edudu-ai 模块                       │
│                                                 │
│  ┌──────────────────┐  ┌──────────────────────┐ │
│  │  RagAiService    │  │  KnowledgeService    │ │
│  │  (Memory + RAG)  │  │  (CRUD + Index)      │ │
│  └────────┬─────────┘  └──────────┬───────────┘ │
│           │                       │             │
│  ┌────────▼───────────────────────▼──────────┐  │
│  │            RagConfig                      │  │
│  │  ChatMemoryProvider  │  ContentRetriever  │  │
│  │  EmbeddingStore      │  EmbeddingModel    │  │
│  └────────┬──────────────────────┬───────────┘  │
│           │                      │              │
│  ┌────────▼──────┐    ┌──────────▼───────────┐  │
│  │ RedisChat     │    │ WebScraper (Jsoup)   │  │
│  │ MemoryStore   │    │                      │  │
│  └───────────────┘    └──────────────────────┘  │
└─────────────────────────────────────────────────┘
         │                      │
         ▼                      ▼
┌─────────────────┐   ┌──────────────────────────┐
│  Redis          │   │  MySQL                   │
│  ai:memory:{id} │   │  ai_knowledge_doc 表     │
└─────────────────┘   └──────────────────────────┘
         │                      │
         ▼                      ▼
┌─────────────────────────────────────────────────┐
│  LangChain4j                                    │
│  ChatLanguageModel │ StreamingChatLanguageModel │
│  (OpenAI / 可替换)                              │
└─────────────────────────────────────────────────┘
```

## RAG 工作流程

```
用户消息
    │
    ▼
1. ContentRetriever 检索知识库 (top-K=3, min-score=0.7)
    │
    ▼
2. 构建增强 Prompt:
   [系统提示] + [检索到的上下文] + [对话历史] + [用户消息]
    │
    ▼
3. ChatLanguageModel 生成回复 (同步) 
   或 StreamingChatLanguageModel 流式输出 (SSE)
    │
    ▼
4. 保存对话到 Redis Memory (TTL 24h)
    │
    ▼
5. 返回响应 (含引用来源)
```

## Redis Key 设计

| Key Pattern | TTL | 说明 |
|-------------|-----|------|
| `ai:memory:{conversationId}` | 24h | 对话消息列表 (Redis List) |

## 数据库设计

```sql
CREATE TABLE IF NOT EXISTS ai_knowledge_doc (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(255) NOT NULL COMMENT '文档标题',
    content     TEXT NOT NULL COMMENT '文档内容',
    category    VARCHAR(100) DEFAULT NULL COMMENT '分类',
    status      TINYINT DEFAULT 1 COMMENT '状态: 0=禁用 1=启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI知识库文档表';
```

## 配置属性

```yaml
langchain4j:
  ai:
    memory:
      max-messages: 20        # 对话窗口最大消息数
      ttl-hours: 24           # Redis 过期时间(小时)
    rag:
      top-k: 3                # 检索返回的最大文档数
      min-score: 0.7          # 最小相似度分数
      chunk-size: 1000        # 文本分块大小
      chunk-overlap: 200      # 分块重叠大小
      embedding-file: ./data/ai-embeddings.json  # 向量存储文件路径
```

## API 接口设计

### 对话接口

| 方法 | 路径 | 说明 |
|------|------|------|
| `POST` | `/api/ai/chat` | 同步对话 (Memory + RAG) |
| `GET` | `/api/ai/chat/stream` | SSE 流式对话 |
| `GET` | `/api/ai/chat/history/{conversationId}` | 获取对话历史 |
| `DELETE` | `/api/ai/chat/history/{conversationId}` | 清除对话历史 |

### 知识库接口

| 方法 | 路径 | 说明 |
|------|------|------|
| `POST` | `/api/ai/knowledge/import` | 导入知识文档 |
| `POST` | `/api/ai/knowledge/import-url` | 从 URL 导入知识 |
| `DELETE` | `/api/ai/knowledge/{id}` | 删除知识文档 |
| `GET` | `/api/ai/knowledge/list` | 列出所有知识文档 |
| `POST` | `/api/ai/knowledge/rebuild` | 重建向量索引 |

---

## 文件清单

### 需要修改的文件 (3 个)

| # | 文件 | 修改内容 |
|---|------|----------|
| 1 | `pom.xml` (根) | 添加 `langchain4j` 和 `langchain4j-spring-boot-starter` 到 dependencyManagement |
| 2 | `edudu-ai/pom.xml` | `langchain4j-core` → `langchain4j`，添加 `jsoup` |
| 3 | `edudu-api/src/main/resources/application.yml` | 添加 `langchain4j.ai.*` 配置属性 |

### 需要创建的文件 (15 个)

#### DTOs (5 个)

| # | 文件路径 | 说明 |
|---|----------|------|
| 1 | `edudu-ai/src/main/java/com/edudu/ai/dto/ChatRequest.java` | 对话请求: userId, conversationId, message |
| 2 | `edudu-ai/src/main/java/com/edudu/ai/dto/ChatResponse.java` | 对话响应: reply, conversationId, sources, timestamp |
| 3 | `edudu-ai/src/main/java/com/edudu/ai/dto/KnowledgeDocRequest.java` | 知识导入请求: title, content, category |
| 4 | `edudu-ai/src/main/java/com/edudu/ai/dto/KnowledgeDocVO.java` | 知识文档视图: id, title, category, status, createTime |
| 5 | `edudu-ai/src/main/java/com/edudu/ai/dto/UrlImportRequest.java` | URL 导入请求: url, category |

#### 实体 & Mapper (2 个)

| # | 文件路径 | 说明 |
|---|----------|------|
| 6 | `edudu-ai/src/main/java/com/edudu/ai/domain/KnowledgeDocument.java` | MyBatis-Plus 实体: ai_knowledge_doc 表映射 |
| 7 | `edudu-ai/src/main/java/com/edudu/ai/mapper/KnowledgeDocumentMapper.java` | MyBatis-Plus Mapper: BaseMapper<KnowledgeDocument> |

#### Memory (1 个)

| # | 文件路径 | 说明 |
|---|----------|------|
| 8 | `edudu-ai/src/main/java/com/edudu/ai/memory/RedisChatMemoryStore.java` | 实现 ChatMemoryStore 接口，Redis 持久化对话历史 |

#### RAG Config (1 个)

| # | 文件路径 | 说明 |
|---|----------|------|
| 9 | `edudu-ai/src/main/java/com/edudu/ai/rag/RagConfig.java` | Spring 配置: EmbeddingStore, ContentRetriever, ChatMemoryProvider |

#### Web Scraper (1 个)

| # | 文件路径 | 说明 |
|---|----------|------|
| 10 | `edudu-ai/src/main/java/com/edudu/ai/scraper/WebScraper.java` | Jsoup 网页内容提取 |

#### Services (2 个)

| # | 文件路径 | 说明 |
|---|----------|------|
| 11 | `edudu-ai/src/main/java/com/edudu/ai/service/KnowledgeService.java` | 知识库 CRUD + 向量索引管理 |
| 12 | `edudu-ai/src/main/java/com/edudu/ai/service/RagAiService.java` | Memory + RAG 对话服务 (同步 + 流式) |

#### Controllers (2 个)

| # | 文件路径 | 说明 |
|---|----------|------|
| 13 | `edudu-api/src/main/java/com/edudu/api/controller/ai/RagChatController.java` | 对话 REST 接口 |
| 14 | `edudu-api/src/main/java/com/edudu/api/controller/ai/KnowledgeController.java` | 知识库管理 REST 接口 |

#### SQL (1 个)

| # | 文件路径 | 说明 |
|---|----------|------|
| 15 | `sql/ai_knowledge_doc.sql` | 知识库表 DDL |

---

## 实现步骤

### Step 1: 更新依赖
- 修改根 `pom.xml`，添加 `langchain4j` 完整版依赖管理
- 修改 `edudu-ai/pom.xml`，替换 `langchain4j-core` 为 `langchain4j`，添加 `jsoup`

### Step 2: 创建数据库表
- 执行 `sql/ai_knowledge_doc.sql`

### Step 3: 创建 DTOs
- 创建 5 个 DTO 类

### Step 4: 创建实体 & Mapper
- KnowledgeDocument + KnowledgeDocumentMapper

### Step 5: 创建 RedisChatMemoryStore
- 实现 ChatMemoryStore 接口

### Step 6: 创建 RagConfig
- 配置 EmbeddingModel, EmbeddingStore, ContentRetriever, ChatMemoryProvider

### Step 7: 创建 WebScraper
- Jsoup 网页抓取工具

### Step 8: 创建 KnowledgeService
- 知识库 CRUD + 向量索引管理

### Step 9: 创建 RagAiService
- Memory + RAG 对话 (同步 + SSE 流式)

### Step 10: 创建 Controllers
- RagChatController + KnowledgeController

### Step 11: 添加配置属性
- 更新 application.yml

### Step 12: 验证
- 编译检查
- 测试知识库导入
- 测试对话 (同步 + 流式)
