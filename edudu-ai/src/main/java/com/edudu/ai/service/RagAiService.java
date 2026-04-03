package com.edudu.ai.service;

import com.edudu.ai.config.RagConfig;
import com.edudu.ai.dto.ChatRequest;
import com.edudu.ai.dto.ChatResponse;
import com.edudu.ai.domain.KnowledgeDocument;
import com.edudu.ai.memory.RedisChatMemoryStore;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * RAG AI 对话服务
 */
@Slf4j
@Service
public class RagAiService {

    private final ChatLanguageModel chatLanguageModel;
    private final RagConfig ragConfig;
    private final KnowledgeService knowledgeService;
    private final RedisChatMemoryStore memoryStore;

    public RagAiService(
            ChatLanguageModel chatLanguageModel,
            RagConfig ragConfig,
            KnowledgeService knowledgeService,
            RedisChatMemoryStore memoryStore) {
        this.chatLanguageModel = chatLanguageModel;
        this.ragConfig = ragConfig;
        this.knowledgeService = knowledgeService;
        this.memoryStore = memoryStore;
    }

    /**
     * 同步对话
     */
    public ChatResponse chat(ChatRequest request) {
        String conversationId = request.getConversationId();
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
        }

        log.info("开始对话: conversationId={}, message={}", conversationId, request.getMessage());

        try {
            // 获取对话历史
            List<String> history = memoryStore.getMessages(conversationId);

            // 构建知识库上下文
            String knowledgeContext = buildKnowledgeContext();

            // 构建完整 prompt
            String fullPrompt = buildPrompt(knowledgeContext, history, request.getMessage());

            // 调用 LLM - 使用 generate 方法
            String reply = chatLanguageModel.generate(fullPrompt);

            // 保存对话历史
            memoryStore.addUserMessage(conversationId, request.getMessage());
            memoryStore.addAiMessage(conversationId, reply);

            List<String> sources = extractSources();
            log.info("对话完成: conversationId={}", conversationId);

            return new ChatResponse(reply, conversationId, sources, LocalDateTime.now());
        } catch (Exception e) {
            log.error("对话失败: conversationId={}", conversationId, e);
            return new ChatResponse(
                    "抱歉，我暂时无法回答您的问题，请稍后重试。",
                    conversationId,
                    List.of(),
                    LocalDateTime.now()
            );
        }
    }

    /**
     * 流式对话（简化版：返回完整响应）
     */
    public String chatStream(ChatRequest request) {
        String conversationId = request.getConversationId();
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
        }

        ChatResponse response = chat(request);
        return response.getReply();
    }

    /**
     * 构建知识库上下文
     */
    private String buildKnowledgeContext() {
        List<KnowledgeDocument> docs = knowledgeService.listEnabled();
        if (docs.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("知识库内容：\n");

        for (KnowledgeDocument doc : docs) {
            sb.append("【").append(doc.getTitle()).append("】\n");
            String content = doc.getContent().length() > 500
                    ? doc.getContent().substring(0, 500) + "..."
                    : doc.getContent();
            sb.append(content).append("\n\n");
        }

        return sb.toString();
    }

    /**
     * 构建完整 Prompt
     */
    private String buildPrompt(String knowledgeContext, List<String> history, String userMessage) {
        StringBuilder sb = new StringBuilder();

        sb.append("你是嘟嘟教育的AI助手，专注于育儿知识和早教内容。\n");

        if (!knowledgeContext.isEmpty()) {
            sb.append("\n").append(knowledgeContext).append("\n");
        }

        if (!history.isEmpty()) {
            sb.append("对话历史：\n");
            for (String msg : history) {
                sb.append(msg).append("\n");
            }
        }

        sb.append("\n用户问题：").append(userMessage).append("\n");
        sb.append("请根据以上信息回答用户问题。");

        return sb.toString();
    }

    /**
     * 提取引用来源
     */
    private List<String> extractSources() {
        List<KnowledgeDocument> docs = knowledgeService.listEnabled();
        List<String> sources = new ArrayList<>();
        for (KnowledgeDocument doc : docs) {
            sources.add(doc.getTitle());
        }
        return sources;
    }

    /**
     * 获取对话历史
     */
    public List<String> getHistory(String conversationId) {
        return memoryStore.getMessages(conversationId);
    }

    /**
     * 清除对话历史
     */
    public void clearHistory(String conversationId) {
        memoryStore.clear(conversationId);
    }
}