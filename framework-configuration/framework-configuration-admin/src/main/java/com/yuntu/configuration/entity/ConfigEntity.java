package com.yuntu.configuration.entity;

import com.google.common.base.Strings;
import com.yuntu.jpa.AuditEntity;

import javax.persistence.*;

/**
 *  16/1/14.
 */
@SuppressWarnings("serial")
@Entity
@IdClass(value = ConfigId.class)
@Table(name = "config")
public class ConfigEntity extends AuditEntity<Long>{

    @Id
    @Column(name = "config_key",nullable = false)
    private String key;

    @Id
    @Column(name = "application_id",nullable = false)
    private Long applicationId;

    @Column(name = "config_value",nullable = false)
    private String value;

    @Column(name = "config_describe")
    private String describe;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if(!Strings.isNullOrEmpty(value) )
            value = value.trim();
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
}
