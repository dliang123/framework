package com.yuntu.base.specification;

/**
 * Created by linxiaohui on 2017/4/11.
 */
public class Property {

    private String propertyName;

    private Property(){

    }

    public static Property name(String propertyName){

        if( null == propertyName || propertyName.trim().isEmpty() ){
            throw new IllegalArgumentException();
        }
        Property property = new Property();
        property.propertyName = propertyName.trim();
        return property;
    }

    public String name() {
        return propertyName;
    }
}
