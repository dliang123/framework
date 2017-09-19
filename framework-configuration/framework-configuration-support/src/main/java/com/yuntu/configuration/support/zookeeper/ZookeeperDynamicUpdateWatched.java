package com.yuntu.configuration.support.zookeeper;

import com.google.common.collect.Maps;
import com.yuntu.base.Application;
import com.yuntu.configuration.ChangeEvent;
import com.yuntu.configuration.ConfigPathUtils;
import com.yuntu.configuration.DynamicUpdateWatched;
import com.yuntu.zookeeper.ZookeeperHelp;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by linxiaohui on 16/1/12.
 */
public class ZookeeperDynamicUpdateWatched extends DynamicUpdateWatched{

    /** 应用信息 **/
    private Application application;
    /** zookeeper Client **/
    private CuratorFramework curatorClient;
    /** pathChildrenCache **/
    private PathChildrenCache pathChildrenCache;
    /** 数据 **/
    private Map<String,byte[]> dataMap = Maps.newConcurrentMap();

    private Object lock = new Object();

    /** 数据是否准备完成 **/
    private boolean isDataReady = false;


    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public CuratorFramework getCuratorClient() {
        return curatorClient;
    }

    public void setCuratorClient(CuratorFramework curatorClient) {
        this.curatorClient = curatorClient;
    }


    @PostConstruct
    public void init(){
        try {

            Assert.notNull(application);
            Assert.notNull(curatorClient);

            String path = ConfigPathUtils.configPath(application);

            waitForStarted();

            initData(path);

            initListener(path);

        } catch (Exception e) {
            throw new Error("dynamicUpdateConfigurer init error",e);
        }
    }

    @PreDestroy
    public void destroy(){
        if( null != pathChildrenCache ){
            try {
                pathChildrenCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 等待程序启动
     * @throws Exception
     */
    private void waitForStarted() throws Exception {

        ZookeeperHelp.waitForStarted(curatorClient);
    }

    /**
     * 初始化默认数据
     * @param rootPath
     * @throws Exception
     */
    private void initData(String rootPath) throws Exception {

        if( !isDataReady ){
            List<String> children = curatorClient.getChildren().forPath(rootPath);
            for(String key : children){
                byte [] data = curatorClient.getData().forPath(rootPath+"/"+key);
                if( null != data ){
                    dataMap.put(key,data);
                }
            }
        }
        isDataReady = true;

    }

    protected void initListener(String rootPath) throws Exception {

        pathChildrenCache = new PathChildrenCache(curatorClient,rootPath,true);

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @SuppressWarnings("incomplete-switch")
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event == null || event.getData() == null) {
                    return;
                }

                synchronized ( lock ){ //加锁 防止调用 initData 时并发数据变更导致 获取的 dataMap 数据不正确
                    String key = event.getData().getPath().replace(rootPath+"/","");
                    switch ( event.getType() ){
                        case CHILD_ADDED:{
                            dataMap.put(key,event.getData().getData());
                            notifyWatchers(ChangeEvent.newInstance(key,event.getData().getData(), ChangeEvent.EventType.ADDED));
                        }break;

                        case CHILD_REMOVED:{
                            dataMap.put(key,event.getData().getData());
                            notifyWatchers(ChangeEvent.newInstance(key,event.getData().getData(), ChangeEvent.EventType.REMOVED));
                        }break;

                        case CHILD_UPDATED:{
                            dataMap.remove(key);
                            notifyWatchers(ChangeEvent.newInstance(key,event.getData().getData(), ChangeEvent.EventType.UPDATED));
                        }break;
                    }
                }

            }
        });

        pathChildrenCache.start();
    }


    /**
     * 获取初始化数据
     * @return
     */
    @Override
    protected Map<String, byte[]> initData() {

        if( !isDataReady ){
            init();
        }
        synchronized (lock){
            return  new ConcurrentHashMap<String, byte[]>(dataMap);
        }
    }
}
