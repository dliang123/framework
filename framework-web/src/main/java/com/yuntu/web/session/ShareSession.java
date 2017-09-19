package com.yuntu.web.session;

import com.google.common.base.Strings;

import com.yuntu.base.store.Store;
import com.yuntu.web.MockStroe;
import com.yuntu.web.util.WebUtils;
import org.slf4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

/*
 * Author:   林晓辉
 * Date:     14-11-6
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-11-6           00000001         创建文件
 *
 */
@SuppressWarnings("deprecation")
public class ShareSession extends HttpSessionWrapper {

    private Store cacheable;
    private String domain;
    private boolean cacheAutoDestroy;

    //提取多少秒给缓存续期(同步缓存与本地session的过期时间)
    private static int ten_seconds_ahead_of_renewal=10;
    //是否共享session
    private boolean share=true;
    //session 存储在缓存服务器的 命名空间
    public static final String domainKey = "entries";
    //缓存实现的key
    public static final String cacheKey="cacheImpl";
    //最后同步时间的Key
    private static final String lastSyncTimeKey="lastSyncTime";
    //最后向缓存服务器写入或删除操作时间的Key
    private static final String lastWriteActionTimeKey = "lastWriteActionTime";
    //最后一次给缓存服务器续约的时间
    private static final String lastRenewalTimeKey="lastRenewalTime";
    //日志
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ShareSession.class);



    public ShareSession(HttpSession delegate){

        this(delegate, (String) delegate.getAttribute(domainKey) );
    }

    public ShareSession(HttpSession delegate,String domain){

        this(delegate, (Store) delegate.getAttribute(cacheKey), domain, true);
    }

    public ShareSession(HttpSession delegate, Store cacheable, String domain,boolean cacheAutoDestroy){

        super(delegate);

        this.domain = null == domain ? this.delegate.getId() : domain;
        this.cacheable = cacheable;
        this.delegate.setAttribute(domainKey, domain);
        this.cacheAutoDestroy = cacheAutoDestroy;
        if( null == this.cacheable ){
            this.cacheable = new MockStroe();
            if(  logger.isWarnEnabled() ){
                logger.warn(" session 共享功能没有找到缓存插件 ");
            }
        }

        //执行同步
        //TODO 可优化为 使用时同步
        synchronization();
        //尝试给共享会话续期
        tryRenewal();

        this.delegate.setAttribute(cacheKey, cacheable);
    }


	@Override
    public HttpSessionContext getSessionContext() {
        return new ShareHttpSessionContext(delegate.getSessionContext());
    }


    @Override
    public void setAttribute(String key, Object value) {

        delegate.setAttribute(key, value);

        if( isShare() && !(value instanceof NoShare)){
            try{
                //写数据到缓存服务器
                cacheable.put(domain, key, value);
                //更新缓存服务器的最后写操作时间
                updateLastWriteActionTime();
            }catch (Exception e){
                if( logger.isWarnEnabled() ){
                    logger.warn(e.getMessage(),e);
                }
            }
        }
    }

    @Override
    public void removeAttribute(String key) {

        delegate.removeAttribute(key);
        if( isShare() ){
            try{
                //从缓存服务器删除数据
                cacheable.delete(domain, key);
                //更新缓存服务器的最后写操作时间
                updateLastWriteActionTime();
            }catch (Exception e){
                if( logger.isWarnEnabled() ){
                    logger.warn(e.getMessage(),e);
                }
            }
        }
    }


    @Override
    public void putValue(String s, Object o) {
        setAttribute(s, o);
    }

    @Override
    public void removeValue(String key) {
        removeAttribute(key);
    }

    @Override
    public Object getValue(String key) {
        return getAttribute(key);
    }

    /**
     * 销毁 SESSION 方法
     * 销毁方法实际调用的 为  delegate.invalidate()
     * 只有手动调用此销毁方法的销毁操作才会同步到缓存服务器
     *     session过期的销毁方法不会同步到缓存服务器( 因为缓存服务器的ttl时间与session的过期时间相同 ),
     *            以此解决关闭浏览器重新打开所产生的无效session过期导致的共享session(缓存服务器session失效问题)
     */
    @Override
    public void invalidate() {

        //session 是否是到期(过期为自然销毁,非过期为手动销毁,例如注销操作等)
        if( isShare() && getExpire() > 0  ){ //剩余过期时间大于0 即表示为手动调用而非session时间到期
            try{
                cacheable.invalidate(domain);
            }catch (Exception e){
                if( logger.isWarnEnabled() ){
                    logger.warn(e.getMessage(),e);
                }
            }
        }
        delegate.invalidate();
    }

    /**
     * 获取 session 还有多少秒过期
     *       如果小于 1 则永不过期
     * **/
    private int getExpire(){
        if( 0 >= delegate.getMaxInactiveInterval() || !cacheAutoDestroy){ //当 delegate.getMaxInactiveInterval() == 0 时session设置为永不过期
            return -1;
        }else{ //剩余过期时间多少秒
            return delegate.getMaxInactiveInterval() - new Long((System.currentTimeMillis() - delegate.getLastAccessedTime())/1000).intValue() ;
        }
    }

    /**
     * 同步操作
     * 注  当多台 WEB应用服务器时间不一致时会出现同步错误
     */
    @SuppressWarnings("unchecked")
	private void synchronization(){

        try{
            //获取缓存服务器最后写数据的时间
            Long lastWriteActionTime = (Long) cacheable.get(domain, lastWriteActionTimeKey);
            if(null != lastWriteActionTime &&  lastWriteActionTime > getLastSyncTime() ){ //缓存服务器最后一次写操作的时间  大于 本地环境最后一次同步时间( 即缓存服务器与本地服务数据不同步 )

                java.util.Map<String,Object> domain = cacheable.entries(this.domain);
                for(java.util.Map.Entry<String,Object> entry : domain.entrySet()){
                    delegate.setAttribute( entry.getKey() ,entry.getValue() );
                }
                updateLastSyncTime();
            }else if(null == lastWriteActionTime && !delegate.isNew()){
                Enumeration<String> names = delegate.getAttributeNames();
                while(names.hasMoreElements()){
                    delegate.removeAttribute(names.nextElement());
                };
                this.domain = delegate.getId();
                this.delegate.setAttribute(domainKey,domain);
            }
        }catch (Exception e){
            if( logger.isWarnEnabled() ){
                logger.warn(e.getMessage(),e);
            }
        }


    }

    public boolean isShare(){
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    /**
     * @return 最后与缓存服务器的时间
     */
    private Long getLastSyncTime() {

        Long lastSyncTime = (Long) delegate.getAttribute(lastSyncTimeKey);
        if( null ==  lastSyncTime )
            return 0l;
        return lastSyncTime;
    }

    /**
     * 更新最后同步时间
     */
    private void updateLastSyncTime() {

        delegate.setAttribute(lastSyncTimeKey,System.currentTimeMillis());
    }

    /**
     *更新缓存服务器的最后写操作时间
     */
    private void updateLastWriteActionTime(){

        try{
            cacheable.put(domain, lastWriteActionTimeKey, System.currentTimeMillis());
        }catch (Exception e){
            if( logger.isWarnEnabled() ){
                logger.warn(e.getMessage(),e);
            }
        }
    }

    /** 更新缓存的过期时间 **/
    public void updateCacheExpire(Long interval,TimeUnit timeUnit){

        //过期时间(秒)
        if( 0 < interval ){ //小于等于0 为不过期
            try{
                cacheable.expire(domain ,interval, timeUnit);
            }catch (Exception e){
                if( logger.isWarnEnabled() ){
                    logger.warn(e.getMessage(),e);
                }
            }
        }

    }

    /**
     * 尝试给缓存的会话续期
     * @return
     */
    private boolean tryRenewal(){

        int expire=getExpire();
        //当过期时间小与 ${ten_seconds_ahead_of_renewal} 秒给session 续期
        if(expire > 0 && ( expire < ten_seconds_ahead_of_renewal || null == delegate.getAttribute(lastRenewalTimeKey) ) ){
            //更新缓存失效的时间 实现续期功能
            updateCacheExpire(Long.valueOf(delegate.getMaxInactiveInterval()), TimeUnit.SECONDS);
            //设置最后缓存续期时间
            delegate.setAttribute(lastRenewalTimeKey,System.currentTimeMillis());
            return true;
        }
        return false;
    }


    public Store getCacheImpl() {
        return cacheable;
    }

     /*
     * 销毁共享共享session
     * @param domainKey
     * @return
     */


    public static boolean destroyShareSessionByDomain(String domain){

        HttpSession session = WebUtils.getSession();
        if( session instanceof ShareSession ){
            ((ShareSession)session).getCacheImpl().invalidate( domain );
            return true;
        }else if(logger.isWarnEnabled()){
            logger.warn("没有启动共享session");
        }
        return false;
    }

    /*
     * 销毁共享共享session
     * @param domainKey
     * @return
     */
    public static boolean destroyShareSession(){

        HttpSession session = WebUtils.getSession();
        String domain = (String)session.getAttribute(domainKey);
        if( session instanceof ShareSession && !Strings.isNullOrEmpty(domain) ){
            ((ShareSession)session).getCacheImpl().invalidate(domain);
            session.invalidate();
            return true;
        }else if(logger.isWarnEnabled()){
            logger.warn("没有启动共享session");
        }
        return false;
    }


    public boolean isCacheAutoDestroy() {
        return cacheAutoDestroy;
    }
}
