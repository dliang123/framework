package com.yuntu.web.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/*
 * Author:
 * Date:     14-11-6
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-6           00000001         创建文件
 *
 */
@SuppressWarnings("deprecation")
public class ShareHttpSessionContext implements HttpSessionContext {

    private HttpSessionContext delegate;

    @SuppressWarnings("unused")
	private ShareHttpSessionContext(){

    }

    public ShareHttpSessionContext(HttpSessionContext delegate){
        this.delegate = delegate;
    }



    public HttpSession getSession(String sessionId) {
//        HttpSession httpSession = delegate.getSession(sessionId);
//        if( httpSession == null )
//            return null;
//        return new ShareSession(delegate.getSession(sessionId));
        throw new RuntimeException("共享session暂不支持此方法");
    }

    @SuppressWarnings("unchecked")
	public Enumeration<String> getIds() {
        return delegate.getIds();
    }
}
