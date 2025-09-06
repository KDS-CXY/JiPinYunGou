package com.baizhanshopping.shopping_seckill_service.redis;

import lombok.extern.java.Log;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLock {
    @Autowired
    private RedissonClient redissonClient;
    public boolean lock(String key, Long expireTime){
        RLock lock = redissonClient.getLock("lock" + key);
        try {
            return lock.tryLock(expireTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    public void unlock(String key){
        RLock lock = redissonClient.getLock("lock" + key);
        if(lock.isLocked()){
            lock.unlock();
        }
    }
}
