package com.example.taskmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // разрешить для всех путей
                .allowedOrigins("*") // разрешить все домены
                .allowedMethods("*") // разрешить все HTTP-методы
                .allowedHeaders("*"); // разрешить все заголовки
    }
}

