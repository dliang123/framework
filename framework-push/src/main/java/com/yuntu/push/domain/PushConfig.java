package com.yuntu.push.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.push.constant.Provider;

/*
 * Copyright (C), 2012-2014, 上海好屋网信息技术有限公司
 * Author:   林晓辉
 * Date:     14-12-18
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-12-18           00000001         创建文件
 *
 */
public class PushConfig implements Serializable{

	private static final long serialVersionUID = 6775164319423545941L;
	private Provider provider;
    private KeyPair keyPair;

    private PushConfig(){

    }

    private PushConfig(Provider provider, KeyPair keyPair){
        this.provider=provider;
        this.keyPair=keyPair;
    }

    private PushConfig(Provider provider, String secret, String key, boolean debug){
        this.provider=provider;
        this.keyPair=new KeyPair(secret,key,debug);
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }


    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public static PushConfig newInstance(Provider provider,KeyPair keyPair){

        if( null == provider ){
            throw new IllegalArgumentException("参数provider必须存在");
        }

        if( null == keyPair ){
            throw new IllegalArgumentException("参数keyPair必须存在");
        }

        if( null == keyPair.getSecret() || keyPair.getSecret().trim().length()<1 ){
            throw new IllegalArgumentException("参数secret必须存在");
        }

        if( null == keyPair.getKey() || keyPair.getKey().trim().length()<1 ){
            throw new IllegalArgumentException("参数key必须存在");
        }
        return new PushConfig(provider,keyPair);
    }

    public static PushConfig newInstance(Provider provider,String secret,String key,boolean debug){

        return newInstance(provider,new KeyPair(secret,key,debug));
    }
    
    public String pushConfigJson(){
        Map<String,Object> json=new HashMap<String, Object>();
        json.put("provider",this.provider);
        json.put("key",this.keyPair.getKey());
        json.put("secret",this.keyPair.getSecret());
        try {
            return JSONUtils.toJSON(json);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
