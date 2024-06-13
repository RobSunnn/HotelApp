package com.HotelApp.config;

import com.HotelApp.util.SessionExpirationListener;
import jakarta.servlet.ServletContext;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationWebConfiguration {

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext)  {
                servletContext.addListener(new SessionExpirationListener());
            }
        };
    }
}
