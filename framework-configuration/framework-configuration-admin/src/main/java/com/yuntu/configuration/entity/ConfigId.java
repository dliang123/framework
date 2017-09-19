package com.yuntu.configuration.entity;

import java.io.Serializable;

/**
 *  16/1/26.
 */
@SuppressWarnings("serial")
public class ConfigId implements Serializable {

    private String key;

    private Long applicationId;


    public ConfigId(){

    }

    public ConfigId(Long applicationId,String key){
        this.applicationId = applicationId;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
}
