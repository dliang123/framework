package com.yuntu.base.utils;

import com.yuntu.base.config.Config;
import com.yuntu.base.config.ConfigNoFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by linxiaohui on 15/11/16.
 */
public class ConfigUtils  {

    private static List<Config> configs = new CopyOnWriteArrayList<Config>();

    /**
     * 获取配置
     * @param key  配置名
     * @return
     * @throws ConfigNoFoundException
     */
    public static String v(String key) throws ConfigNoFoundException {
        if( null == key || key.trim().isEmpty() )
            throw new IllegalArgumentException("config key must is not null");
        for(Config config : configs){
            String v = config.v(key);
            if( null != v ){
                return v;
            }
        }
        throw new ConfigNoFoundException("config "+key+" noFound");
    }
    
    /**
     * 获取配置
     * @param key  配置名
     * @return
     * @throws ConfigNoFoundException
     */
    public static String v(String key,Boolean ignoreNotFound) throws ConfigNoFoundException {
        if( null == key || key.trim().isEmpty() )
            throw new IllegalArgumentException("config key must is not null");
        for(Config config : configs){
            String v = config.v(key);
            if( null != v ){
                return v;
            }
        }
        
        if(ignoreNotFound){
        	return null;
        }else{
        	throw new ConfigNoFoundException("config "+key+" noFound");
        }
    }    

    /**
     * 获取配置,如果不存在返回默认值
     * @param key              配置 key
     * @param defaultValue     默认值
     * @return
     */
    public static String v(String key,String defaultValue){
        try {
            return v(key);
        }catch (ConfigNoFoundException e){
            return defaultValue;
        }
    }

    /**
     * 获取 number 型的配置
     * @param key           配置的 key
     * @param numberClass   具体 number 的 class
     * @param <T>
     * @return
     */
    public static <T extends Number> T v(String key,Class<T> numberClass){

        for(Config config : configs){
            T v = config.v(key,numberClass);
            if( null != v ){
                return v;
            }
        }
        return null;
    }

    /**
     * 执行表达式
     * @param expression    表达式
     * @return
     */
    public Object execute(String expression) {
        for(Config config : configs){
            try {
                Object v = config.execute(expression);
                if( null != v ){
                    return v;
                }
            }catch (Exception e){

            }
        }
        return null;
    }

    /**
     * 执行表达式
     * @param expression    表达式
     * @param defaultValue  默认值
     * @param <T>
     * @return
     */
    public <T> T execute(String expression, T defaultValue) {
        @SuppressWarnings("unchecked")
		T v = (T)execute(expression);
        if( null == v )
            return defaultValue;
        return v;
    }

    /**
     * 获取配置(number类型的),不存在返回默认值
     * @param key           配置的 key
     * @param defaultValue  默认值
     * @param <T>
     * @return
     */
    public static <T extends Number> T v(String key,T defaultValue){

        for(Config config : configs){
            try {
                @SuppressWarnings("unchecked")
				T v = (T)config.v(key,defaultValue.getClass());
                if( null != v ){
                    return v;
                }
            }catch (Exception e){

            }
        }
        return defaultValue;
    }

    /**
     * 注册方法
     * @param config
     */
    public static synchronized void register(Config config){

        //防止 null 注册 和 重复注册
        if( null != config && !configs.contains(config) ){
            //copy list
            List<Config> _configs = new ArrayList<Config>(configs);
            //增加新配置
            _configs.add(config);
            //排序
            Collections.sort(_configs, new Comparator<Config>() {
                public int compare(Config o1, Config o2) {
                    return Integer.compare(o1.getOrder(),o2.getOrder());
                }
            });
            //引用替换
            configs = new CopyOnWriteArrayList<Config>(_configs);
        }
    }


}