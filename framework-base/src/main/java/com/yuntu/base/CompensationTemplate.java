package com.yuntu.base;

/**
 * Created by linxiaohui on 15/12/8.
 */
public interface CompensationTemplate<T> extends Template<T>{

    T execute() throws Exception;

}
