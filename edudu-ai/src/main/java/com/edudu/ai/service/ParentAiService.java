package com.edudu.ai.service;

import com.edudu.ai.prompt.ParentPromptBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AI家长助手服务
 */
@Slf4j
@Service
public class ParentAiService extends AbstractAiService {

    /**
     * 回答家长育儿问题
     * @param age 孩子年龄
     * @param question 问题描述
     * @return JSON格式的回答
     */
    public String answerQuestion(int age, String question) {
        String prompt = ParentPromptBuilder.build(age, question);
        String result = chat(prompt);
        log.info("回答育儿问题完成, 年龄: {}, 问题: {}", age, question.substring(0, Math.min(50, question.length())));
        return result;
    }

    /**
     * 生成成长报告
     * @param age 孩子年龄
     * @param data 行为数据（JSON格式）
     * @return JSON格式的成长报告
     */
    public String generateGrowthReport(int age, String data) {
        String prompt = ParentPromptBuilder.buildGrowthReport(age, data);
        String result = chat(prompt);
        log.info("生成成长报告完成, 年龄: {}", age);
        return result;
    }
}
