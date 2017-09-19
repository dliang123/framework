package com.yuntu.mybatis.extend;

/*
 * Copyright (C), 2012-2014
 * Author:
 * Date:     14-11-27
 * Description: mybatis 实体侦听器
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *             14-11-27           00000001         创建文件
 *
 * @param <T>  要侦听的实体类型(不传则侦听所有类型)
 */
public interface EntityListener<T> extends java.util.EventListener{

    public void beforeChange(EntityEvent<T> event);
    public void afterChange(EntityEvent<T> event);

    public int getOrder();
}
