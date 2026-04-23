package com.edudu.ai.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于 Redis 的对话历史存储，实现 LangChain4j ChatMemoryStore 接口
 */
@Slf4j
@Component
public class RedisChatMemoryStore implements ChatMemoryStore {

    private static final String KEY_PREFIX = "ai:chat-memory:";
    private static final Duration DEFAULT_TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisChatMemoryStore(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String key = getKey(memoryId);
        List<String> jsonList = redisTemplate.opsForList().range(key, 0, -1);
        if (jsonList == null || jsonList.isEmpty()) {
            return new ArrayList<>();
        }

        List<ChatMessage> messages = new ArrayList<>();
        for (String json : jsonList) {
            try {
                ChatMessage message = objectMapper.readValue(json, new TypeReference<ChatMessage>() {});
                messages.add(message);
            } catch (JsonProcessingException e) {
                log.warn("反序列化 ChatMessage 失败: {}", json, e);
            }
        }
        return messages;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String key = getKey(memoryId);
        // 删除旧消息
        redisTemplate.delete(key);
        // 添加新消息
        if (messages != null && !messages.isEmpty()) {
            for (ChatMessage message : messages) {
                try {
                    String json = objectMapper.writeValueAsString(message);
                    redisTemplate.opsForList().rightPush(key, json);
                } catch (JsonProcessingException e) {
                    log.error("序列化 ChatMessage 失败", e);
                }
            }
        }
        redisTemplate.expire(key, DEFAULT_TTL);
        log.debug("更新对话历史: memoryId={}, 消息数={}", memoryId, messages != null ? messages.size() : 0);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String key = getKey(memoryId);
        redisTemplate.delete(key);
        log.info("清除对话历史: memoryId={}", memoryId);
    }

    /**
     * 获取对话消息数量
     */
    public long getMessageCount(Object memoryId) {
        String key = getKey(memoryId);
        Long size = redisTemplate.opsForList().size(key);
        return size != null ? size : 0;
    }

    /**
     * 修剪消息，只保留最近 N 条
     */
    public void trimMessages(Object memoryId, int keepCount) {
        String key = getKey(memoryId);
        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size > keepCount) {
            redisTemplate.opsForList().trim(key, -keepCount, -1);
            log.info("修剪对话历史: memoryId={}, 保留{}条, 原有{}条", memoryId, keepCount, size);
        }
    }

    private String getKey(Object memoryId) {
        return KEY_PREFIX + memoryId.toString();
    }
}
