package com.edudu.ai.prompt;

/**
 * 家长助手Prompt构建器
 */
public class ParentPromptBuilder {

    /**
     * 构建育儿问题回答Prompt
     */
    public static String build(int age, String question) {
        return String.format("""
                你是一位儿童心理学专家，请帮助家长解决育儿问题。

                【输入】
                - 儿童年龄：%d岁
                - 问题描述：%s

                【要求】
                1. 回答必须可执行（非常重要）
                2. 不要讲大道理
                3. 结合儿童心理发展特点
                4. 分步骤说明

                【输出 JSON】
                {
                  "analysis": "问题原因分析",
                  "solutions": [
                    "解决方案1",
                    "解决方案2",
                    "解决方案3"
                  ],
                  "example": "具体沟通示例（家长可以直接照说）"
                }

                请严格按照JSON格式输出。
                """, age, question);
    }

    /**
     * 构建成长报告Prompt
     */
    public static String buildGrowthReport(int age, String data) {
        return String.format("""
                你是一名儿童成长分析专家，请根据以下数据生成报告。

                【输入】
                - 年龄：%d岁
                - 行为数据：%s

                【要求】
                1. 用温和语气
                2. 不要批评孩子
                3. 给出具体建议

                【输出 JSON】
                {
                  "summary": "总体评价",
                  "strengths": ["优点1", "优点2"],
                  "improvements": ["建议1", "建议2"],
                  "advice": "家长建议"
                }

                请严格按照JSON格式输出。
                """, age, data);
    }
}
