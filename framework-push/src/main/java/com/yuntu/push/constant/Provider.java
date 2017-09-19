package com.yuntu.push.constant;

import com.yuntu.push.domain.PushHandles;

/**
 * Created by 林晓辉 on 2014/9/22.
 */
public enum Provider  {

	/**
	 * 华为推送
	 * add by Ice Wang on 2016-10-14
	 */
	huawei(PushHandles.newBuilder()
			.register(DeviceType.android, com.yuntu.push.huawei.HuaweiPushImpl.class)
			.register(DeviceType.all, com.yuntu.push.huawei.HuaweiPushImpl.class)
			.build()),

    //极光推送
    jiguang(PushHandles.newBuilder()
            .register(DeviceType.android, com.yuntu.push.jiguang.AndroidPushImpl.class)
            .register(DeviceType.ios,com.yuntu.push.jiguang.IosPushImpl.class)
            .register(DeviceType.all,com.yuntu.push.jiguang.AllPushImpl.class)
            .build()),
    
    //小米
    xiaomi(PushHandles.newBuilder()
            .register(DeviceType.android, com.yuntu.push.xiaomi.AndroidXiaomiPushImpl.class)
            .register(DeviceType.all, com.yuntu.push.xiaomi.AndroidXiaomiPushImpl.class)
            .build());

    Provider(PushHandles pushHandles){
        this.handles=pushHandles;
    }

    private PushHandles handles;

    public PushHandles pushHandles(){
        return handles;
    }
}
