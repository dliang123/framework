package com.yuntu.configuration;

import org.springframework.util.Assert;

/**
 *  16/1/12.
 */
public class ChangeEvent {

    public enum EventType {
        ADDED,
        REMOVED,
        UPDATED
    }

    private String key;
    private byte [] data;
    private EventType type;

    private ChangeEvent(String key , byte [] data,EventType type){

        Assert.notNull(key);
        Assert.notNull(type);
        this.key = key;
        this.data = data;
        this.type = type;
    }

    public String getKey() {
        return key;
    }


    public byte[] getData() {
        return data;
    }

    public EventType getType() {
        return type;
    }


    public static ChangeEvent newInstance(String key ,byte [] data,EventType type){
        return new ChangeEvent(key, data, type);
    }
}
