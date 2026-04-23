package com.edudu.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 对话响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String reply;
    private String conversationId;
    private List<String> sources;
    private LocalDateTime timestamp;

    /**
     * 本次对话是否更新了用户长期记忆
     */
    private boolean memoryUpdated;

    /**
     * 兼容旧构造
     */
    public ChatResponse(String reply, String conversationId, List<String> sources, LocalDateTime timestamp) {
        this(reply, conversationId, sources, timestamp, false);
    }

    /**
     * 工厂方法：AI功能不可用时返回
     */
    public static ChatResponse unavailable(String conversationId) {
        return new ChatResponse(
                "AI功能暂时不可用，请联系管理员配置AI服务。",
                conversationId,
                List.of(),
                LocalDateTime.now(),
                false);
    }

    /**
     * 工厂方法：发生错误时返回
     */
    public static ChatResponse error(String conversationId) {
        return new ChatResponse(
                "抱歉，我暂时无法回答您的问题，请稍后重试。",
                conversationId,
                List.of(),
                LocalDateTime.now(),
                false);
    }

    /**
     * 完整构造
     */
    public ChatResponse(String reply, String conversationId, LocalDateTime timestamp, boolean memoryUpdated) {
        this(reply, conversationId, List.of(), timestamp, memoryUpdated);
    }
}
