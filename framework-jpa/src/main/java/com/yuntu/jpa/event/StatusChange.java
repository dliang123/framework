package com.yuntu.jpa.event;

import java.io.Serializable;

/**
 *  2016/11/7.
 */
public interface StatusChange<T> extends Serializable{

    T currentStatus();

    boolean isStatusChange();
}
