package com.yuntu.zookeeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;

/**
 * Created by linxiaohui on 16/1/25.
 */
public class ZookeeperHelp {


    /**
     * 等待程序启动
     * @param curatorClient
     * @throws Exception
     */
    public static void waitForStarted(CuratorFramework curatorClient) throws Exception {

        while( curatorClient.getState() == CuratorFrameworkState.LATENT ){

            if( curatorClient.getState() == CuratorFrameworkState.STOPPED ){
                throw new Error("curatorClient is STOPPED");
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
