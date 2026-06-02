package com.fundoonotes.fundoo_notes.service.implementation;
import com.fundoonotes.fundoo_notes.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override   // value store..
    public void set(String key, Object value) {
        log.debug("Redis SET: key={}", key);
        redisTemplate.opsForValue().set(key, value);     // there are diff. data structure in redis..like for string - opsForValue  and opsforList
    }

    @Override   // value store with expiry..
    public void setWithExpiry(String key, Object value, long timeout, TimeUnit timeUnit) {
        log.debug("Redis SET with expiry: key={}, timeout={} {}", key, timeout, timeUnit);
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }


    // fetch the value from redis..
    @Override
    public Object get(String key) {
        log.debug("Redis GET: key={}", key);
        return redisTemplate.opsForValue().get(key);
    }

    //  otp is one time use, so we have to delete key - email
    @Override
    public void delete(String key) {
        log.debug("Redis DELETE: key={}", key);
        redisTemplate.delete(key);
    }

    // check key is present or not in redis..
    @Override
    public boolean exists(String key) {
        log.debug("Redis EXISTS: key={}", key);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}