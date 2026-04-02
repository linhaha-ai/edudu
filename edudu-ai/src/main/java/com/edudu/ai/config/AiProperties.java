package com.edudu.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiProperties {

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 模型类型：openai, azure, dashscope
     */
    private String modelType = "openai";

    /**
     * 模型名称
     */
    private String modelName = "gpt-4o-mini";

    /**
     * Base URL（用于代理或自托管）
     */
    private String baseUrl;

    /**
     * 超时时间（秒）
     */
    private Integer timeout = 30;

    /**
     * temperature（0-1，控制随机性）
     */
    private Double temperature = 0.7;

    /**
     * 最大token数
     */
    private Integer maxTokens = 2048;

    /**
     * 是否启用缓存
     */
    private Boolean enableCache = true;

    /**
     * Azure OpenAI 配置
     */
    private Azure azure = new Azure();

    /**
     * 阿里云通义千问配置
     */
    private DashScope dashscope = new DashScope();

    @Data
    public static class Azure {
        private String endpoint;
        private String apiKey;
        private String deploymentName;
        private String apiVersion;
    }

    @Data
    public static class DashScope {
        private String apiKey;
    }
}
