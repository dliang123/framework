package com.yuntu.web.util;

import org.springframework.core.NamedThreadLocal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Copyright (C), 2012-2014,
 * Author:
 * Date:     15-1-22
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            15-1-22           00000001         创建文件
 *
 */
public class HolderUtils {

    private static final ThreadLocal<HttpServletRequest> requestHolder = new NamedThreadLocal<HttpServletRequest>("native request");
    private static final ThreadLocal<HttpServletResponse> responseHolder = new NamedThreadLocal<HttpServletResponse>("native response");

    public static void setRequest(HttpServletRequest servletRequest){
        requestHolder.set(servletRequest);
    }

    public static void setResponse(HttpServletResponse servletResponse){
        responseHolder.set(servletResponse);
    }

    public static HttpServletRequest getRequest() {
        return requestHolder.get();
    }

    public static HttpServletResponse getResponse() {
        return responseHolder.get();
    }
    
    public static void removeRequest() {
    	requestHolder.remove();
    }
    
    public static void removeResponse() {
    	responseHolder.remove();
    }
}
