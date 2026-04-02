package com.edudu.ai.service;

import com.edudu.ai.prompt.TaskPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AI任务服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAiService extends AbstractAiService {

    /**
     * 生成每日早教任务
     * @param age 年龄
     * @param theme 主题（颜色/动物/情绪/语言）
     * @return JSON格式的任务内容
     */
    public String generateTask(int age, String theme) {
        String prompt = TaskPromptBuilder.build(age, theme);
        String result = chat(prompt);
        log.info("生成早教任务完成, 年龄: {}, 主题: {}", age, theme);
        return result;
    }

    /**
     * 生成适合年龄段的任务列表
     */
    public String generateTaskByAgeGroup(int age) {
        // 根据年龄段选择主题
        String theme;
        if (age < 2) {
            theme = "语言";  // 0-2岁：语言输入
        } else if (age < 4) {
            theme = "颜色";  // 2-4岁：认知启蒙
        } else {
            theme = "情绪";  // 4-6岁：情绪管理
        }
        return generateTask(age, theme);
    }
}
