package com.yuntu.base.config;

/**
 *  15/11/11.
 */
@SuppressWarnings("serial")
public class ConfigNoFoundException extends RuntimeException {

    public ConfigNoFoundException() {
    }

    public ConfigNoFoundException(String message) {
        super(message);
    }

    public ConfigNoFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigNoFoundException(Throwable cause) {
        super(cause);
    }

    public ConfigNoFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
