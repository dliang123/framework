package com.yuntu.configuration;

import com.yuntu.base.config.Config;
import com.yuntu.base.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by linxiaohui on 16/1/8.
 */
@Lazy(false)
@Component(SpringConfig.NAME)
@SuppressWarnings("unchecked")
public class SpringConfig implements Config, BeanFactoryPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpringConfig.class);
    public static final String NAME = "com.yuntu.configuration.SpringConfig";
    /** 默认排序 **/
    private int order = Integer.MAX_VALUE;

    private ConfigurableListableBeanFactory beanFactory;


    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        this.beanFactory = beanFactory;
        ConfigUtils.register(this);
    }

    public String v(String key) {

        String v = beanFactory.resolveEmbeddedValue(key);
        if(  key.equals(v) ){
            try {
                String keyExpression = "${"+key+"}";
                v = beanFactory.resolveEmbeddedValue(keyExpression);
                
                if(Objects.equals(keyExpression,v)){
                	return null;
                }
            }catch (Exception e){
                return null;
            }
        }
        return v;
    }

    public <T extends Number> T v(String key,Class<T> numberClass){

        String v = null;
        try {
            v = v(key);
            if( !StringUtils.isEmpty(v) && !v.equals(v) ){
                return beanFactory.getTypeConverter().convertIfNecessary(v,numberClass);
            }
        }catch (Exception e){
            logger.debug("config key {} value {} to {} 出错 {}",key,v,numberClass,""+e.getMessage());
        }
        return null;

    }

    public Object execute(String expression) {

        return beanFactory.getBeanExpressionResolver().evaluate(expression,new BeanExpressionContext(beanFactory,null));
    }

	public <T> T execute(String expression, T defaultValue) {
        try {
            return (T)execute(expression);
        }catch (Exception e){
            logger.warn("查询配置 {} 出错!",expression,e);
        }
        return null;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
