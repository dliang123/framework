package com.yuntu.web.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 *  16/7/7.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface EnableEtag {

}