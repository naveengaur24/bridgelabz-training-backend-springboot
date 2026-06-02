package com.fundoonotes.fundoo_notes.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    void set(String key, Object value);

    void setWithExpiry(String key, Object value, long timeout, TimeUnit timeUnit);

    Object get(String key);

    void delete(String key);

    boolean exists(String key);
}