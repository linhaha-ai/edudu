package com.edudu.ai.config;

import com.edudu.ai.memory.RedisChatMemoryStore;
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

    // Getter 方法供外部使用
    @Value("${langchain4j.ai.rag.top-k:3}")
    private int topK;

    @Value("${langchain4j.ai.rag.min-score:0.7}")
    private double minScore;

    @Value("${langchain4j.ai.rag.chunk-size:1000}")
    private int chunkSize;

    @Value("${langchain4j.ai.rag.chunk-overlap:200}")
    private int chunkOverlap;

    @Value("${langchain4j.ai.memory.max-messages:20}")
    private int maxMessages;

    @Value("${langchain4j.ai.memory.ttl-hours:24}")
    private int ttlHours;

    @Value("${langchain4j.ai.rag.embedding-file:./data/ai-embeddings.json}")
    private String embeddingFile;

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore(StringRedisTemplate redisTemplate) {
        return new RedisChatMemoryStore(redisTemplate);
    }
}