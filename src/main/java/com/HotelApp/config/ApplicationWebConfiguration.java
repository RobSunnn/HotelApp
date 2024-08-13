package com.HotelApp.config;

import com.HotelApp.repository.ForbiddenRequestRepository;
import com.HotelApp.web.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ApplicationWebConfiguration implements WebMvcConfigurer {

    private final ForbiddenRequestRepository forbiddenRequestRepository;

    public ApplicationWebConfiguration(ForbiddenRequestRepository forbiddenRequestRepository) {
        this.forbiddenRequestRepository = forbiddenRequestRepository;
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor(forbiddenRequestRepository);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/**");
    }

}
