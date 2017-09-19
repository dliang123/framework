package com.yuntu.mybatis.audit;

import java.io.Serializable;
import java.util.Date;

/*
 * Copyright (C), 2012-2014
 * Author:   林晓辉
 * Date:     14-11-27
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-11-27           00000001         创建文件
 * @param <T>  用户标识的类型
 */
public interface AuditEntity<T extends Serializable> extends Serializable {


    public T getCreator();

    public void setCreator(T creator);

    public Date getCreateTime();

    public void setCreateTime(Date createTime);

    public T getModifier();

    public void setModifier(T modifier);

    public Date getModifyTime();

    public void setModifyTime(Date modifyTime) ;
}
