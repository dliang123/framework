package com.yuntu.mybatis.hilo;

import com.google.common.collect.Maps;
import com.yuntu.common.utils.SpringContextUtils;
import com.yuntu.mybatis.extend.EntityEvent;
import com.yuntu.mybatis.extend.EntityListener;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 高地位算法 生成 id 侦听
 * Created by 林晓辉 on 2015/3/5.
 */
@SuppressWarnings("rawtypes")
public class HiLoListener implements EntityListener {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HiLoListener.class);
    private int order;


    private java.util.concurrent.ConcurrentMap<Class,HiLo> classCache;
    private java.util.Map<String,SequenceHiLoGenerator> generators;

    public HiLoListener(){


        if( !SpringContextUtils.isAvailable() && logger.isWarnEnabled()){
            throw new Error("spring bean 工具类没有启动! 高地位插件不能使用!");
        }
        if( logger.isInfoEnabled() ){
            logger.info("高地位 id 插件启动!");
        }

        classCache = Maps.newConcurrentMap();
        generators = SpringContextUtils.getBeansOfType(SequenceHiLoGenerator.class);
    }

    public void beforeChange(EntityEvent event) {

        try{
            //获取源对象
            Object obj=event.getSource();
            if(null == obj || EntityEvent.TriggerType.INSERT != event.getTriggerType()){ //不是插入操作 或 要插入的对象不存在 跳出方法
                return;
            }


            if( !classCache.containsKey(obj.getClass()) ){
                HiLo hilo= AnnotationUtils.findAnnotation(obj.getClass(), HiLo.class);

                if( !generators.containsKey(hilo.sequenceName()) ){
                    throw new Error("错误的高低位配置");
                }

                if( null != hilo ){ //缓存实体的 hilo 配置
                    classCache.put(obj.getClass(), hilo);
                }
            }

            if( classCache.containsKey(obj.getClass()) ){
                HiLo hilo= classCache.get(obj.getClass());

                if(  hilo.rewrite() || null == PropertyUtils.getProperty(obj,hilo.id()) ){ //设置重写ID 或 没有id
                    PropertyUtils.setProperty(obj,hilo.id(), generators.get( hilo.sequenceName()).next());
                }
            }
        }catch (Exception e){
            if (logger.isWarnEnabled()){
                logger.warn(e.getMessage(),e);
            }
        }

    }


    public void afterChange(EntityEvent event) {

    }

    public static Logger getLogger() {
        return logger;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }




}