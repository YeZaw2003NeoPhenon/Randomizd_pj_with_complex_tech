package com.example.softwareProjectWithDocker.cache;


import com.example.softwareProjectWithDocker.entity.record.SoftwareEngineerRecord;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class RedisConfig {

//
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
        config.setPassword(RedisPassword.of("neo'sreliablerediscache"));
        return new LettuceConnectionFactory(config);
        // host name and port server
    }

    /**
     * Configures the cache manager for Redis caching. Defines global and specific cache behavior,
     * including time-to-live for caching and serialization rules.
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration redisConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // the concurrent time of caching
                .disableCachingNullValues() // don't do caching to null values
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        Map<String, RedisCacheConfiguration> cacheConfig = new HashMap<>();
        cacheConfig.put("allSoftwareEngineers", redisConfig.entryTtl(Duration.ofMinutes(60)));
        cacheConfig.put("softwareEngineerWithPagination", redisConfig.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .withInitialCacheConfigurations(cacheConfig)
                .cacheDefaults(redisConfig).build();
    }

//    /**
//     * Configures a RedisTemplate bean for custom Redis operations beyond caching.
//     * This method is commented out as it is currently unused but could be utilized
//     * for direct Redis operations if needed in the future.
//     */
//    @Bean
//    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
//       RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

}
