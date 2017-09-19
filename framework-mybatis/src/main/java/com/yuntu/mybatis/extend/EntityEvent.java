package com.yuntu.mybatis.extend;

/*
 * Copyright (C), 2012-2014
 * Author:   Administrator
 * Date:     14-11-27
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-27           00000001         创建文件
 *
 */
@SuppressWarnings({"serial","unchecked"})
public class EntityEvent<T> extends java.util.EventObject{

    public enum TriggerType{
        UNKNOWN, INSERT, UPDATE, DELETE, SELECT
    }

    private TriggerType triggerType;


    public EntityEvent(T source,TriggerType triggerType) {
        super(source);
        this.triggerType= triggerType;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

	@Override
    public T getSource() {
        return (T)super.getSource();
    }
}
