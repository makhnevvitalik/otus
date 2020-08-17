package com.otus.demo;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "redis.cache.enabled", havingValue = "true", matchIfMissing = true)
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(CacheProperties cacheProperties) {
        return builder -> {
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()));
            Redis redisProperties = cacheProperties.getRedis();
            if (redisProperties.getTimeToLive() != null) {
                config = config.entryTtl(redisProperties.getTimeToLive());
            }
            if (redisProperties.getKeyPrefix() != null) {
                config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
            }
            if (!redisProperties.isCacheNullValues()) {
                config = config.disableCachingNullValues();
            }
            if (!redisProperties.isUseKeyPrefix()) {
                config = config.disableKeyPrefix();
            }
            builder.cacheDefaults(config);
        };
    }
}
