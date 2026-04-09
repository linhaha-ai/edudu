package com.edudu.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * API服务模块启动类
 * 专门用于前端接口交互，独立打包部署
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.edudu"})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        System.out.println("""
                ╔════════════════════════════════════════════════════════════════════╗
                ║                                                                    ║
                ║          AI早教陪伴平台 - API服务启动成功                              ║
                ║                                                                    ║
                ║          Swagger文档: http://localhost:8081/api/swagger-ui.html     ║
                ║                                                                    ║
                ╚════════════════════════════════════════════════════════════════════╝
                """);
    }
}
