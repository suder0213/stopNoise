package com.ll.stopnoise.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://floor.noise.quantification.s3-website.ap-northeast-2.amazonaws.com", "https://noise-stop.o-r.kr")
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT", "OPTIONS") // OPTIONS 메서드도 추가
                .allowCredentials(true);
    }
}

