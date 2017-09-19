package com.yuntu.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by linxiaohui on 16/1/12.
 */
public abstract class DynamicUpdateWatched {

    private static final Logger logger = LoggerFactory.getLogger(DynamicUpdateWatched.class);

    private final List<DynamicUpdateWatcher> changeConsumerList = new CopyOnWriteArrayList<DynamicUpdateWatcher>();
    private final Object lock = new Object();


    protected abstract Map<String,byte []> initData();

    /***
     * 注册
     * @param consumer
     */
    public final Map<String,byte[]> register(DynamicUpdateWatcher consumer){

        if( null != changeConsumerList && !changeConsumerList.contains(consumer)){
            synchronized ( lock ){
                if( !changeConsumerList.contains(consumer)  ){
                    changeConsumerList.add(consumer);
                }
            }
        }
        return initData();
    }

    /**
     * 注销
     * @param consumer
     */
    public final void remove(DynamicUpdateWatcher consumer){

        if( null != changeConsumerList && changeConsumerList.contains(consumer) ){
            synchronized ( lock ){
                if( changeConsumerList.contains(consumer)  ){
                    changeConsumerList.remove(consumer);
                }
            }
        }
    }

    protected final void notifyWatchers(ChangeEvent configChangeEvent){

        for(DynamicUpdateWatcher consumer : changeConsumerList){
            try {
                SwitchManager.change( configChangeEvent );
            }catch (Exception e){
                logger.warn("",e);
            }
            try {
                consumer.change( configChangeEvent );
            }catch (Exception e){
                logger.warn("",e);
            }
        }
    }

}
