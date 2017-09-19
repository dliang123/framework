package com.yuntu.web.interceptor;

import com.yuntu.web.AuthorityProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于资源的权限处理
 * Created by linxiaohui on 15/11/12.
 */
public class ResourcesAuthorityInterceptor extends HandlerInterceptorAdapter {

    @Autowired(required = false)
    protected AuthorityProcess authorityProcess;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if( handler instanceof HandlerMethod && null != authorityProcess){

            return authorityProcess.validationByResources( request.getRequestURI() );
        }
        return true;
    }

}
