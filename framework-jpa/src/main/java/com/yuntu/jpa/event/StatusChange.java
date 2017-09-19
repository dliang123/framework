package com.yuntu.jpa.event;

import java.io.Serializable;

/**
 * Created by linxiaohui on 2016/11/7.
 */
public interface StatusChange<T> extends Serializable{

    T currentStatus();

    boolean isStatusChange();
}
