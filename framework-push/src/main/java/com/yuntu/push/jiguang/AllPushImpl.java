package com.yuntu.push.jiguang;

import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;

import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;

/**
 * Created by linxiaohui on 15/11/24.
 */
public class AllPushImpl extends PushImpl {

    private static final AndroidPushImpl android = new AndroidPushImpl();
    private static final IosPushImpl ios = new IosPushImpl();

    @Override
    protected PlatformNotification platformNotification(NotificationEntity notificationEntity) {
        return null;
    }

    @Override
    protected Platform platform() {
        return Platform.all();
    }

    @Override
    protected Notification notification(NotificationEntity notificationEntity) {

        return Notification.newBuilder()
                .addPlatformNotification(android.platformNotification(notificationEntity))
                .addPlatformNotification(ios.platformNotification(notificationEntity)).build();
    }

    @Override
    protected PushPayload pushPayload(NotificationEntity notificationEntity, Audience audience, KeyPair keyPair) {
        return PushPayload.newBuilder().setPlatform(platform()).setAudience(audience)
                .setNotification(notification(notificationEntity))
                .setOptions(Options.newBuilder().setApnsProduction(notificationEntity.isApnsProduction()).build()).build();
    }
}
