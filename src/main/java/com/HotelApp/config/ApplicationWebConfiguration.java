package com.HotelApp.config;

import com.HotelApp.repository.ForbiddenRequestRepository;
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

    private final ForbiddenRequestRepository forbiddenRequestRepository;

    public ApplicationWebConfiguration(ForbiddenRequestRepository forbiddenRequestRepository) {
        this.forbiddenRequestRepository = forbiddenRequestRepository;
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> servletContext.addListener(new SessionExpirationListener());
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
