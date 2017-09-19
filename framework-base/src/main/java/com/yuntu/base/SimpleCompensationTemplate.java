package com.yuntu.base;

/**
 * Created by linxiaohui on 15/12/8.
 */
public interface SimpleCompensationTemplate<T> extends CompensationTemplate<T> {

    int retries();
    Class<? extends Exception>[] retriesFor();
}
