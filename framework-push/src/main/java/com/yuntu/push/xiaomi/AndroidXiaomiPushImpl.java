package com.yuntu.push.xiaomi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import com.xiaomi.xmpush.server.Message;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;

import java.util.HashMap;

/**
 * Created by jack on 10/14/16.
 */
public class AndroidXiaomiPushImpl extends XiaomiPushImpl {
	@Override
	protected  Message buildMessage(NotificationEntity notificationEntity,String packageName) throws JsonProcessingException {
		Message message = new Message.Builder()
				.title(notificationEntity.getTitle())
				.description(notificationEntity.getDescription()).payload(JSONUtils.toJSON(notificationEntity.getParams()))
				.restrictedPackageName(packageName)
				.passThrough(0)  	//设置消息是否通过透传的方式送给app，1表示透传消息，0表示通知栏消息。
				.notifyType(-1)     // 使用默认提示音提示	设置通知类型，type的值可以是DEFAULT_ALL或者以下其他几种的OR组合：
				//DEFAULT_ALL = -1;//all DEFAULT_SOUND  = 1;   // 使用默认提示音提示 DEFAULT_VIBRATE = 2;   // 使用默认震动提示 DEFAULT_LIGHTS = 4;    // 使用默认led灯光提示
				//		timeToLive( int milliseconds)	可选项。如果用户离线，设置消息在服务器保存的时间，单位：ms。服务器默认最长保留两周。
				//		timeToSend(long timeToSend)	可选项。定时发送消息。timeToSend是以毫秒为单位的时间戳。注：仅支持七天内的定时消息。
				//		notifyId(Integer id)	可选项。默认情况下，通知栏只显示一条推送消息。如果通知栏要显示多条推送消息，需要针对不同的消息设置不同的notify_id（相同notify_id的通知栏消息会覆盖之前的）。
				//		extra(String key, String value)	可选项，对app提供一些扩展的功能，请参考2.2。除了这些扩展功能，开发者还可以定义一些key和value来控制客户端的行为。注：key和value的字符数不能超过1024，至多可以设置10个key-value键值对。
				.build();
		return message;
	}

	public static void main(String[] args) throws Exception {
		AndroidXiaomiPushImpl push = new AndroidXiaomiPushImpl();
		HashMap param = Maps.newHashMap();
		param.put("url","http://www.baidu.com");
		push.pushBroadcastNotification(NotificationEntity.newInstance("消息标题","消息描述", param),new KeyPair("9BBwOi38OusOkO1rWjzPlA==",null,false,"com.yuntu.android.example"));
	}
}
