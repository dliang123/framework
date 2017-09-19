package com.yuntu.base;

/**
 * Created by linxiaohui on 15/12/10.
 */
@SuppressWarnings("serial")
public abstract class BusinessException extends Exception {

    public abstract int getCode();


    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
