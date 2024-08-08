package com.HotelApp.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static com.HotelApp.common.constants.InfoConstants.USERS_CACHE;
import static com.HotelApp.common.constants.SuccessConstants.CACHE_MANAGER_BUILD;
import static com.HotelApp.common.constants.SuccessConstants.CAFFEINE_BUILD;

@Configuration
@EnableCaching
public class ApplicationCacheConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ApplicationCacheConfiguration.class);

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(USERS_CACHE);
        log.info(CACHE_MANAGER_BUILD);

        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        log.info(CAFFEINE_BUILD);
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(200);
    }
}
