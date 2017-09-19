package com.yuntu.base;

/**
 * Created by linxiaohui on 15/12/10.
 */
@SuppressWarnings("serial")
public abstract class BusinessRuntimeException extends RuntimeException {

    public abstract int getCode();

    public BusinessRuntimeException() {
    }

    public BusinessRuntimeException(String message) {
        super(message);
    }

    public BusinessRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessRuntimeException(Throwable cause) {
        super(cause);
    }

    public BusinessRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
