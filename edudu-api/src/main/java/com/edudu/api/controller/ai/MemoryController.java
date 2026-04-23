package com.edudu.api.controller.ai;

import com.edudu.ai.domain.ConversationSummary;
import com.edudu.ai.domain.UserMemory;
import com.edudu.ai.service.ConversationSummaryService;
import com.edudu.ai.service.UserMemoryService;
import com.edudu.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户记忆管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/ai/memory")
@RequiredArgsConstructor
@Tag(name = "AI记忆管理接口")
public class MemoryController {

    private final UserMemoryService userMemoryService;
    private final ConversationSummaryService conversationSummaryService;

    /**
     * 获取用户长期记忆
     */
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户长期记忆", description = "获取用户的所有长期记忆信息")
    public AjaxResult getMemories(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<UserMemory> memories = userMemoryService.getAllMemories(userId);
        return AjaxResult.success(memories);
    }

    /**
     * 手动更新用户记忆
     */
    @PutMapping("/{userId}")
    @Operation(summary = "更新用户记忆", description = "手动添加或更新用户长期记忆")
    public AjaxResult updateMemory(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @RequestBody Map<String, String> memoryMap) {
        log.info("手动更新用户记忆: userId={}", userId);
        memoryMap.forEach((key, value) ->
                userMemoryService.saveMemory(userId, key, value, "manual"));
        return AjaxResult.success("记忆更新成功");
    }

    /**
     * 删除指定记忆
     */
    @DeleteMapping("/{userId}/{key}")
    @Operation(summary = "删除用户记忆", description = "删除用户指定的长期记忆")
    public AjaxResult deleteMemory(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "记忆键") @PathVariable String key) {
        log.info("删除用户记忆: userId={}, key={}", userId, key);
        boolean result = userMemoryService.deleteMemory(userId, key);
        return result ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * 获取对话摘要
     */
    @GetMapping("/{userId}/summary/{conversationId}")
    @Operation(summary = "获取对话摘要", description = "获取指定会话的对话摘要")
    public AjaxResult getSummary(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "会话ID") @PathVariable String conversationId) {
        ConversationSummary summary = conversationSummaryService.getSummary(conversationId);
        return AjaxResult.success(summary);
    }
}
