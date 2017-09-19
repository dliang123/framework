package com.yuntu.base.specification;

/**
 * Created by linxiaohui on 2017/4/11.
 */
public class UnknownPropertyException extends IllegalArgumentException{

    public UnknownPropertyException() {
    }

    public UnknownPropertyException(String s) {
        super(s);
    }

    public UnknownPropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownPropertyException(Throwable cause) {
        super(cause);
    }
}
