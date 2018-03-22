/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-10-15 14:41 创建
 *
 */
package com.alibaba.dubbo.examples.cache;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author daidai@yiji.com
 */
public class RedisService<K, F, V> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private RedisTemplate<K, V> redisTemplate;
    private long expirationSecs;

    public RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long getExpirationSecs() {
        return expirationSecs;
    }

    public void setExpirationSecs(long expirationSecs) {
        this.expirationSecs = expirationSecs;
    }

    public void put(K key, V value) {
        try {
            ValueOperations<K, V> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value, expirationSecs, TimeUnit.SECONDS);
        } catch (Throwable e) {
            logger.error(e);
        }
    }

    public void put(K key, F field, V value) {
        try {
            HashOperations<K, F, V> valueOperations = redisTemplate.opsForHash();
            valueOperations.put(key, field, value);
        } catch (Throwable e) {
            logger.error(e);
        }
    }

    public V get(K key) {
        try {
            ValueOperations<K, V> valueOperations = redisTemplate.opsForValue();
            return valueOperations.get(key);
        } catch (Throwable e) {
            logger.error(e);
            return null;
        }
    }


    public V get(K key, F field) {
        try {
            HashOperations<K, F, V> valueOperations = redisTemplate.opsForHash();
            return valueOperations.get(key, field);
        } catch (Throwable e) {
            logger.error(e);
            return null;
        }
    }
}
