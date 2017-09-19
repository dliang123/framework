package com.yuntu.push.jiguang;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.push.Pushable;
import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;
import com.yuntu.push.domain.PushEntity;
import com.yuntu.push.utils.AsynchronousPushHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**极光推送实现类
 * Created by 林晓辉 on 14-5-21.
 */
abstract class PushImpl implements Pushable {

    private static final Logger logger= LoggerFactory.getLogger(PushImpl.class);
    private static Integer tag_max_length=20;
    private static Integer phone_max_length=1000;


    public void pushBroadcastNotification(NotificationEntity notificationEntity,KeyPair keyPair) throws Exception {

        send(notificationEntity,Audience.all(),keyPair);
    }

    public void pushPhoneNotification(NotificationEntity notificationEntity, String phone, KeyPair keyPair) throws Exception {

        send(notificationEntity, Audience.alias(phone), keyPair);
    }

    public void pushPhoneNotification(NotificationEntity notificationEntity, String[] phones, KeyPair keyPair) throws Exception {

        pushPhoneNotification(notificationEntity, Arrays.asList(phones), keyPair);
    }

    public void pushPhoneNotification(NotificationEntity notificationEntity, List<String> phones, KeyPair keyPair) throws Exception {

        for(int i = 0;i < Math.ceil(phones.size() / new Float(phone_max_length)) ;i++){

            List<String> _phones =  phones.subList(i*phone_max_length,Math.min(phones.size(),(i+1)*phone_max_length));
            send(notificationEntity, Audience.alias( _phones ), keyPair);
        }
    }

    public void pushTagNotification(NotificationEntity notificationEntity, String tag, KeyPair keyPair) throws Exception {

        send(notificationEntity, Audience.tag(tag), keyPair);
    }

    public void pushTagNotification(NotificationEntity notificationEntity, String [] tags, KeyPair keyPair) throws Exception {

        pushTagNotification(notificationEntity, Arrays.asList(tags), keyPair);
    }

    public void pushTagNotification(NotificationEntity notificationEntity, List<String> tags, KeyPair keyPair) throws Exception {

        for(int i = 0;i < Math.ceil(tags.size() / new Float(tag_max_length)) ;i++){

            List<String> _tags =  tags.subList(i*tag_max_length,Math.min(tags.size(),(i+1)*tag_max_length));
            send(notificationEntity, Audience.tag( _tags ), keyPair);
        }
    }

    /**
     * 增加按照map格式标签推送的方法，部分推送平台采用这种格式
     * Add by Ice Wang on 2016-10-14
     */
    public void pushMapNotification(NotificationEntity notificationEntity,
    		Map<String, String[]> map, KeyPair keyPair) throws Exception {
    	List<String> tags = Lists.newArrayList();
    	for(Entry<String, String[]> entry : map.entrySet()) {
    		tags.addAll(Lists.newArrayList(entry.getValue()));
    	}
    	pushTagNotification(notificationEntity, tags, keyPair);
    }

    private final JPushClient findKeyByApp(KeyPair keyPair) {
        return new JPushClient(keyPair.getSecret(), keyPair.getKey(),3);
    }

    private void send(NotificationEntity notificationEntity, Audience audience, KeyPair keyPair) throws APIConnectionException, APIRequestException {

        PushPayload payload = pushPayload(notificationEntity,audience,keyPair);
        //设置离线发送时间
        payload.resetOptionsTimeToLive( notificationEntity.getTimeToLive() );
        JPushClient jPushClient = findKeyByApp(keyPair);
        if( notificationEntity.isAsynchronous() ){ //异步发送
            AsynchronousPushHelp.asynchronous(new PushEntity(jPushClient, payload));
        }else {
            PushResult result =jPushClient.sendPush(payload);
            if( logger.isInfoEnabled() ){
                try {
                    logger.info("极光推送 消息:{} 响应:{}", JSONUtils.toJSON(payload),JSONUtils.toJSON(result));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected Notification notification(NotificationEntity notificationEntity){
        return Notification.newBuilder().addPlatformNotification( platformNotification(notificationEntity) ).build();
    }

    protected Message message(NotificationEntity notificationEntity,KeyPair keyPair){

        String mesContent = Strings.isNullOrEmpty(notificationEntity.getDescription())
                ? notificationEntity.getTitle() : notificationEntity.getDescription();
        return Message.newBuilder()
                .setMsgContent(mesContent)
                .setTitle(notificationEntity.getTitle())
                .addExtras(notificationEntity.getParams())
                .addExtra("debug", keyPair.isDebug())
                .build();
    }

    protected abstract PlatformNotification platformNotification (NotificationEntity notificationEntity);


    protected abstract Platform platform();


    protected abstract PushPayload pushPayload(NotificationEntity notificationEntity,Audience audience,KeyPair keyPair);
}
