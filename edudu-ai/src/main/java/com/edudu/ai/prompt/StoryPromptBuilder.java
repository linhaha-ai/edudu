package com.edudu.ai.prompt;

/**
 * 故事Prompt构建器
 */
public class StoryPromptBuilder {

    /**
     * 构建故事生成Prompt
     */
    public static String build(int age, String gender, String scene, String goal) {
        return String.format("""
                你是一位专业的儿童早教专家，熟悉蒙特梭利（Montessori Method）教育理念。

                请根据以下信息，为儿童创作一个适合的故事：

                【输入信息】
                - 年龄：%d岁
                - 性别：%s
                - 场景：%s
                - 教育目标：%s

                【要求】
                1. 故事必须适合该年龄理解（非常重要）
                2. 用简单语言，句子短
                3. 情节温和，不要有恐怖或冲突
                4. 必须有正向引导
                5. 控制在 300-500 字

                【输出格式（必须严格JSON）】
                {
                  "title": "故事标题",
                  "content": "故事正文",
                  "moral": "教育意义",
                  "interaction": [
                    "家长可以问孩子的问题1",
                    "家长可以问孩子的问题2"
                  ]
                }

                请严格按照JSON格式输出，不要添加任何额外说明。
                """, age, gender, scene, goal);
    }

    /**
     * 构建睡前故事Prompt
     */
    public static String buildBedtimeStory(int age, String gender, String goal) {
        return build(age, gender, "睡前", goal);
    }

    /**
     * 构建情绪引导故事Prompt
     */
    public static String buildEmotionStory(int age, String gender, String emotion) {
        return build(age, gender, "情绪管理", emotion);
    }
}
