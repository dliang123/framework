package com.yuntu.push.jiguang;

import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.PlatformNotification;

import com.yuntu.common.utils.StringUtils;
import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;

/**
 * IOS推送实现
 * Created by 林晓辉 on 2014/9/22.
 */
public class IosPushImpl extends PushImpl{

    @Override
    protected PlatformNotification platformNotification(NotificationEntity notificationEntity) {
        return IosNotification.newBuilder()
                .setAlert(StringUtils.isNullOrEmpty(notificationEntity.getDescription())?notificationEntity.getTitle():notificationEntity.getDescription())
                .addExtras(notificationEntity.getParams()).build();
    }

    @Override
    protected Platform platform() {
        return Platform.ios();
    }


    @Override
    protected PushPayload pushPayload(NotificationEntity notificationEntity, Audience audience, KeyPair keyPair) {

        return PushPayload.newBuilder().setPlatform(platform()).setAudience(audience)
                .setMessage(message(notificationEntity,keyPair)).setOptions(Options.newBuilder()
                         .setApnsProduction(notificationEntity.isApnsProduction())
                         .build())
            .build();

    }
}
