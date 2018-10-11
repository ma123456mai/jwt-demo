package com.jwt.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

/**
 * @author Mr丶s
 * @ClassName RedisConfig
 * @Version V1.0
 * @Date 2018/10/21 11:14
 * @Description
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate redis = new RedisTemplate();
        GenericToStringSerializer<String> keySerializer = new GenericToStringSerializer<String>(String.class);
        redis.setKeySerializer(keySerializer);
        redis.setHashKeySerializer(keySerializer);
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        redis.setValueSerializer(valueSerializer);
        redis.setHashValueSerializer(valueSerializer);
        redis.setConnectionFactory(connectionFactory);

        return redis;
    }


    @Bean(name = "StringRedisTemplate")
    public RedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate redis = new StringRedisTemplate();
        GenericToStringSerializer<String> keySerializer = new GenericToStringSerializer<String>(String.class);
        redis.setKeySerializer(keySerializer);
        redis.setHashKeySerializer(keySerializer);
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        redis.setValueSerializer(valueSerializer);
        redis.setHashValueSerializer(valueSerializer);
        redis.setConnectionFactory(connectionFactory);

        return redis;
    }

    /**
     * 配置redis缓存管理对象
     *
     * @param redisTemplate
     * @return
     */
    @Bean(name = "integerRedisTemplate")
    public RedisTemplate integerRedisTemplate(RedisConnectionFactory redisTemplate) {
        return new StringRedisTemplate(redisTemplate);
    }

    /**
     * 配置redis缓存管理对象
     *
     * @param redisTemplate
     * @return
     */
    @Bean(name = "longRedisTemplate")
    public RedisTemplate longRedisTemplate(RedisConnectionFactory redisTemplate) {
        return new StringRedisTemplate(redisTemplate);
    }




}
