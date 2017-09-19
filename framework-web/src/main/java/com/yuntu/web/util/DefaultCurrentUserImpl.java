package com.yuntu.web.util;



import com.yuntu.base.user.CurrentUserHelp;
import com.yuntu.base.user.ICurrentUser;
import com.yuntu.web.exception.NoLoginException;

import java.io.Serializable;

/*
 * Author:   林晓辉
 * Date:     14-11-28
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-11-28           00000001         创建文件
 *
 */
public class DefaultCurrentUserImpl implements ICurrentUser<Long,Serializable> {

    public static String idKey="USER_ID";

    public DefaultCurrentUserImpl(){
        CurrentUserHelp.setDelegate(this);
    }

    public Long getCurrentUserID() {
        Long uid = (Long) WebUtils.getSessionAttribute(idKey);
        if( null == uid ){
            throw new NoLoginException();
        }
        return uid;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        DefaultCurrentUserImpl.idKey = idKey;
    }

    public Long currentUserID() {
        return null;
    }

    public Serializable currentUser() {
        return null;
    }
}
