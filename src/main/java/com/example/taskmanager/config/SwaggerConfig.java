package com.example.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("API для управления проектами и задачами")
                        .version("1.0"));
    }
}