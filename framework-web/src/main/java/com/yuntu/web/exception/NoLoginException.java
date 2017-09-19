package com.yuntu.web.exception;

/*
 * Copyright (C), 2012-2014,
 * Author:   林晓辉
 * Date:     14-12-5
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-12-5           00000001         创建文件
 *
 */
@SuppressWarnings("serial")
public class NoLoginException extends RuntimeException {
    public NoLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoLoginException(Throwable cause) {
        super(cause);
    }

    public NoLoginException() {

    }
}
