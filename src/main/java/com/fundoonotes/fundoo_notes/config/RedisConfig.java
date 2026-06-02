package com.fundoonotes.fundoo_notes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {  // it's the main class to communicate with redis..

        RedisTemplate<String, Object> template = new RedisTemplate<>();   // key = email id, value = otp

        // Connection factory set ki..
        template.setConnectionFactory(connectionFactory);

        // Key → String format mein store hoga
        template.setKeySerializer(new StringRedisSerializer());

        // Value, java object → JSON format mein store hoga
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash key → String format  and hash is a data staructure like object..
        template.setHashKeySerializer(new StringRedisSerializer());

        // Hash value → JSON format(readable string)
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();  //RedisTemplate finalize kia..
        return template;
    }
}