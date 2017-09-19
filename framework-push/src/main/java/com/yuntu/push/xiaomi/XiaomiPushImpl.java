package com.yuntu.push.xiaomi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;
import com.yuntu.push.Pushable;
import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jack on 10/14/16.
 */
public abstract class XiaomiPushImpl implements Pushable {
	private static final Logger logger = LoggerFactory.getLogger(XiaomiPushImpl.class);
	/**
	 * 向终端推送广播通知
	 *
	 * @param notificationEntity 通知主体
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	@Override
	public void pushBroadcastNotification(NotificationEntity notificationEntity, KeyPair keyPair) throws Exception {
		Asserts.notNull(notificationEntity,"notificationEntity not null");
		Asserts.notNull(keyPair,"keyPair not null");
		Asserts.notEmpty(keyPair.getAppPackage(), "AppPackage not null");
		Asserts.notEmpty(keyPair.getSecret(), "Secret not null");

		Constants.useOfficial();
		Sender sender = new Sender(keyPair.getSecret());
		sender.broadcastAll(buildMessage(notificationEntity, keyPair.getAppPackage()), 3);
	}


	/**
	 * 按手机推送通知
	 *
	 * @param notificationEntity 通知主题
	 * @param phone              别名
	 * @param keyPair(小米的key=app的package)
	 * @return
	 * @throws Exception
	 */
	@Override
	public void pushPhoneNotification(NotificationEntity notificationEntity, String phone, KeyPair keyPair) throws Exception {
		Asserts.notNull(notificationEntity,"notificationEntity not null");
		Asserts.notNull(keyPair,"keyPair not null");
		Asserts.notEmpty(keyPair.getAppPackage(), "AppPackage not null");
		Asserts.notEmpty(keyPair.getSecret(), "Secret not null");
		Asserts.notEmpty(phone, "Phone not null");

		Constants.useOfficial();
		Sender sender = new Sender(keyPair.getSecret());
		sender.sendToAlias(buildMessage(notificationEntity,keyPair.getAppPackage()),phone,3);
	}

	/**
	 * 按手机推送通知
	 *
	 * @param notificationEntity 通知主题
	 * @param phones             别名
	 * @param keyPair
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	@Override
	public void pushPhoneNotification(NotificationEntity notificationEntity, String[] phones, KeyPair keyPair) throws Exception {
		Asserts.notNull(notificationEntity,"notificationEntity not null");
		Asserts.notNull(keyPair,"keyPair not null");
		Asserts.notEmpty(keyPair.getAppPackage(), "AppPackage not null");
		Asserts.notEmpty(keyPair.getSecret(), "Secret not null");
		Asserts.notNull(phones, "Phone not null");

		Constants.useOfficial();
		Sender sender = new Sender(keyPair.getSecret());
		sender.sendToAlias(buildMessage(notificationEntity,keyPair.getAppPackage()), Arrays.asList(phones),3);
	}

	/**
	 * 按手机推送通知
	 *
	 * @param notificationEntity 通知主题
	 * @param phones             别名
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	@Override
	public void pushPhoneNotification(NotificationEntity notificationEntity, List<String> phones, KeyPair keyPair) throws Exception {
		Asserts.notNull(notificationEntity,"notificationEntity not null");
		Asserts.notNull(keyPair,"keyPair not null");
		Asserts.notEmpty(keyPair.getAppPackage(), "AppPackage not null");
		Asserts.notEmpty(keyPair.getSecret(), "Secret not null");
		Asserts.notNull(phones, "Phone not null");

		Constants.useOfficial();
		Sender sender = new Sender(keyPair.getSecret());
		sender.sendToAlias(buildMessage(notificationEntity,keyPair.getAppPackage()),phones,3);
	}

	/**
	 * 按标签推送通知
	 *
	 * @param notificationEntity 通知主题
	 * @param tag                标签
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	@Override
	public void pushTagNotification(NotificationEntity notificationEntity, String tag, KeyPair keyPair) throws Exception {
		Asserts.notNull(notificationEntity,"notificationEntity not null");
		Asserts.notNull(keyPair,"keyPair not null");
		Asserts.notEmpty(keyPair.getAppPackage(), "AppPackage not null");
		Asserts.notEmpty(keyPair.getSecret(), "Secret not null");
		Asserts.notNull(tag, "Tag not null");

		Constants.useOfficial();
		Sender sender = new Sender(keyPair.getSecret());
		sender.broadcast(buildMessage(notificationEntity, keyPair.getAppPackage()), tag, 3);
	}

	/**
	 * 按标签推送通知
	 *
	 * @param notificationEntity 通知主题
	 * @param tags               标签
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	@Override
	public void pushTagNotification(NotificationEntity notificationEntity, String[] tags, KeyPair keyPair) throws Exception {
		Asserts.notNull(notificationEntity,"notificationEntity not null");
		Asserts.notNull(keyPair,"keyPair not null");
		Asserts.notEmpty(keyPair.getAppPackage(), "AppPackage not null");
		Asserts.notEmpty(keyPair.getSecret(), "Secret not null");
		Asserts.notNull(tags, "Tags not null");

		Constants.useOfficial();
		Sender sender = new Sender(keyPair.getSecret());
		sender.multiTopicBroadcast(buildMessage(notificationEntity, keyPair.getAppPackage()), Arrays.asList(tags), Sender.BROADCAST_TOPIC_OP.UNION, 3);
	}

	/**
	 * 按标签推送通知
	 *
	 * @param notificationEntity 通知主题
	 * @param tags               标签
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	@Override
	public void pushTagNotification(NotificationEntity notificationEntity, List<String> tags, KeyPair keyPair) throws Exception {
		Asserts.notNull(notificationEntity,"notificationEntity not null");
		Asserts.notNull(keyPair,"keyPair not null");
		Asserts.notEmpty(keyPair.getAppPackage(), "AppPackage not null");
		Asserts.notEmpty(keyPair.getSecret(), "Secret not null");
		Asserts.notNull(tags, "Tags not null");

		Constants.useOfficial();
		Sender sender = new Sender(keyPair.getSecret());
		sender.multiTopicBroadcast(buildMessage(notificationEntity, keyPair.getAppPackage()), tags, Sender.BROADCAST_TOPIC_OP.UNION, 3);
	}

	/**
	 * 按map格式标签推送通知
	 *
	 * @param notificationEntity
	 * @param map
	 * @param keyPair
	 * @throws Exception
	 * @date 2016年10月14日
	 */
	@Override
	public void pushMapNotification(NotificationEntity notificationEntity, Map<String, String[]> map, KeyPair keyPair) throws Exception {
		Asserts.notNull(notificationEntity,"notificationEntity not null");
		Asserts.notNull(keyPair,"keyPair not null");
		Asserts.notEmpty(keyPair.getAppPackage(), "AppPackage not null");
		Asserts.notEmpty(keyPair.getSecret(), "Secret not null");
		Asserts.notNull(map, "map not null");

		Constants.useOfficial();
		Sender sender = new Sender(keyPair.getSecret());
		sender.multiTopicBroadcast(buildMessage(notificationEntity, keyPair.getAppPackage()), Arrays.asList((String[]) map.values().toArray()), Sender.BROADCAST_TOPIC_OP.UNION, 3);
	}

	protected abstract Message buildMessage(NotificationEntity notificationEntity,String packageName) throws JsonProcessingException;
}
