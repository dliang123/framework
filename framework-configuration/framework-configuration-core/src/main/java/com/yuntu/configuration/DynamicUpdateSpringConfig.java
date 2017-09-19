package com.yuntu.configuration;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jack on 11/16/16.
 *
 * 监控配置中心ZK的变化,更改Spring 的@Value注解的值的变化
 */
//谁用的话配置就行了 //@Component
public class DynamicUpdateSpringConfig implements BeanPostProcessor ,DynamicUpdateWatcher {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Byte [] lock = new Byte[1];

	private static final Map<String,List<TargetWrapper>> methodMap = new HashMap<>();
	private static final Map<String,List<TargetWrapper>>  fieldMap = new HashMap<>();

	private DynamicUpdateWatched watched;
	/**
	 * Apply this BeanPostProcessor to the given new bean instance <i>before</i> any bean
	 * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
	 * or a custom init-method). The bean will already be populated with property values.
	 * The returned bean instance may be a wrapper around the original.
	 *
	 * @param bean     the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws BeansException in case of errors
	 * @see InitializingBean#afterPropertiesSet
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * Apply this BeanPostProcessor to the given new bean instance <i>after</i> any bean
	 * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
	 * or a custom init-method). The bean will already be populated with property values.
	 * The returned bean instance may be a wrapper around the original.
	 * <p>In case of a FactoryBean, this callback will be invoked for both the FactoryBean
	 * instance and the objects created by the FactoryBean (as of Spring 2.0). The
	 * post-processor can decide whether to apply to either the FactoryBean or created
	 * objects or both through corresponding {@code bean instanceof FactoryBean} checks.
	 * <p>This callback will also be invoked after a short-circuiting triggered by a
	 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation} method,
	 * in contrast to all other BeanPostProcessor callbacks.
	 *
	 * @param bean     the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws BeansException in case of errors
	 * @see InitializingBean#afterPropertiesSet
	 * @see FactoryBean
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if( logger.isDebugEnabled() ){
			logger.debug(beanName);
		}
		synchronized ( lock ) {
			for (Field field : bean.getClass().getDeclaredFields()) {
				Value[] _value = field.getDeclaredAnnotationsByType(Value.class);
				if (null == _value) {
					continue;
				}
				for (Value _v : _value) {
					if (!fieldMap.containsKey(_v.value())) {
						fieldMap.put(_v.value(),  new ArrayList());
					}
					fieldMap.get(_v.value()).add(new TargetWrapper(field, bean));
				}
			}

			for (Method method : bean.getClass().getMethods()) {
				Value[] _value = method.getDeclaredAnnotationsByType(Value.class);
				if (null == _value) {
					continue;
				}
				for (Value _v : _value) {
					if (!methodMap.containsKey(_v.value())) {
						methodMap.put(_v.value(), new ArrayList());
					}
					methodMap.get(_v.value()).add(new TargetWrapper(method, bean));
				}
			}
		}

		watched.register(this);
		return bean;
	}

	@Override
	public void change(ChangeEvent event) {
		if (event == null || event.getData() == null) {
			return;
		}

		logger.debug(" config change  key {} type  ",event.getKey(),event.getType());

		List<TargetWrapper> twField = fieldMap.get(event.getKey());
		if (twField==null) {
			twField = fieldMap.get("${"+event.getKey()+"}");
		}
		synchronized ( lock ) {
			if (twField!=null&&!twField.isEmpty()) {
				twField.stream().forEach(t -> {
					try {
						t.setFieldValue(new String(event.getData()));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				});
			}
		}

		List<TargetWrapper> twMethod = methodMap.get(event.getKey());
		if (twField==null) {
			twMethod = methodMap.get("${"+event.getKey()+"}");
		}
		synchronized ( lock ) {
			if (twMethod!=null&&!twMethod.isEmpty()) {
				twMethod.stream().forEach(t -> {
					try {
						t.setMethodValue(new String(event.getData()));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				});
			}
		}

		if( logger.isDebugEnabled() ){
			logger.debug(" config change key {} value {},",event.getKey(),event.getData());
		}
	}

	private static class TargetWrapper implements Serializable{
		private Field field;
		private Method method;
		private Object target;

		private TargetWrapper(Field field,Object target){
			this.field = field;
			this.target = target;
		}

		private TargetWrapper(Method method,Object target){
			this.method = method;
			this.target = target;
		}
		private void setFieldValue(Object value) throws IllegalAccessException {
			if( !Modifier.isPublic(field.getModifiers()) ){
				field.setAccessible(true);
			}
			if( field.getType().isEnum() ){
				value = Enum.valueOf((Class<? extends Enum>) field.getType(), String.valueOf(value));
			}else {
				value = ConvertUtils.convert(value, field.getType());
			}
			field.set(target,value);
		}

		public void setMethodValue(Object value) throws IllegalAccessException{
			// NEED TODO
		}
	}
}
