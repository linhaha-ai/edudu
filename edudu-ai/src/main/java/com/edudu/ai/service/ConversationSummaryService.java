package com.edudu.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edudu.ai.config.RagConfig;
import com.edudu.ai.domain.ConversationSummary;
import com.edudu.ai.mapper.ConversationSummaryMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对话摘要服务
 */
@Slf4j
@Service
public class ConversationSummaryService extends ServiceImpl<ConversationSummaryMapper, ConversationSummary> {

    private final RagConfig ragConfig;
    private final ChatLanguageModel chatLanguageModel;

    @Autowired
    public ConversationSummaryService(RagConfig ragConfig,
                                     ChatLanguageModel chatLanguageModel) {
        this.ragConfig = ragConfig;
        this.chatLanguageModel = chatLanguageModel;
    }

    /**
     * 判断是否需要摘要
     */
    public boolean shouldSummarize(String conversationId, long currentMessageCount) {
        int threshold = ragConfig.getSummaryThreshold();
        if (threshold <= 0) {
            return false;
        }

        ConversationSummary existing = getSummary(conversationId);
        int alreadySummarized = existing != null ? existing.getMessageCount() : 0;
        long newMessages = currentMessageCount - alreadySummarized;
        return newMessages >= threshold;
    }

    /**
     * 生成对话摘要
     */
    public String generateSummary(String conversationId, Long userId, List<ChatMessage> messages) {
        if (chatLanguageModel == null) {
            log.warn("ChatLanguageModel 不可用，无法生成摘要");
            return null;
        }

        if (messages == null || messages.isEmpty()) {
            log.warn("消息列表为空，无法生成摘要: conversationId={}", conversationId);
            return null;
        }

        log.info("开始生成对话摘要: conversationId={}, 消息数={}", conversationId, messages.size());

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("请将以下对话历史总结为简洁的摘要，保留关键信息和上下文：\n\n");

        for (ChatMessage message : messages) {
            promptBuilder.append(message.type().name()).append(": ")
                    .append(message.text()).append("\n");
        }

        promptBuilder.append("\n请用中文输出摘要，不超过200字。");

        String summaryText = chatLanguageModel.generate(promptBuilder.toString());

        // 保存或更新摘要
        ConversationSummary existing = getSummary(conversationId);
        if (existing != null) {
            existing.setSummary(summaryText);
            existing.setMessageCount(messages.size());
            updateById(existing);
        } else {
            ConversationSummary summary = new ConversationSummary();
            summary.setConversationId(conversationId);
            summary.setUserId(userId);
            summary.setSummary(summaryText);
            summary.setMessageCount(messages.size());
            save(summary);
        }

        log.info("对话摘要生成完成: conversationId={}", conversationId);
        return summaryText;
    }

    /**
     * 获取对话摘要
     */
    public ConversationSummary getSummary(String conversationId) {
        return getOne(new LambdaQueryWrapper<ConversationSummary>()
                .eq(ConversationSummary::getConversationId, conversationId));
    }

    /**
     * 构建摘要上下文字符串
     */
    public String buildSummaryContext(String conversationId) {
        ConversationSummary summary = getSummary(conversationId);
        if (summary == null || summary.getSummary() == null || summary.getSummary().isEmpty()) {
            return "";
        }
        return "之前的对话摘要：\n" + summary.getSummary() + "\n";
    }
}
