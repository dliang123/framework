package com.yuntu.base;

/**
 * Created by linxiaohui on 16/1/20.
 */
public interface Template <T> {

    T execute() throws Exception;
}
