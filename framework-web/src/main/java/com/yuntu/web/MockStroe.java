package com.yuntu.web;

import com.yuntu.base.store.Store;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
 * Author:
 * Date:     14-11-6
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-6           00000001         创建文件
 *
 */
public class MockStroe implements Store {

    public String[] keys(String domainKey) {
        return new String[0];
    }

    public Object get(String key) {
        return null;
    }

    public void set(String key, Object value) {

    }

    public void delete(String key) {

    }

    public void put(String domainKey, String key, Object o) {

    }

    public void delete(String domainKey, String key) {

    }

    public void invalidate(String domainKey) {

    }

    public Object get(String domainKey, String key) {
        return null;
    }

    public <T> T get(String domainKey, String key, T defaultValue) {
        return null;
    }

    public Map<String, Object> entries(String domainKey) {
        return null;
    }

    public void expire(String domainKey, long timeOut, TimeUnit timeUnit) {

    }

    public boolean hasKey(String domainKey) {
        return true;
    }

    public int order() {
        return 0;
    }
}
