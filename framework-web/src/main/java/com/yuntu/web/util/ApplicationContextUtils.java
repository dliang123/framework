package com.yuntu.web.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.Map;

/**
 *  15/11/13.
 */
public class ApplicationContextUtils {

    public static Collection<HandlerMethod> findHandlerMethods(ApplicationContext context){

        Map<RequestMappingInfo, HandlerMethod>  map = findHandlerMethodsMap(context);
        if( map != null )
            return map.values();
        return null;
    }

    public static Map<RequestMappingInfo, HandlerMethod> findHandlerMethodsMap(ApplicationContext context){

        //判断是否是 webApplication 作用域 (spring mvc )
        if( context instanceof WebApplicationContext) {

            //判断是否是 webApplication 的子作用域( dispatcher )
            if (context.getParent() instanceof WebApplicationContext) {


                //获取映射处理器
                Map<String, HandlerMapping> handlerMappingMap = context.getBeansOfType(HandlerMapping.class);

                // 常见映射处理器
                // 1 RequestMappingHandlerMapping
                // 2 BeanNameUrlHandlerMapping
                // 3 SimpleUrlHandlerMapping
                // 4 DefaultAnnotationHandlerMapping{不建议使用,被 RequestMappingHandlerMapping 替代} )
                //遍历
                for (Map.Entry<String, HandlerMapping> entry : handlerMappingMap.entrySet()) {

                    if (entry.getValue() instanceof RequestMappingHandlerMapping) {

                        RequestMappingHandlerMapping handlerMapping = (RequestMappingHandlerMapping) entry.getValue();

                        return handlerMapping.getHandlerMethods();


                    }
                }
            }
        }
        return null;
    }
}
