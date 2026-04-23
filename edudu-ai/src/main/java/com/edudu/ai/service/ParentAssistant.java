package com.edudu.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * 家长助手接口
 * 使用 LangChain4j AiServices 声明式定义，自动管理 Memory
 */
public interface ParentAssistant {

    @SystemMessage("你是一位儿童心理学专家，请帮助家长解决育儿问题。" +
            "回答必须可执行（非常重要），不要讲大道理。" +
            "结合儿童心理发展特点，分步骤说明。" +
            "请严格按照JSON格式输出：" +
            "{\"analysis\": \"问题原因分析\", \"solutions\": [\"解决方案1\", \"解决方案2\", \"解决方案3\"], " +
            "\"example\": \"具体沟通示例（家长可以直接照说）\"}")
    String chat(@MemoryId String memoryId, @UserMessage String message);
}
