package com.yuntu.jpa.event;

import org.springframework.util.Assert;

import java.io.Serializable;

/***
 * 事件实体
 * @param <T>
 */
public class ActionEvent<T> implements Serializable {

    private T source;
    private OperateType operateType;

    public ActionEvent(OperateType operateType,T source){
        Assert.notNull(source);
        Assert.notNull(operateType);
        this.operateType = operateType;
        this.source = source;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public OperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(OperateType operateType) {
        this.operateType = operateType;
    }
}