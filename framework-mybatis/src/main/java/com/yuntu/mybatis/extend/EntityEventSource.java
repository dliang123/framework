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
public class EntityEventSource {


    public enum triggerType{
        UNKNOWN, INSERT, UPDATE, DELETE, SELECT
    }

    private Object trigger;
    private triggerType triggerType;

    public EntityEventSource(Object trigger, EntityEventSource.triggerType triggerType) {
        this.trigger = trigger;
        this.triggerType = triggerType;
    }

    public Object getTrigger() {
        return trigger;
    }

    public EntityEventSource.triggerType getTriggerType() {
        return triggerType;
    }
}
