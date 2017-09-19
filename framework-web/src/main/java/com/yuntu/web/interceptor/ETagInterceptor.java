package com.yuntu.web.interceptor;

import com.google.common.collect.Maps;
import com.yuntu.common.utils.ObjectUtils;
import com.yuntu.web.annotation.EnableEtag;
import com.yuntu.web.util.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class ETagInterceptor extends HandlerInterceptorAdapter implements org.springframework.context.ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ETagInterceptor.class);

    protected ApplicationContext applicationContext;

    private Map<Method, String> ETagEnables = Maps.newConcurrentMap();


    protected void initHandleMethods() {

        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = ApplicationContextUtils.findHandlerMethodsMap(applicationContext);

        if (null == handlerMethodMap) return;

        logger.info("开始初始化ETag拦截器 拦截点!");
        ETagEnables.clear();

        if (!ObjectUtils.isEmpty(handlerMethodMap)) {
            for (HandlerMethod handlerMethod : handlerMethodMap.values()) {
                EnableEtag eTagEnable = handlerMethod.getMethodAnnotation(EnableEtag.class);
                if (eTagEnable == null)
                    eTagEnable = handlerMethod.getBeanType().getAnnotation(EnableEtag.class);
                if (null != eTagEnable) {
                    ETagEnables.put(handlerMethod.getMethod(), "");
                }
            }
        }
        logger.info("初始化ETag拦截器 拦截点,完成!!");

    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Method method = findMethod(handler);

        if (method == null || !ETagEnables.containsKey(method)) { //禁用 eag
            if (WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class) == null) {
                logger.warn("ETag 功能无效, response不是一个 {},请检查是否启动 {} ", ContentCachingResponseWrapper.class, ShallowEtagHeaderFilter.class);
            } else {
                ShallowEtagHeaderFilter.disableContentCaching(request);
            }
        }
        return super.preHandle(request, response, handler);
    }

    private Method findMethod(Object handler) {
        if (handler instanceof HandlerMethod) {
            return ((HandlerMethod) handler).getMethod();
        }
        return null;
    }


    public void onApplicationEvent(ContextRefreshedEvent event) {

        this.applicationContext = event.getApplicationContext();
        initHandleMethods();
    }
}