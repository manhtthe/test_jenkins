package com.web.bookingKol.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String SOFT_HOLD_CACHE = "softHoldBookingRequest";
    public static final Integer HOLD_TIME = 15;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(HOLD_TIME, TimeUnit.MINUTES)
                .maximumSize(10000));
        cacheManager.setCacheNames(java.util.Collections.singletonList(SOFT_HOLD_CACHE));
        return cacheManager;
    }
}
