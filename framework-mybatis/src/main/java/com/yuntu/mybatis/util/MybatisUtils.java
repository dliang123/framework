package com.yuntu.mybatis.util;


import com.yuntu.common.utils.ReflectUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

/*
 * Copyright (C), 2012-2014
 * Author:   林晓辉
 * Date:     14-10-23
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-10-23           00000001         创建文件
 *
 */
public class MybatisUtils {

    public static DefaultParameterHandler copy(DefaultParameterHandler handler){

        MappedStatement mappedStatement = (MappedStatement) ReflectUtils.getFieldValue(handler, "mappedStatement");
        Object parameterObject = ReflectUtils.getFieldValue(handler, "parameterObject");
        BoundSql boundSql = (BoundSql)ReflectUtils.getFieldValue(handler, "boundSql");
        return new DefaultParameterHandler(mappedStatement,parameterObject,boundSql);
    }

    public static void replaceSql(BoundSql boundSql,String sql ){

        ReflectUtils.setFieldValue(boundSql,"sql",sql);
    }
}

