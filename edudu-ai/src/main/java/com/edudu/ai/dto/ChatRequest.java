package com.edudu.ai.dto;

import lombok.Data;

/**
 * AI 对话请求
 */
@Data
public class ChatRequest {

    private Long userId;

    private String conversationId;

    private String message;
}
