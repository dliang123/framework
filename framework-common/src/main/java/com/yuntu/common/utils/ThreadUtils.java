package com.yuntu.common.utils;

/**
 *  15/11/11.
 */
public class ThreadUtils {


    /**
     * 休眠
     * @param millis 毫秒
     */
    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 休眠
     * @param millis 毫秒
     * @param nanos  纳秒
     */
    public static void sleep(long millis,int nanos){
        try {
            Thread.sleep(millis,nanos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
