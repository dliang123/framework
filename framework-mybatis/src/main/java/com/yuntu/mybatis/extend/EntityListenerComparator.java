package com.yuntu.mybatis.extend;

import java.util.Comparator;

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
@SuppressWarnings("rawtypes")
public class EntityListenerComparator implements Comparator<EntityListener> {

    public int compare(EntityListener o1, EntityListener o2) {
        if( null == o1 || null == o2){
            return 0;
        }
        return Integer.compare(o1.getOrder(),o2.getOrder());
    }
}
