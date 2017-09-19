package com.yuntu.redis.config;

import com.yuntu.redis.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis组件相关初始化
 * Created by  on 2014/10/10.
 */
public class Initialization {

    private Logger logger = LoggerFactory.getLogger(Initialization.class);


    public void setRedisTemplate(RedisTemplate<Object,Object> redisTemplate) {
        if( logger.isDebugEnabled() ){
            logger.debug("开始初始化 redis 组件");
        }

        RedisUtils.setRedisTemplate(redisTemplate);

        if( logger.isDebugEnabled() ){
            logger.debug("初始化 redis 组件完成");
        }
    }
}
