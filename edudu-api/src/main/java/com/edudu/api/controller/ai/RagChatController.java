package com.edudu.api.controller.ai;

import com.edudu.ai.dto.ChatRequest;
import com.edudu.ai.dto.ChatResponse;
import com.edudu.ai.service.RagAiService;
import com.edudu.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RAG AI 对话控制器
 */
@Slf4j
@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
@Tag(name = "AI对话接口")
public class RagChatController {

    private final RagAiService ragAiService;

    /**
     * 同步对话
     */
    @PostMapping
    @Operation(summary = "同步对话", description = "使用 Memory + RAG 进行同步对话")
    public AjaxResult chat(@RequestBody ChatRequest request) {
        log.info("收到对话请求: userId={}, conversationId={}", request.getUserId(), request.getConversationId());
        ChatResponse response = ragAiService.chat(request);
        return AjaxResult.success(response);
    }

    /**
     * 流式对话 (SSE)
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "流式对话", description = "SSE 流式输出 AI 回复")
    public String chatStream(
            @Parameter(description = "会话ID") @RequestParam String conversationId,
            @Parameter(description = "用户消息") @RequestParam String message) {
        log.info("收到流式对话请求: conversationId={}", conversationId);
        ChatRequest request = new ChatRequest();
        request.setConversationId(conversationId);
        request.setMessage(message);
        return ragAiService.chatStream(request);
    }

    /**
     * 获取对话历史
     */
    @GetMapping("/history/{conversationId}")
    @Operation(summary = "获取对话历史")
    public AjaxResult getHistory(
            @Parameter(description = "会话ID") @PathVariable String conversationId) {
        List<String> history = ragAiService.getHistory(conversationId);
        return AjaxResult.success(history);
    }

    /**
     * 清除对话历史
     */
    @DeleteMapping("/history/{conversationId}")
    @Operation(summary = "清除对话历史")
    public AjaxResult clearHistory(
            @Parameter(description = "会话ID") @PathVariable String conversationId) {
        ragAiService.clearHistory(conversationId);
        return AjaxResult.success("对话历史已清除");
    }
}