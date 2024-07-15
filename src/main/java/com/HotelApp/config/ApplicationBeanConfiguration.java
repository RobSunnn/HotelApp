package com.HotelApp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
