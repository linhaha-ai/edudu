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
}