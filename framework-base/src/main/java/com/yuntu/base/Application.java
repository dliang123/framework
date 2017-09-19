package com.yuntu.base;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *  16/1/11.
 */
public class Application {

    private String key;

    private String cluster;
    /** host **/
    private String host;
    /** ip **/
    private String ip;


    public Application (String key) throws UnknownHostException{
        this(key,null);
    }


    public Application (String key, String cluster) throws UnknownHostException {

        if( null == key ){
            throw new IllegalArgumentException("key must not null");
        }
        if( null == cluster || cluster.trim().length()<1 ){
            cluster = "default";
        }

        this.key = key;
        this.cluster = cluster;

        try{
            InetAddress address = InetAddress.getLocalHost();
            ip=address.getHostAddress();
            host=address.getHostName();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
