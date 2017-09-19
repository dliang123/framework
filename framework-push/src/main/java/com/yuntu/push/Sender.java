package com.yuntu.push;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.PushPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.push.domain.PushEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 消费者
 * Created by 林晓辉 on 2014/10/22.
 */
public class Sender implements Runnable {

    private static final Logger logger= LoggerFactory.getLogger(Sender.class);

    private PushEntity pushEntity;

    public Sender(PushEntity pushEntity ){

        this.pushEntity = pushEntity;
    }

    public void run() {

        Object message = pushEntity.getMessage();
        Object client = pushEntity.getClient();
        try {

            Object result = null;
            if( pushEntity.getClient() instanceof JPushClient ){ //极光推送

                result =( (JPushClient) client ).sendPush( (PushPayload) message );
            }
            if(  logger.isDebugEnabled() ){
                logger.debug("client : {} message : {} result {}", JSONUtils.toJSON(client),JSONUtils.toJSON(message), JSONUtils.toJSON(result) );
            }
        } catch (Exception e) {
            try {
                logger.debug("errorMessage : {} client : {} message : {} ",e.getMessage() , JSONUtils.toJSON(client),JSONUtils.toJSON(message) );
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        }

    }
}
