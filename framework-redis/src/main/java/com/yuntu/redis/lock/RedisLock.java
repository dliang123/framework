/**
 * <p>Copyright:Copyright(c) 2015</p>
 * <p>Company:上海运图投资有限公司</p>
 * <p>包名:com.yuntu.redis.lock</p>
 * <p>文件名:RedisLock.java</p>
 * <p>类更新历史信息</p>
 * @todo <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a> 创建于 2015年12月22日 上午11:54:37
 */
package com.yuntu.redis.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import com.yuntu.redis.util.RedisUtils;

/** 
 * 基于Redis的分布式锁
 * <p>Company:上海运图投资有限公司</p>
 * @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a> 
 * @date 2015年12月22日 上午11:54:37 
 * @version 1.0.2015
 */
public class RedisLock {
	
    private String key;
    
    private RedisLock(String key){
    	this.key = key;
    }
    
    public static RedisLock create(String key){
    	return new RedisLock(key);
    }

	/**
	* 获取锁，如果锁可用，立即返回true，否则返回false
	* <p>默认过期时间3分钟</p>
	* @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a>
	* @date 2015年12月22日  下午2:15:39
	 */
	public Boolean tryLock(){
		return tryLock(EXPIRE);
	}
	
	/**
	* 获取锁，如果锁可用，立即返回true，否则返回false
	* @param expire 过期时间
	* @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a>
	* @date 2015年12月22日  下午2:15:39
	 */	
	public Boolean tryLock(final long expire){
		return (Boolean) RedisUtils.execute(new RedisCallback<Boolean>() {
			/* (non-Javadoc)
			 * @see org.springframework.data.redis.core.RedisCallback#doInRedis(org.springframework.data.redis.connection.RedisConnection)
			 * @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a>
			 * @date 2015年12月22日 下午2:28:14
			 */
			@SuppressWarnings("unchecked")
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] lockBytes = RedisUtils.getRedisTemplate().getStringSerializer().serialize(key);
				
				boolean locked = connection.setNX(lockBytes, lockBytes);
				
				connection.expire(lockBytes, expire);
				
				return locked;
			}
		},Boolean.FALSE);
	}
	
	/**
	* 释放锁
	* @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a>
	* @date 2015年12月22日  下午2:52:09
	 */
	@SuppressWarnings("unchecked")
	public Boolean unLock(){
		try{
			return (Boolean) RedisUtils.execute(new RedisCallback<Boolean>() {
				/* (non-Javadoc)
				 * @see org.springframework.data.redis.core.RedisCallback#doInRedis(org.springframework.data.redis.connection.RedisConnection)
				 * @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a>
				 * @date 2015年12月22日 下午2:28:14
				 */
				public Boolean doInRedis(RedisConnection connection)
						throws DataAccessException {
					byte[] lockBytes = RedisUtils.getRedisTemplate().getStringSerializer().serialize(key);
					long flag = connection.del(lockBytes);
					if(flag > 0){
						return Boolean.TRUE;
					}else{
						return Boolean.FALSE;
					}
				}
			},Boolean.FALSE);			
		}catch(Exception e){
			logger.error("释放锁异常。组件：redis，事件：del，键：{}",key,e);
			return Boolean.FALSE;
		}
	}
	
	/**过期时间（秒）*/
	private static final int EXPIRE = 3*60;
	
	private Logger logger = LoggerFactory.getLogger(RedisLock.class);
}
