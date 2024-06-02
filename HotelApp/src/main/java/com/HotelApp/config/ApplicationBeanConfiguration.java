package com.HotelApp.config;

import com.HotelApp.service.exception.ForbiddenUserException;
import com.HotelApp.service.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public static ModelMapper modelMapper() {
        return new ModelMapper();
    }


//    @Bean
//    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
//
//        Properties properties = new Properties();
//        properties.setProperty(UserNotFoundException.class.getSimpleName(), "404");
//        properties.setProperty(ForbiddenUserException.class.getSimpleName(), "403");
//
//        resolver.setExceptionMappings(properties);
//        resolver.setDefaultErrorView("error");
//
//        return resolver;
//    }
}
