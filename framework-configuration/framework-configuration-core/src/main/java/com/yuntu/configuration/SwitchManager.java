package com.yuntu.configuration;

import com.yuntu.base.utils.ConfigUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by linxiaohui on 16/1/13.
 */
public class SwitchManager {


    private static final Logger logger = LoggerFactory.getLogger(SwitchManager.class);

    public static Map<String,TargetWrapper> targetWrapperCache = new ConcurrentHashMap<String, TargetWrapper>();

    public static void change(ChangeEvent event) {

        if( null != event ){
            TargetWrapper targetWrapper = targetWrapperCache.get(event.getKey());
            if( null != targetWrapper ){
                try {
                    targetWrapper.setValue(new String(event.getData()));
                } catch (Exception e) {
                    logger.info("",e);
                }
            }
        }
    }

    public static void register(Object object) {
        if( null != object ){

            Class<?> cls = object instanceof Class ? (Class<?>) object : object.getClass();

            for(Field field : cls.getDeclaredFields()){
                Switch _switch=field.getAnnotation(Switch.class);

                if( null != _switch ){

                    if( Modifier.isFinal( field.getModifiers() ) ){
                        logger.warn("{} is final ,不能动态更新!",field);
                    }else {
                        TargetWrapper targetWrapper = new TargetWrapper(field,object);
                        try {
                            targetWrapper.setValue(ConfigUtils.v(_switch.key()));
                        }catch (Exception e){}
                        targetWrapperCache.put(_switch.key(),targetWrapper);
                    }
                }
            }
        }
    }


    private static class TargetWrapper{
        private Field field;
        private Object target;

        private TargetWrapper(Field field,Object target){
            this.field = field;
            this.target = target;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
		private void setValue(Object value) throws IllegalAccessException {

            if( !Modifier.isPublic( field.getModifiers() ) ){
                field.setAccessible(true);
            }

            if( field.getType().isEnum() ){
                value = Enum.valueOf((Class<? extends Enum>) field.getType(), String.valueOf(value));
            }else {
                value = ConvertUtils.convert(value,field.getType());
            }
            field.set(target,value);
        }
    }

}
