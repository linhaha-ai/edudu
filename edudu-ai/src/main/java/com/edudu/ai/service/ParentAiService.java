package com.edudu.ai.service;

import com.edudu.ai.config.RagConfig;
import com.edudu.ai.memory.MemoryExtractor;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * AI家长助手服务（重构版）
 * 使用 LangChain4j AiServices 自动管理对话记忆
 */
@Slf4j
@Service
public class ParentAiService extends AbstractAiService {

    @Lazy
    @Autowired(required = false)
    private ParentAssistant parentAssistant;

    private final UserMemoryService userMemoryService;
    private final MemoryExtractor memoryExtractor;
    private final RagConfig ragConfig;

    public ParentAiService(UserMemoryService userMemoryService,
                           MemoryExtractor memoryExtractor,
                           RagConfig ragConfig) {
        this.userMemoryService = userMemoryService;
        this.memoryExtractor = memoryExtractor;
        this.ragConfig = ragConfig;
    }

    /**
     * 回答家长育儿问题
     * @param age 孩子年龄
     * @param userId 用户ID（用于对话记忆和长期记忆）
     * @param question 问题描述
     * @return JSON格式的回答
     */
    public String answerQuestion(int age, Long userId, String question) {
        if (parentAssistant != null) {
            // 使用 AiServices：自动管理对话记忆
            String memoryId = userId != null ? "parent:" + userId : "parent:anonymous";
            String result = parentAssistant.chat(memoryId, question);
            log.info("回答育儿问题完成(AiServices), 年龄: {}, question: {}",
                    age, question.substring(0, Math.min(50, question.length())));

            // 提取用户记忆
            if (userId != null && ragConfig.isMemoryExtractionEnabled()) {
                memoryExtractor.extractAndSave(userId, question, result);
            }
            return result;
        }

        // 降级：使用 AbstractAiService
        String prompt = buildPromptWithMemory(age, userId, question);
        String result = chat(prompt);
        log.info("回答育儿问题完成(降级), 年龄: {}", age);
        return result;
    }

    /**
     * 回答家长育儿问题（兼容旧接口）
     */
    public String answerQuestion(int age, String question) {
        return answerQuestion(age, null, question);
    }

    /**
     * 生成成长报告
     * @param age 孩子年龄
     * @param data 行为数据（JSON格式）
     * @return JSON格式的成长报告
     */
    public String generateGrowthReport(int age, String data) {
        String prompt = String.format("""
                你是一名儿童成长分析专家，请根据以下数据生成报告。

                【输入】
                - 年龄：%d岁
                - 行为数据：%s

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

                请严格按照JSON格式输出。
                """, age, data);

        String result = chat(prompt);
        log.info("生成成长报告完成, 年龄: {}", age);
        return result;
    }

    /**
     * 构建带记忆的 Prompt（降级方案）
     */
    private String buildPromptWithMemory(int age, Long userId, String question) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一位儿童心理学专家，请帮助家长解决育儿问题。\n\n");

        if (userId != null) {
            String memoryContext = userMemoryService.buildMemoryContext(userId);
            if (!memoryContext.isEmpty()) {
                sb.append(memoryContext).append("\n");
            }
        }

        sb.append(String.format("""
                【输入】
                - 儿童年龄：%d岁
                - 问题描述：%s

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

                请严格按照JSON格式输出。
                """, age, question));

        return sb.toString();
    }
}
