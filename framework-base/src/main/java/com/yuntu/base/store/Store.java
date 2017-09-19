package com.yuntu.base.store;

import java.util.concurrent.TimeUnit;

/*
 * Author:   林晓辉
 * Date:     14-11-6
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-6           00000001         创建文件
 *
 */
public interface Store {

    String[] keys(String domainKey) ;

    Object get(String key);

    void set(String key, Object value);

    void delete(String key);

    void put(String domainKey, String key, Object o);

    void delete(String domainKey, String key);

    void invalidate(String domainKey);

    Object get(String domainKey, String key);

    <T> T get(String domainKey, String key, T defaultValue);

    java.util.Map<String,Object> entries(String domainKey);

    void expire(String domainKey, long timeOut, TimeUnit timeUnit);

    boolean hasKey(String domainKey);

    int order();
}
