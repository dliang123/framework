package com.yuntu.push.domain;

import java.io.Serializable;

/**
 * Created by 林晓辉 on 2014/9/23.
 */
public class KeyPair implements Serializable{

	private static final long serialVersionUID = 259696362270318613L;
	private String secret;
    private String key;
    private String appPackage;
    private boolean debug;

    public KeyPair(){

    }

    public KeyPair(String secret, String key,boolean debug) {
        this.secret = secret;
        this.key = key;
        this.debug = debug;
    }

    public KeyPair(String secret, String key,boolean debug,String appPackage) {
        this.key = key;
        this.secret = secret;
        this.appPackage = appPackage;
        this.debug = debug;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
