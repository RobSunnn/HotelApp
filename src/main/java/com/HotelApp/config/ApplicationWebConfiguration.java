package com.HotelApp.config;

import com.HotelApp.util.LoggingInterceptor;
import com.HotelApp.util.SessionExpirationListener;
import jakarta.servlet.ServletContext;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ApplicationWebConfiguration implements WebMvcConfigurer {

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> servletContext.addListener(new SessionExpirationListener());
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/**");
    }

}
