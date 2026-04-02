package com.edudu.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI能力模块启动类
 * 基于LangChain4j集成各大语言模型
 */
@SpringBootApplication
public class EduAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduAiApplication.class, args);
    }
}
