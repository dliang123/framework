package com.yuntu.jpa.event;

import com.yuntu.common.utils.GenericsUtils;
import com.yuntu.common.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 事件管理器
 */
@Component
public class ActionEventManager implements ApplicationListener {

    private static final Logger LOG = LoggerFactory.getLogger(ActionEventManager.class);

    private static Map<Class,List<ActionListener>> container = null;
    private static final byte[] LOCK = new byte[]{};
    private static final ExecutorService SERVICE = Executors.newCachedThreadPool();
    private static final AtomicLong UNEXECUTED = new AtomicLong();
    private static volatile boolean running = true;

    /**
     * 注册
     * @param T
     * @param listener
     * @param <T>
     */
    public static <T> void register(Class<T> T,  ActionListener listener){
        List<ActionListener> listenerList = container.get(T);
        if(null == listenerList || listenerList.isEmpty()){
            listenerList = new CopyOnWriteArrayList();
            container.put(T, listenerList);

        }
        listenerList.add(listener);
    }

    /**
     * 初始化
     */
    public static void init(){
        container = new ConcurrentHashMap();
        Map<String,ActionListener> map = SpringContextUtils.getBeansOfType(ActionListener.class);
        for(ActionListener listener : map.values()){
            Class cls = getSuperclassTypeArguments(listener.getClass(),0);

            if(null == cls || cls == Object.class){
                cls = GenericsUtils.getInterfaceActualClass(listener.getClass(), ActionListener.class);
            }

            if( null == cls )
                throw new Error("错误");
            register(cls, listener);
        }
        LOG.info("实体侦听注册成功!",container);
    }

    private static Class getSuperclassTypeArguments(Class cls,int index){
        Type[] types = GenericsUtils.getSuperclassTypeArguments(cls);
        if( null != types && types.length >  index )
            return (Class) types[index];
        return null;
    }


    public static synchronized void notice(ActionEvent event){

        if( !running ){
            throw new IllegalStateException("ActionEventManager stop");
        }

        if( null == event || null == event.getSource() ) {
            return;
        }

        if( null == container){
            synchronized ( LOCK ){
                if( null == container){
                    init();
                }
            }
        }

        SERVICE.execute( () -> execute(event) );
        UNEXECUTED.addAndGet(1);
    }

    private static void execute(ActionEvent event){
        try {
            boolean hasListener = false;
            for(Map.Entry<Class, List<ActionListener>> entry : container.entrySet() ){
                if(  entry.getKey().isInstance( event.getSource() ) ){
                    for (ActionListener listener : entry.getValue()) {
                        hasListener = true;
                        LOG.info("{} match listener {}",entry.getKey(),listener);
                        try {
                            if( listener.isHandle(event) ){
                                listener.handle(event);
                            }
                        } catch (Exception e) {
                            LOG.warn("执行通知失败 {}", e ,listener);
                        }
                    }
                }else {
                    LOG.info("{} 不是 {} 实例 ",entry.getKey(),event.getSource());
                }
            }
            if( !hasListener ){
                LOG.info("{} 没有找到侦听器 ",event.getSource());
            }

        }catch (Exception e){
            LOG.warn("执行任务出现异常", e);
        }finally {
            long count = UNEXECUTED.addAndGet(-1);
            if( !running && count == 0 ){
                synchronized ( ActionEventManager.class ){
                    ActionEventManager.class.notify();
                }
            }
        }
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if( event instanceof ContextClosedEvent){

            synchronized( ActionEventManager.class ){
                running = false;
                if ( UNEXECUTED.get() > 0 ){
                    try {
                        ActionEventManager.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                SERVICE.shutdown();
            }

        }

    }
}