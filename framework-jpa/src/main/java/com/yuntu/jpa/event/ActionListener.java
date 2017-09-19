package com.yuntu.jpa.event;

/***
 * 事件侦听
 * @param <T>
 */
public interface ActionListener<T> {

    void handle(ActionEvent<T> event);

    boolean isHandle(ActionEvent<T> event);
}