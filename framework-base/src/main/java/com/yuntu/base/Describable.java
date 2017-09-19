package com.yuntu.base;

public interface Describable {

    String getDescription();

    default KeyValue keyValue(){
        return new KeyValue(getDescription(),this);
    }
}