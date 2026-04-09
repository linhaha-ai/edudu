package com.edudu.ai.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * AI服务基类
 */
@Slf4j
@Service
public abstract class AbstractAiService {

    @Lazy
    @Autowired(required = false)
    protected ChatLanguageModel chatLanguageModel;
    
    /**
     * 检查AI功能是否可用
     * @return true=可用 false=不可用
     */
    protected boolean isAiAvailable() {
        return chatLanguageModel != null;
    }
    
    /**
     * 调用AI生成内容
     * @param prompt 提示词
     * @return AI响应，如果AI不可用则返回null
     */
    protected String chat(String prompt) {
        if (!isAiAvailable()) {
            log.warn("ChatLanguageModel bean 未配置，AI功能不可用");
            return null;
        }
        
        log.debug("发送请求到AI, prompt长度: {}", prompt.length());
        long startTime = System.currentTimeMillis();
        
        String response = chatLanguageModel.generate(prompt);
        
        long costTime = System.currentTimeMillis() - startTime;
        log.debug("AI响应完成, 耗时: {}ms, 响应长度: {}", costTime, response.length());
        
        return response;
    }

}
