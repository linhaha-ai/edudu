package com.edudu.ai.config;

import com.edudu.ai.memory.RedisChatMemoryStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * RAG 配置类
 */
@Configuration
@Getter
public class RagConfig {

    @Value("${langchain4j.ai.rag.top-k:3}")
    private int topK;

    @Value("${langchain4j.ai.rag.min-score:0.7}")
    private double minScore;

    @Value("${langchain4j.ai.rag.chunk-size:500}")
    private int chunkSize;

    @Value("${langchain4j.ai.rag.chunk-overlap:100}")
    private int chunkOverlap;

    @Value("${langchain4j.ai.memory.max-messages:20}")
    private int maxMessages;

    @Value("${langchain4j.ai.memory.ttl-hours:24}")
    private int ttlHours;

    @Value("${langchain4j.ai.memory.summary-threshold:40}")
    private int summaryThreshold;

    @Value("${langchain4j.ai.rag.redis-index:edudu-knowledge}")
    private String redisIndex;

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${langchain4j.ai.memory-extraction.enabled:true}")
    private boolean memoryExtractionEnabled;

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        return new RedisChatMemoryStore(redisTemplate, objectMapper);
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider(RedisChatMemoryStore store) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(maxMessages)
                .chatMemoryStore(store)
                .build();
    }
}
