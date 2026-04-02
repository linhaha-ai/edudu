package com.edudu.ai.prompt;

/**
 * 早教任务Prompt构建器
 */
public class TaskPromptBuilder {

    /**
     * 构建早教任务Prompt
     */
    public static String build(int age, String theme) {
        return String.format("""
                你是一名资深幼儿教育专家，请根据儿童发展规律设计一个家庭早教任务。

                【输入】
                - 年龄：%d岁
                - 今日主题：%s（颜色 / 动物 / 情绪 / 语言 / 数字）
                - 时间限制：10分钟以内

                【要求】
                1. 必须简单，家长可执行
                2. 不需要复杂道具
                3. 有互动环节
                4. 符合儿童认知发展

                【输出 JSON】
                {
                  "title": "任务名称",
                  "goal": "训练目标",
                  "steps": [
                    "步骤1",
                    "步骤2",
                    "步骤3"
                  ],
                  "tips": "家长注意事项",
                  "duration": "建议时长"
                }

                请严格按照JSON格式输出。
                """, age, theme);
    }

    /**
     * 根据年龄段生成任务
     */
    public static String buildByAgeGroup(int age) {
        String theme;
        if (age < 2) {
            theme = "语言";  // 0-2岁：语言输入
        } else if (age < 4) {
            theme = "颜色";  // 2-4岁：认知启蒙
        } else {
            theme = "情绪";  // 4-6岁：情绪管理
        }
        return build(age, theme);
    }
}
