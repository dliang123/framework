package com.yuntu.web.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 *  15/11/12.
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Authority {

    String [] permissions();
}
