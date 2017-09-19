package com.yuntu.mybatis.hilo;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by 林晓辉 on 2015/3/5.
 * 高地位算法 id 注解
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface HiLo {

    /** id 字段 **/
    String id();
    /** 使用的高低位配置名称 **/
    String sequenceName();
    /** 是否重写已有id **/
    boolean rewrite() default true;
}
