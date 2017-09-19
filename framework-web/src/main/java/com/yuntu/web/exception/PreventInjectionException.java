package com.yuntu.web.exception;


/*
 * Author:   林晓辉
 * Date:     14-11-6
 * Description: 防注入的运行时异常
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-6           00000001         创建文件
 *
 */
@SuppressWarnings("serial")
public class PreventInjectionException extends RuntimeException {


    public PreventInjectionException() {
    }

    public PreventInjectionException(String message) {
        super(message);
    }

    public PreventInjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreventInjectionException(Throwable cause) {
        super(cause);
    }

    public PreventInjectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
