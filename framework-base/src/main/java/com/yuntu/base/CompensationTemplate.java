package com.yuntu.base;

/**
 *  15/12/8.
 */
public interface CompensationTemplate<T> extends Template<T>{

    T execute() throws Exception;

}
