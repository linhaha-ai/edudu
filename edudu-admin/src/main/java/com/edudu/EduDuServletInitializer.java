package com.edudu;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * web容器中进行部署
 * 
 * @author edudu
 */
public class EduDuServletInitializer extends SpringBootServletInitializer
{
    @Override
    protected @NonNull SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(AdminApplication.class);
    }
}
