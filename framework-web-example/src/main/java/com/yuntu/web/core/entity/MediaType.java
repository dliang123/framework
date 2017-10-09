package com.yuntu.web.core.entity;

/**
 * 图文类型
 * Created by jackdeng on 2017/8/3.
 */
public enum MediaType {

    text("纯文"),
    image("纯图"),
    rich("图文");

    private final String name ;
    MediaType(String _name){
        this.name=_name;
    }

    public String getName(){
        return this.name;
    }
}
