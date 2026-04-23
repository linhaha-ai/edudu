package com.edudu.ai.memory;

import com.edudu.ai.service.UserMemoryService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从对话中自动提取用户长期记忆
 * 支持两种方式：规则匹配（快速）和 LLM 提取（精准）
 */
@Slf4j
@Component
public class MemoryExtractor {

    private final UserMemoryService userMemoryService;
    private final ChatLanguageModel chatLanguageModel;

    // 规则匹配模式：孩子年龄
    private static final Pattern CHILD_AGE_PATTERN =
            Pattern.compile("我[的家]?孩子[大概约]?(\\d{1,2})岁");

    // 规则匹配模式：孩子性别
    private static final Pattern CHILD_GENDER_PATTERN =
            Pattern.compile("我[的家]?孩子是?(男孩|女孩|男|女|儿子|女儿|小子|闺女)");

    @Autowired
    public MemoryExtractor(UserMemoryService userMemoryService,
                           ChatLanguageModel chatLanguageModel) {
        this.userMemoryService = userMemoryService;
        this.chatLanguageModel = chatLanguageModel;
    }

    /**
     * 从对话中提取用户记忆并保存
     * @return true 如果有记忆被更新
     */
    public boolean extractAndSave(Long userId, String userMessage, String aiReply) {
        if (userId == null || userMessage == null) {
            return false;
        }

        boolean updated = false;

        // 1. 规则匹配提取（快速，无 token 消耗）
        updated |= extractByRules(userId, userMessage);

        // 2. LLM 提取（可选，有 token 消耗，仅在规则匹配无结果时触发）
        if (!updated && chatLanguageModel != null) {
            updated |= extractByLlm(userId, userMessage, aiReply);
        }

        return updated;
    }

    /**
     * 基于规则的快速提取
     */
    private boolean extractByRules(Long userId, String userMessage) {
        boolean updated = false;

        // 提取孩子年龄
        Matcher ageMatcher = CHILD_AGE_PATTERN.matcher(userMessage);
        if (ageMatcher.find()) {
            String age = ageMatcher.group(1);
            userMemoryService.saveMemory(userId, "child_age", age + "岁", "conversation");
            log.info("提取记忆(规则): userId={}, key=child_age, value={}", userId, age + "岁");
            updated = true;
        }

        // 提取孩子性别
        Matcher genderMatcher = CHILD_GENDER_PATTERN.matcher(userMessage);
        if (genderMatcher.find()) {
            String gender = normalizeGender(genderMatcher.group(1));
            userMemoryService.saveMemory(userId, "child_gender", gender, "conversation");
            log.info("提取记忆(规则): userId={}, key=child_gender, value={}", userId, gender);
            updated = true;
        }

        return updated;
    }

    /**
     * 基于 LLM 的精准提取
     */
    private boolean extractByLlm(Long userId, String userMessage, String aiReply) {
        try {
            String prompt = String.format("""
                    请从以下用户消息中提取可能需要长期记住的用户信息。

                    用户消息：%s
                    AI回复：%s

                    请以 JSON 格式输出，键为记忆名称（英文），值为记忆内容（中文）。
                    只提取明确的、有价值的信息，如孩子年龄、偏好、特殊需求等。
                    如果没有需要记住的信息，输出空对象 {}。

                    示例输出：
                    {"child_age": "3岁", "child_preference": "喜欢恐龙"}

                    请直接输出 JSON，不要添加任何说明。
                    """, userMessage != null ? userMessage : "", aiReply != null ? aiReply : "");

            String response = chatLanguageModel.generate(prompt);
            if (response == null || response.trim().equals("{}") || response.trim().isEmpty()) {
                return false;
            }

            // 简单解析 JSON（避免引入额外依赖）
            return parseSimpleJson(userId, response.trim());

        } catch (Exception e) {
            log.warn("LLM 记忆提取失败: userId={}", userId, e);
            return false;
        }
    }

    /**
     * 简单 JSON 解析（避免用正则处理复杂嵌套）
     */
    private boolean parseSimpleJson(Long userId, String json) {
        boolean updated = false;
        // 匹配 "key": "value" 模式
        Pattern kvPattern = Pattern.compile("\"(\\w+)\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = kvPattern.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            if (!key.isEmpty() && !value.isEmpty()) {
                userMemoryService.saveMemory(userId, key, value, "conversation");
                log.info("提取记忆(LLM): userId={}, key={}, value={}", userId, key, value);
                updated = true;
            }
        }
        return updated;
    }

    /**
     * 标准化性别描述
     */
    private String normalizeGender(String raw) {
        return switch (raw) {
            case "男", "男孩", "儿子", "小子" -> "男";
            case "女", "女孩", "女儿", "闺女" -> "女";
            default -> raw;
        };
    }
}
