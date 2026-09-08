package com.whitechoke.remind.configuration;

import com.whitechoke.remind.user.dto.UserGetResponseDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Map;

@EnableCaching
@Configuration
public class RedisConfig {
    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory,
            ObjectMapper objectMapper
    ) {
        var baseConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                "users", cacheFor(baseConfig, objectMapper, UserGetResponseDto.class)
        );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(baseConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .transactionAware()
                .build();
    }

    private <T> RedisCacheConfiguration cacheFor(
            RedisCacheConfiguration baseConfig,
            ObjectMapper objectMapper,
            Class<T> type
    ) {
        var serializer = new JacksonJsonRedisSerializer<>(objectMapper, type);
        return baseConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer)
        );
    }
}
