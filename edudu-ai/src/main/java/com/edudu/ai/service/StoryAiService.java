package com.edudu.ai.service;

import com.edudu.ai.prompt.StoryPromptBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AI故事服务
 */
@Slf4j
@Service
public class StoryAiService extends AbstractAiService {

    /**
     * 生成故事
     * @param age 年龄
     * @param gender 性别
     * @param scene 场景
     * @param goal 教育目标
     * @return JSON格式的故事内容
     */
    public String generateStory(int age, String gender, String scene, String goal) {
        String prompt = StoryPromptBuilder.build(age, gender, scene, goal);
        String result = chat(prompt);
        log.info("生成故事完成, 年龄: {}, 场景: {}, 目标: {}", age, scene, goal);
        return result;
    }

    /**
     * 生成睡前故事
     */
    public String generateBedtimeStory(int age, String gender, String goal) {
        return generateStory(age, gender, "睡前", goal);
    }

    /**
     * 生成情绪引导故事
     */
    public String generateEmotionStory(int age, String gender, String emotion) {
        return generateStory(age, gender, "情绪管理", emotion);
    }

    /**
     * 生成品德故事
     */
    public String generateMoralStory(int age, String gender, String moral) {
        return generateStory(age, gender, "品德教育", moral);
    }
}
