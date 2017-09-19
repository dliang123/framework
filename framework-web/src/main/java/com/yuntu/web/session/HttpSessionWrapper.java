package com.yuntu.web.session;

import javax.servlet.ServletContext;
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
public class HttpSessionWrapper implements HttpSession {

    //被包装的Session
    protected HttpSession delegate;

    @SuppressWarnings("unused")
	private HttpSessionWrapper(){

    }



    public HttpSessionWrapper(HttpSession session){
        if (session == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        this.delegate = session;
    }

    public HttpSession getDelegate() {
        return delegate;
    }

    public void setDelegate(HttpSession delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        this.delegate = delegate;
    }


    public boolean isWrapperFor(HttpSession wrapped) {
        if (delegate == wrapped) {
            return true;
        } else if (delegate instanceof HttpSessionWrapper) {
            return ((HttpSessionWrapper) delegate).isWrapperFor(wrapped);
        } else {
            return false;
        }
    }


    public boolean isWrapperFor(Class<?> wrappedType) {


        if (!HttpSession.class.isAssignableFrom(wrappedType)) {
            throw new IllegalArgumentException(
                    "Given class " +wrappedType.getName() + " not a subinterface of " + HttpSession.class.getName() );
        }
        if (wrappedType.isAssignableFrom(delegate.getClass())) {
            return true;
        } else if (delegate instanceof HttpSessionWrapper) {
            return ((HttpSessionWrapper) delegate).isWrapperFor(wrappedType);
        } else {
            return false;
        }
    }

    public long getCreationTime() {
        return getDelegate().getCreationTime();
    }

    public String getId() {
        return getDelegate().getId();
    }

    public long getLastAccessedTime() {
        return getDelegate().getLastAccessedTime();
    }

    public ServletContext getServletContext() {
        return getDelegate().getServletContext();
    }

    public void setMaxInactiveInterval(int interval) {
        getDelegate().setMaxInactiveInterval(interval);
    }

    public int getMaxInactiveInterval() {
        return getDelegate().getMaxInactiveInterval();
    }

    public HttpSessionContext getSessionContext() {
        return getDelegate().getSessionContext();
    }

    public Object getAttribute(String name) {
        return getDelegate().getAttribute(name);
    }

    public Object getValue(String name) {
        return getDelegate().getValue(name);
    }

    @SuppressWarnings("unchecked")
	public Enumeration<String> getAttributeNames() {
        return getDelegate().getAttributeNames();
    }

	public String[] getValueNames() {
        return getDelegate().getValueNames();
    }

    public void setAttribute(String name, Object value) {
        getDelegate().setAttribute(name, value);
    }

    public void putValue(String name, Object value) {
        getDelegate().putValue(name, value);
    }

    public void removeAttribute(String name) {
        getDelegate().removeAttribute(name);
    }

    public void removeValue(String name) {
        getDelegate().removeValue(name);
    }

    public void invalidate() {
        getDelegate().invalidate();
    }

    public boolean isNew() {
        return getDelegate().isNew();
    }

}
