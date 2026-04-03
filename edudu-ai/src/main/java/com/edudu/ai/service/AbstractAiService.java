package com.edudu.ai.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AI服务基类
 */
@Slf4j
@Service
public abstract class AbstractAiService {

    @Autowired
    protected ChatLanguageModel chatLanguageModel;
    
    /**
     * 调用AI生成内容
     * @param prompt 提示词
     * @return AI响应
     */
    protected String chat(String prompt) {
        log.debug("发送请求到AI, prompt长度: {}", prompt.length());
        long startTime = System.currentTimeMillis();
        
        String response = chatLanguageModel.generate(prompt);
        
        long costTime = System.currentTimeMillis() - startTime;
        log.debug("AI响应完成, 耗时: {}ms, 响应长度: {}", costTime, response.length());
        
        return response;
    }

}
