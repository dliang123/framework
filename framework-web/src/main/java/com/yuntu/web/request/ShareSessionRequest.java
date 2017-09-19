package com.yuntu.web.request;


import com.yuntu.base.store.Store;
import com.yuntu.web.session.ShareSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/*
 * Author:   林晓辉
 * Date:     14-11-6
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-11-6           00000001         创建文件
 *
 */
public class ShareSessionRequest extends HttpServletRequestWrapper {

    //共享session
    private ShareSession session;
    //是否自动销毁

    public ShareSessionRequest(HttpServletRequest request,Store cacheable,String domain,boolean cacheAutoDestroy) {

        super(request);
        session = new ShareSession( request.getSession(),cacheable, domain ,cacheAutoDestroy);

    }

    public HttpSession getSession() {
        return session;
    }

    public HttpSession getSession(boolean create) {
        return session;
    }
}
