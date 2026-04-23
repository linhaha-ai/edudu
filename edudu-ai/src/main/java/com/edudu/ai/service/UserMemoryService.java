package com.edudu.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edudu.ai.domain.UserMemory;
import com.edudu.ai.mapper.UserMemoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户长期记忆服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserMemoryService extends ServiceImpl<UserMemoryMapper, UserMemory> {

    /**
     * 保存用户记忆（存在则更新）
     */
    public void saveMemory(Long userId, String key, String value) {
        saveMemory(userId, key, value, "conversation");
    }

    /**
     * 保存用户记忆（存在则更新）
     */
    public void saveMemory(Long userId, String key, String value, String source) {
        UserMemory existing = getOne(new LambdaQueryWrapper<UserMemory>()
                .eq(UserMemory::getUserId, userId)
                .eq(UserMemory::getMemoryKey, key));

        if (existing != null) {
            existing.setMemoryValue(value);
            existing.setSource(source);
            updateById(existing);
            log.debug("更新用户记忆: userId={}, key={}", userId, key);
        } else {
            UserMemory memory = new UserMemory();
            memory.setUserId(userId);
            memory.setMemoryKey(key);
            memory.setMemoryValue(value);
            memory.setSource(source);
            save(memory);
            log.debug("新增用户记忆: userId={}, key={}", userId, key);
        }
    }

    /**
     * 获取用户指定记忆
     */
    public UserMemory getMemory(Long userId, String key) {
        return getOne(new LambdaQueryWrapper<UserMemory>()
                .eq(UserMemory::getUserId, userId)
                .eq(UserMemory::getMemoryKey, key));
    }

    /**
     * 获取用户所有长期记忆
     */
    public List<UserMemory> getAllMemories(Long userId) {
        return list(new LambdaQueryWrapper<UserMemory>()
                .eq(UserMemory::getUserId, userId)
                .orderByDesc(UserMemory::getUpdateTime));
    }

    /**
     * 构建用户画像上下文字符串
     */
    public String buildMemoryContext(Long userId) {
        List<UserMemory> memories = getAllMemories(userId);
        if (memories.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("用户画像信息：\n");
        for (UserMemory memory : memories) {
            sb.append("- ").append(memory.getMemoryKey()).append(": ")
                    .append(memory.getMemoryValue()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 删除用户指定记忆
     */
    public boolean deleteMemory(Long userId, String key) {
        return remove(new LambdaQueryWrapper<UserMemory>()
                .eq(UserMemory::getUserId, userId)
                .eq(UserMemory::getMemoryKey, key));
    }
}
