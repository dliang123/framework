package com.yuntu.common.exception;

/**
 *  15/8/27.
 */
public interface ExceptionConvert<T extends Throwable> {

    T cast(String msg);
    T cast(Throwable throwable);
}
