package com.edudu.ai.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于 Redis 的对话历史存储
 */
@Slf4j
@Component
public class RedisChatMemoryStore {

    private static final String KEY_PREFIX = "ai:memory:";
    private static final Duration DEFAULT_TTL = Duration.ofHours(24);
    private static final int MAX_MESSAGES = 20;

    private final StringRedisTemplate redisTemplate;

    public RedisChatMemoryStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取对话历史
     */
    public List<String> getMessages(String conversationId) {
        String key = getKey(conversationId);
        List<String> list = redisTemplate.opsForList().range(key, 0, -1);
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * 添加用户消息
     */
    public void addUserMessage(String conversationId, String message) {
        String key = getKey(conversationId);
        redisTemplate.opsForList().rightPush(key, "USER: " + message);
        trimMessages(key);
        redisTemplate.expire(key, DEFAULT_TTL);
    }

    /**
     * 添加AI消息
     */
    public void addAiMessage(String conversationId, String message) {
        String key = getKey(conversationId);
        redisTemplate.opsForList().rightPush(key, "AI: " + message);
        trimMessages(key);
        redisTemplate.expire(key, DEFAULT_TTL);
    }

    /**
     * 清除对话历史
     */
    public void clear(String conversationId) {
        String key = getKey(conversationId);
        redisTemplate.delete(key);
        log.info("清除对话历史: conversationId={}", conversationId);
    }

    private String getKey(String conversationId) {
        return KEY_PREFIX + conversationId;
    }

    private void trimMessages(String key) {
        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size > MAX_MESSAGES) {
            redisTemplate.opsForList().trim(key, -MAX_MESSAGES, -1);
        }
    }
}