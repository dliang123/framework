package com.yuntu.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务开关
 *
 * 适用于字段 基本数据类型,枚举(枚举目前只支持 name 形式)
 * ( 不适用于程序启动时就要验证的数值 ,同一开关的 key 不要映射两次)
 * Created by linxiaohui on 16/1/13.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Switch {

    String key();
    String des();
}
