package com.yuntu.push.jiguang;

import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.PlatformNotification;

import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;


/**
 * 极光安卓推送实现
 * Created by 林晓辉 on 2014/9/22.
 */
public class AndroidPushImpl extends PushImpl{


    @Override
    protected PlatformNotification platformNotification(NotificationEntity notificationEntity) {
        return AndroidNotification.newBuilder()
                .setAlert(notificationEntity.getDescription())
                .setTitle(notificationEntity.getTitle())
                .addExtras(notificationEntity.getParams()).build();
    }

    @Override
    protected Platform platform() {
        return Platform.android();
    }


    @Override
    protected PushPayload pushPayload(NotificationEntity notificationEntity,Audience audience, KeyPair keyPair) {

        PushPayload payload = PushPayload.newBuilder().setPlatform(platform()).setAudience(audience)
                .setMessage(message( notificationEntity ,keyPair))
//                .setNotification( notification( notificationEntity ))
                .build();
        return payload;
    }
}
