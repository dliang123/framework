package com.yuntu.web.processor;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yuntu.base.store.Store;
import com.yuntu.common.utils.DigestUtils;
import com.yuntu.common.utils.StringUtils;
import com.yuntu.web.annotation.Cache;
import com.yuntu.web.request.BodyReaderHttpServletRequestWrapper;
import com.yuntu.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by linxiaohui on 15/11/16.
 */
public class CacheProcessor {

    private Store store;

    private Map<Method, Cache> cacheTarget = Maps.newConcurrentMap();
    private Set<Method> viewCache = Sets.newConcurrentHashSet();

    private static final Logger LOG = LoggerFactory.getLogger(CacheProcessor.class);



    protected String findCacheKey(Method method){

        HttpServletRequest request = WebUtils.getRequest();
        Cache cache = cacheTarget.get(method);

        if( null == cache ) return null;

        String prefix = method.toString() +":" + cache.cacheKey() + ":";
        switch ( cache.scope() ){

            case cookie:{
                Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request,cache.cacheKey());
                if( null != cookie )
                    return prefix + cookie.getValue();
            }
            case request:
                return prefix + StringUtils.nullToEmpty(request.getAttribute(cache.cacheKey()));
            case session:
                return prefix + StringUtils.nullToEmpty(request.getSession().getAttribute( cache.cacheKey() ));
            case parameter:
                return prefix + StringUtils.nullToEmpty(request.getParameter(cache.cacheKey()));
            case header:
                return prefix + StringUtils.nullToEmpty(request.getHeader(cache.cacheKey()));
            case queryString:
                return prefix + WebUtils.getRequest().getQueryString();
            case requestBody:
                String body = BodyReaderHttpServletRequestWrapper.requestBody(request);
                return prefix + DigestUtils.digest(body, DigestUtils.Digest.MD5_32);

            default: return null;
        }
    }



    private boolean canCache(Method method){
        return  store!=null && cacheTarget.containsKey( method );
    }

    public void init(ApplicationContext context,Collection<HandlerMethod> handlerMethods) {

        LOG.info("开始初始化缓存拦截器 缓存点!");

        cacheTarget.clear();
        if(!ObjectUtils.isEmpty(handlerMethods)){
            for(HandlerMethod handlerMethod : handlerMethods){

                Cache cache = handlerMethod.getMethodAnnotation(Cache.class);
                if( cache == null )
                    cache = handlerMethod.getBeanType().getAnnotation(Cache.class);

                if( null != cache && !handlerMethod.isVoid() ){
                    cacheTarget.put(handlerMethod.getMethod(), cache);
                    if( null == handlerMethod.getMethodAnnotation(ResponseBody.class) ){
                        viewCache.add( handlerMethod.getMethod() );
                    }
                }

            }
        }

        try {
            store = context.getBean(Store.class);
        }catch (Exception e){
            LOG.warn(e.getMessage());
        }
        LOG.info("初始化缓存拦截器 缓存点完成!");

    }

    public Object methodValue(Method method) {

        if( !canCache(method) ){
            return null;
        }

        String key = findCacheKey(method);
        if( !Strings.isNullOrEmpty(key) ){
            return store.get(key);
        }

        return null;

    }

    public void methodValue(Method method, Object value) {

        if(  null == method || null == value || !canCache(method)  ){
            return;
        }

        String key = findCacheKey(method);
        if( Strings.isNullOrEmpty(key) ) return;

        try {
            //value = viewCache.contains(hashCode()) ? value : JSONUtils.toJSON(value);
            store.set(key, value);
            store.expire(key,cacheTarget.get(method).expire(), TimeUnit.MILLISECONDS);
        }catch (Exception e){
            LOG.warn("缓存 {} 结果 {} 失败",method,value,e);
        }
    }

}
