package com.yuntu.web.filter;

import com.google.common.base.Strings;
import com.yuntu.web.constant.PreventInjectionStrategy;
import com.yuntu.web.request.PreventInjectionRequest;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*
 * Author:
 * Date:     14-11-6
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-6           00000001         创建文件
 *
 */
public class PreventInjectionFilter implements Filter {

    //策略
    private PreventInjectionStrategy strategy = PreventInjectionStrategy.replace;

    public void init(FilterConfig filterConfig) throws ServletException {

        //要过滤的关键字
        String leaches = filterConfig.getInitParameter("leaches");
        if( !Strings.isNullOrEmpty(leaches) ){
            String [] _leaches= leaches.split(",");
            if( _leaches.length>0 ){
                PreventInjectionRequest.setLeaches(_leaches);
            }
        }

        String strategyName=filterConfig.getInitParameter("strategy");
        if(!Strings.isNullOrEmpty(strategyName )){
            try{
                strategy=PreventInjectionStrategy.valueOf(strategyName);
            }catch (Exception e){

            }
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        PreventInjectionRequest preventInjectionRequest= new PreventInjectionRequest((HttpServletRequest) request);
        preventInjectionRequest.setStrategy(strategy);
        chain.doFilter(preventInjectionRequest,response);
    }

    public void destroy() {

    }
}
