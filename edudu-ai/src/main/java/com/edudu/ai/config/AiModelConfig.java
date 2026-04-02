package com.edudu.ai.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI模型配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AiModelConfig {

    private final AiProperties aiProperties;

    /**
     * 创建ChatLanguageModel（支持流式）
     */
    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        log.info("初始化流式ChatLanguageModel, 类型: {}", aiProperties.getModelType());
        
        return switch (aiProperties.getModelType().toLowerCase()) {
            case "openai" -> OpenAiStreamingChatModel.builder()
                    .apiKey(aiProperties.getApiKey())
                    .modelName(aiProperties.getModelName())
                    .temperature(aiProperties.getTemperature())
                    .timeout(java.time.Duration.ofSeconds(aiProperties.getTimeout()))
                    .baseUrl(aiProperties.getBaseUrl())
                    .build();
            default -> throw new IllegalArgumentException("不支持的模型类型: " + aiProperties.getModelType());
        };
    }

    /**
     * 创建ChatModel（非流式）
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        log.info("初始化ChatLanguageModel, 类型: {}", aiProperties.getModelType());
        
        return switch (aiProperties.getModelType().toLowerCase()) {
            case "openai" -> OpenAiChatModel.builder()
                    .apiKey(aiProperties.getApiKey())
                    .modelName(aiProperties.getModelName())
                    .temperature(aiProperties.getTemperature())
                    .timeout(java.time.Duration.ofSeconds(aiProperties.getTimeout()))
                    .baseUrl(aiProperties.getBaseUrl())
                    .build();
            default -> throw new IllegalArgumentException("不支持的模型类型: " + aiProperties.getModelType());
        };
    }
}
