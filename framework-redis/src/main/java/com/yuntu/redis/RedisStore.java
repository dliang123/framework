package com.yuntu.redis;

import com.yuntu.base.store.Store;
import com.yuntu.redis.util.RedisUtils;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by linxiaohui on 15/11/16.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class RedisStore implements Store {
    public String[] keys(String domainKey) {

        return(String[]) RedisUtils.boundHashOps(domainKey).keys().toArray(new String[]{});
    }

    public Object get(String key) {
        return RedisUtils.boundValueOps(key).get();
    }

    public void set(String key, Object value) {
        RedisUtils.boundValueOps(key).set(value);

    }

    public void delete(String key) {
        RedisUtils.delete(key);
    }

    public void put(String domainKey, String key, Object value) {
        boundHashOps(domainKey).put(key,value);
    }

    public void delete(String domainKey, String key) {
        boundHashOps(domainKey).delete(key);
    }

    public void invalidate(String domainKey) {
        RedisUtils.delete(domainKey);
    }

    public Object get(String domainKey, String key) {
        return boundHashOps(domainKey).get(key);
    }

    public <T> T get(String domainKey, String key, T defaultValue) {
        Object value = boundHashOps(domainKey).get(key);
        if( null == value )
            return defaultValue;
        return (T) value;
    }

    public Map<String, Object> entries(String domainKey) {
        return boundHashOps(domainKey).entries();
    }

    public void expire(String domainKey,long timeout, TimeUnit timeUnit) {
        RedisUtils.expire(domainKey,timeout,timeUnit);
    }

    public boolean hasKey(String domainKey) {
        return RedisUtils.hasKey(domainKey);
    }

    public int order() {
        return 0;
    }

    private BoundHashOperations<String ,String, Object> boundHashOps(String domainKey){
        BoundHashOperations<String ,String, Object> boundHashOps =(BoundHashOperations) RedisUtils.boundHashOps(domainKey);
        return boundHashOps;
    }
}
