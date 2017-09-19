package com.yuntu.web.example.controller;

import com.yuntu.base.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by linxiaohui on 16/1/11.
 */
public class B {

    private String aaa;

    @Value("${a1}")
    private String bbb;


    public String getAaa() {
        return aaa;
    }

    public void setAaa(String aaa) {
        System.err.println("----------------------------   "+bbb);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                        System.err.println("isPush   "+ConfigUtils.v("isPush","yes"));
                        System.err.println("AAAA   "+Config.AAAA);

                    }catch (Exception e){

                    }
                }
            }
            }
        ).start();
        this.aaa = aaa;
    }

}
