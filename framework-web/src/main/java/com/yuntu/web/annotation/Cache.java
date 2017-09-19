package com.yuntu.web.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 *  15/11/12.
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Cache {

    enum scope{
        cookie,request,session,header,parameter,requestBody,queryString
    }

    /**
     * 缓存的 key 在那里获取
     * @return
     */
    scope scope() default scope.request;

    /**
     * 缓存的 key
     * @return
     */
    String cacheKey();

    /**
     * 缓存时间
     * @return
     */
    long expire() default 15*60*1000;

}
