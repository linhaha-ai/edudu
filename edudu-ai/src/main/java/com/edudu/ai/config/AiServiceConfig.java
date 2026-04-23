package com.edudu.ai.config;

import com.edudu.ai.service.AiAssistant;
import com.edudu.ai.service.ParentAssistant;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 服务配置 - 基于 LangChain4j AiServices
 */
@Slf4j
@Configuration
public class AiServiceConfig {

    @Autowired(required = false)
    private ChatLanguageModel chatLanguageModel;

    /**
     * RAG AI 助手（含对话记忆 + 向量检索）
     */
    @Bean
    @ConditionalOnBean(ChatLanguageModel.class)
    public AiAssistant aiAssistant(ChatMemoryProvider chatMemoryProvider,
                                   ContentRetriever contentRetriever) {
        if (chatLanguageModel == null) {
            log.warn("ChatLanguageModel 未配置，无法创建 AiAssistant");
            return null;
        }
        log.info("初始化 RAG AI 助手（含对话记忆 + 向量检索）");
        return AiServices.builder(AiAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .contentRetriever(contentRetriever)
                .build();
    }

    /**
     * 家长助手（含对话记忆）
     */
    @Bean
    @ConditionalOnBean(ChatLanguageModel.class)
    public ParentAssistant parentAssistant(ChatMemoryProvider chatMemoryProvider) {
        if (chatLanguageModel == null) {
            log.warn("ChatLanguageModel 未配置，无法创建 ParentAssistant");
            return null;
        }
        log.info("初始化家长助手（含对话记忆）");
        return AiServices.builder(ParentAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
