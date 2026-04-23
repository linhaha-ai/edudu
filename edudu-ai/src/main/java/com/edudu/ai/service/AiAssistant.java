package com.edudu.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * RAG AI 对话助手接口
 * 使用 LangChain4j AiServices 声明式定义，自动管理 Memory 和 RAG
 */
public interface AiAssistant {

    @SystemMessage("你是嘟嘟教育的AI助手，专注于育儿知识和早教内容。" +
            "请用中文回答，语气亲切温和，适合家长阅读。" +
            "如果用户提到孩子的信息（年龄、性别、偏好等），请记住这些信息。" +
            "回答要具体可执行，不要讲大道理。")
    String chat(@MemoryId String memoryId, @UserMessage String message);
}
