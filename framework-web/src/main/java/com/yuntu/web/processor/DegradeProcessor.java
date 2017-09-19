package com.yuntu.web.processor;

import com.google.common.collect.Maps;
import com.yuntu.web.annotation.Degrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 *  15/11/16.
 */
public class DegradeProcessor {


    private static final Logger LOG = LoggerFactory.getLogger(DegradeProcessor.class);
    private Map<Method, Class<? extends Exception>> degradeFor = Maps.newConcurrentMap();
    private Map<Method, Object> lastValue = Maps.newConcurrentMap();


    public Object degradeForException(Exception exception,Method method){

        Class<? extends Exception > e = degradeFor.get(method);
        if( null == e || !e.isInstance( exception ) )
            return null;
        Object value =  lastValue.get(method);
        if( LOG.isDebugEnabled() ){
            LOG.debug("方法 {} 出现异常 {} 使用降级结果 {}",method,exception,value);
        }
        return value;
    }



    public void methodValue(Method method, Object value) {

        if( null == value )
            return;
        lastValue.put(method,value);
        if( LOG.isDebugEnabled() ){
            LOG.debug("方法 {} 更新降级结果 {}", method, value);
        }
    }

    public void init(ApplicationContext context,Collection<HandlerMethod> handlerMethods) {

        try {
            if( !ObjectUtils.isEmpty( handlerMethods ) ){
                for(HandlerMethod handlerMethod : handlerMethods  ){
                    Degrade degrade = handlerMethod.getMethodAnnotation( Degrade.class );
                    if( null == degrade )
                        degrade = handlerMethod.getBeanType().getAnnotation( Degrade.class );
                    if( null == degrade || handlerMethod.isVoid() ){
                        continue;
                    }
                    degradeFor.put(handlerMethod.getMethod(), degrade.degradeFor() );
                    if( LOG.isDebugEnabled() ){
                        LOG.debug("方法 {} 降级方案 {}",handlerMethod.getMethod(),degrade);
                    }
                }
            }
        }catch (Exception e){
            LOG.warn("初始化降级处理器失败!",e);
        }
    }
}
