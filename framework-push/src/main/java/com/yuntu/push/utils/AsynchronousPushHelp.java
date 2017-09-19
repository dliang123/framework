package com.yuntu.push.utils;

import com.yuntu.push.Sender;
import com.yuntu.push.domain.PushEntity;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 林晓辉 on 2014/10/22.
 */
public class AsynchronousPushHelp {

    private static AsynchronousPushImp asynchronousPushImp;
    private static Object lock=new Object();

    @PreDestroy
    public void destroy(){
        asynchronousPushImp.destroy();
    }

    public static void asynchronous(PushEntity pushEntity){

        if( null == asynchronousPushImp ){
            synchronized (lock){
                if( null == asynchronousPushImp  ){
                    asynchronousPushImp = new AsynchronousPushImp();
                }
            }
        }
        asynchronousPushImp.asynchronous(pushEntity);
    }


    static class AsynchronousPushImp{
        private ExecutorService executorService;

        private AsynchronousPushImp(){
            executorService = Executors.newFixedThreadPool(50);
        }

        public synchronized void destroy(){ //销毁方法
            executorService.shutdown();
        }

        public void asynchronous(PushEntity pushEntity){
            executorService.execute( new Sender(pushEntity) );
        }
    }
}
