package com.yuntu.web.interceptor;

import com.google.common.collect.Maps;
import com.yuntu.web.AuthorityProcess;
import com.yuntu.web.annotation.Authority;
import com.yuntu.web.exception.NoAuthorityException;
import com.yuntu.web.util.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * 基于注解的 权限拦截器
 * Created by linxiaohui on 15/11/12.
 */
public class AnnotationAuthorityInterceptor extends HandlerInterceptorAdapter implements org.springframework.context.ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationAuthorityInterceptor.class);

    protected AuthorityProcess authorityProcess;

    protected ApplicationContext applicationContext;

    private Map<Method,String[]> permissions = Maps.newConcurrentMap();



    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if( handler instanceof HandlerMethod){
            Method method = ((HandlerMethod) handler).getMethod();
            if( handler instanceof HandlerMethod && null != authorityProcess && permissions.containsKey(method) ){

                boolean pass = authorityProcess.validationByPermissions( permissions.get( method) );
                if( pass == false )
                    throw new NoAuthorityException("没有权限");
                return pass;
            }
        }


        return true;
    }

    private void initAuthorityProcess(){

        try {
            authorityProcess = applicationContext.getBean( AuthorityProcess.class );
        }catch (Exception e){
            LOG.warn("没有找到 authorityProcess {}",e.getMessage());
        }
    }

    protected void initHandleMethods() {

        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =ApplicationContextUtils.findHandlerMethodsMap(applicationContext);

        if( null == handlerMethodMap ) return;

        LOG.info("开始初始化权限拦截器 拦截点!");
        permissions.clear();

        if( !ObjectUtils.isEmpty( handlerMethodMap ) ){
            for( HandlerMethod handlerMethod : handlerMethodMap.values() ){
                Authority authority = handlerMethod.getMethodAnnotation(Authority.class);
                if( authority == null )
                    authority = handlerMethod.getBeanType().getAnnotation(Authority.class);
                if( null != authority && !ObjectUtils.isEmpty(authority.permissions()) ){
                    permissions.put(handlerMethod.getMethod(),authority.permissions());
                }
            }
        }
        LOG.info("初始化权限拦截器 拦截点,完成!!");

    }


    public void onApplicationEvent(ContextRefreshedEvent event) {

        this.applicationContext = event.getApplicationContext();
        initAuthorityProcess();
        initHandleMethods();
    }
}
