package com.yuntu.configuration;

import com.yuntu.base.Application;

public class ConfigPathUtils {

    public static String configPath(Application application){

        return  "/config/"+ application.getKey()+"/"+application.getCluster() ;
    }
}
