package com.edudu.ai.service;

import com.edudu.ai.config.RagConfig;
import com.edudu.ai.dto.ChatRequest;
import com.edudu.ai.dto.ChatResponse;
import com.edudu.ai.memory.MemoryExtractor;
import com.edudu.ai.memory.RedisChatMemoryStore;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * RAG AI 对话服务（重构版）
 * 基于 LangChain4j AiServices，自动管理 Memory 和 RAG 向量检索
 */
@Slf4j
@Service
public class RagAiService {

    @Lazy
    @Autowired(required = false)
    private AiAssistant aiAssistant;

    @Lazy
    @Autowired(required = false)
    private ChatLanguageModel chatLanguageModel;

    private final RagConfig ragConfig;
    private final RedisChatMemoryStore memoryStore;
    private final UserMemoryService userMemoryService;
    private final ConversationSummaryService conversationSummaryService;
    private final MemoryExtractor memoryExtractor;

    public RagAiService(
            RagConfig ragConfig,
            RedisChatMemoryStore memoryStore,
            UserMemoryService userMemoryService,
            ConversationSummaryService conversationSummaryService,
            MemoryExtractor memoryExtractor) {
        this.ragConfig = ragConfig;
        this.memoryStore = memoryStore;
        this.userMemoryService = userMemoryService;
        this.conversationSummaryService = conversationSummaryService;
        this.memoryExtractor = memoryExtractor;
    }

    /**
     * 检查AI功能是否可用
     */
    private boolean isAiAvailable() {
        return aiAssistant != null || chatLanguageModel != null;
    }

    /**
     * 同步对话
     */
    public ChatResponse chat(ChatRequest request) {
        if (!isAiAvailable()) {
            log.warn("AI功能不可用，ChatLanguageModel 未配置");
            return ChatResponse.unavailable(request.getConversationId());
        }

        String conversationId = request.getConversationId();
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
        }

        log.info("开始对话: conversationId={}, userId={}, message={}",
                conversationId, request.getUserId(),
                request.getMessage().substring(0, Math.min(50, request.getMessage().length())));

        try {
            // 构建带上下文的用户消息
            String enhancedMessage = buildContextualMessage(conversationId, request.getUserId(), request.getMessage());

            // 调用 AI（AiServices 自动处理 Memory + RAG）
            String reply;
            if (aiAssistant != null) {
                reply = aiAssistant.chat(conversationId, enhancedMessage);
            } else {
                // 降级：直接使用 chatLanguageModel
                reply = chatLanguageModel.generate(request.getMessage());
            }

            // 后处理：提取用户记忆
            boolean memoryUpdated = false;
            if (request.getUserId() != null && ragConfig.isMemoryExtractionEnabled()) {
                memoryUpdated = memoryExtractor.extractAndSave(
                        request.getUserId(), request.getMessage(), reply);
            }

            // 后处理：检查是否需要生成摘要
            checkAndSummarize(conversationId, request.getUserId());

            log.info("对话完成: conversationId={}", conversationId);
            return new ChatResponse(reply, conversationId, LocalDateTime.now(), memoryUpdated);

        } catch (Exception e) {
            log.error("对话失败: conversationId={}", conversationId, e);
            return ChatResponse.error(request.getConversationId());
        }
    }

    /**
     * 流式对话（简化版：返回完整响应）
     */
    public String chatStream(ChatRequest request) {
        ChatResponse response = chat(request);
        return response.getReply();
    }

    /**
     * 构建带上下文的用户消息
     * 将用户长期记忆和对话摘要注入到消息中，让 AI 在生成回答时参考
     */
    private String buildContextualMessage(String conversationId, Long userId, String userMessage) {
        StringBuilder contextBuilder = new StringBuilder();

        // 注入用户长期记忆
        if (userId != null) {
            String memoryContext = userMemoryService.buildMemoryContext(userId);
            if (!memoryContext.isEmpty()) {
                contextBuilder.append(memoryContext).append("\n");
            }
        }

        // 注入对话摘要
        String summaryContext = conversationSummaryService.buildSummaryContext(conversationId);
        if (!summaryContext.isEmpty()) {
            contextBuilder.append(summaryContext);
        }

        // 组合最终消息
        if (contextBuilder.isEmpty()) {
            return userMessage;
        }

        return contextBuilder + "\n用户问题：" + userMessage;
    }

    /**
     * 检查并生成对话摘要
     */
    private void checkAndSummarize(String conversationId, Long userId) {
        try {
            long messageCount = memoryStore.getMessageCount(conversationId);
            if (conversationSummaryService.shouldSummarize(conversationId, messageCount)) {
                List<ChatMessage> messages = memoryStore.getMessages(conversationId);
                if (messages != null && !messages.isEmpty()) {
                    conversationSummaryService.generateSummary(conversationId, userId, messages);
                    // 摘要生成后，修剪旧消息，保留最近N条
                    int keepCount = ragConfig.getMaxMessages();
                    memoryStore.trimMessages(conversationId, keepCount);
                }
            }
        } catch (Exception e) {
            log.warn("摘要生成异常: conversationId={}", conversationId, e);
        }
    }

    /**
     * 获取对话历史
     */
    public List<ChatMessage> getHistory(String conversationId) {
        List<ChatMessage> messages = memoryStore.getMessages(conversationId);
        return messages != null ? messages : new ArrayList<>();
    }

    /**
     * 清除对话历史
     */
    public void clearHistory(String conversationId) {
        memoryStore.deleteMessages(conversationId);
    }
}
