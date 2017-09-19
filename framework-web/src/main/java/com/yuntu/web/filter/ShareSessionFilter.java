package com.yuntu.web.filter;

import com.google.common.base.Strings;


import com.yuntu.base.store.Store;
import com.yuntu.redis.RedisStore;
import com.yuntu.web.request.ShareSessionRequest;
import com.yuntu.web.session.ShareSession;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/*
 * Author:   林晓辉
 * Date:     14-11-6
 * Description: 共享session的过滤器
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-11-6           00000001         创建文件
 *
 */
public class ShareSessionFilter implements Filter{

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ShareSessionFilter.class);

    private static final String configCacheKey ="cacheImplClass";
    private static final String configCacheAutoDestroyKey ="cacheAutoDestroy";
    private static final String configDomainParamKey ="domainKey";

    /***
     是否自动销毁缓存session
     设置  true ,即与容器的session同时销毁 (默认true)
     设置 false,存在缓存服务器的 session 永不过期,且不会与web容器session同时销毁, 需要手动销毁
     方式 1 ShareSession.destroyShareSession 来执行同步销毁,
     方式 2 ShareSession.destroyShareSessionByDomain( session 的域 )
     **/
    private boolean cacheAutoDestroy=true;
    private Store cacheable;
    private String domainParam="key";

    public void init(FilterConfig filterConfig) throws ServletException {

        //获取共享session 是否自动销毁
        this.cacheAutoDestroy =(Boolean) findConfigValue(filterConfig,ShareSessionFilter.configCacheAutoDestroyKey,cacheAutoDestroy);
        //获取前端传过来的session的domain的参数名
        this.domainParam =(String)findConfigValue(filterConfig,ShareSessionFilter.configDomainParamKey,domainParam);
        //获取缓存实现类
        Class<?> cacheClass =(Class<?>)findConfigValue(filterConfig, configCacheKey,RedisStore.class);
        if(  Store.class.isAssignableFrom(cacheClass) ){
            try {
                cacheable = (Store) cacheClass.newInstance();
            }catch (Exception e){
                logger.error("错误的共享session缓存实现类{}",cacheClass,e);
                throw new IllegalArgumentException("错误的共享session缓存实现类",e);
            }
        }else {
            logger.error("错误的共享session缓存实现类{}",cacheClass);
            throw new IllegalArgumentException("错误的共享session缓存实现类");
        }

    }

    private Object findConfigValue(FilterConfig config,String paramKey,Object defaultValue){
        String dpk = config.getInitParameter(paramKey);
        if( !Strings.isNullOrEmpty(dpk) ){
            try{
                Class<?> returnClass = defaultValue instanceof Class ? Class.class : defaultValue.getClass();
                return ConvertUtils.convert(dpk, returnClass);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest r = (HttpServletRequest)request;
        ShareSessionRequest shareSessionRequest = new ShareSessionRequest(r,cacheable,getDomain(r),this.cacheAutoDestroy);
        chain.doFilter( shareSessionRequest,response  );
    }

    private String getDomain(HttpServletRequest request){

        //从参数中获取 domain
        String domain = findParam(request,domainParam,request.getRequestedSessionId());
        if( Strings.isNullOrEmpty(domain) ){ //不存在domain
            //从session 获取domain
            domain = (String) request.getSession().getAttribute(ShareSession.domainKey);
        }

        //真正的SessionID
        String sessionId = request.getSession().getId();
        if(domain == null || ( !Objects.equals(domain, sessionId) && !cacheable.hasKey(domain) ) ){
            //第一次访问或  ( 会话超时或另外一台负载服务器访问此服务器 并且 已存在共享 session 主键key为 request.getRequestedSessionId  )
            domain = sessionId;
        }
        return domain;
    }

    private String findParam(HttpServletRequest request,String name,String defaultValue){
        String returnValue = request.getParameter(name);
        if(  Strings.isNullOrEmpty(returnValue) )
            returnValue = request.getHeader(name);
        if(  Strings.isNullOrEmpty(returnValue) ){
            Cookie cookie=WebUtils.getCookie(request,name);
            if( null != cookie ){
                returnValue = cookie.getValue();
            }
        }
        if( Strings.isNullOrEmpty(returnValue) ){
            return defaultValue;
        }
        return returnValue;


    }

    public void destroy() {

    }

}
