package com.edudu.ai.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.redis.RedisEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 嵌入存储配置 - RAG 向量检索
 */
@Slf4j
@Configuration
public class EmbeddingStoreConfig {

    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    /**
     * Redis 向量存储（生产环境推荐）
     */
    @Bean
    @ConditionalOnProperty(name = "langchain4j.ai.rag.store", havingValue = "redis", matchIfMissing = true)
    public EmbeddingStore<TextSegment> redisEmbeddingStore(RagConfig ragConfig) {
        log.info("使用 Redis 向量存储: host={}, port={}, index={}",
                ragConfig.getRedisHost(), ragConfig.getRedisPort(), ragConfig.getRedisIndex());
        return RedisEmbeddingStore.builder()
                .host(ragConfig.getRedisHost())
                .port(ragConfig.getRedisPort())
                .dimension(384)
                .indexName(ragConfig.getRedisIndex())
                .prefix("ai:embedding:")
                .build();
    }

    /**
     * 内存向量存储（开发/测试降级方案）
     */
    @Bean
    @ConditionalOnProperty(name = "langchain4j.ai.rag.store", havingValue = "memory")
    public EmbeddingStore<TextSegment> inMemoryEmbeddingStore() {
        log.info("使用内存向量存储（开发模式，重启后数据丢失）");
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel,
            RagConfig ragConfig) {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(dev.langchain4j.data.document.splitter.DocumentSplitters.recursive(
                        ragConfig.getChunkSize(),
                        ragConfig.getChunkOverlap()))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public ContentRetriever contentRetriever(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel,
            RagConfig ragConfig) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(ragConfig.getTopK())
                .minScore(ragConfig.getMinScore())
                .build();
    }
}
