package com.yuntu.push;

import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;

import java.util.List;
import java.util.Map;

/**
 * 消息推送类 Created by 林晓辉 on 14-5-19.
 */
public interface Pushable {

	/**
	 * 向终端推送广播通知
	 * 
	 * @param notificationEntity
	 *            通知主体
	 * @return
	 * @throws Exception
	 */
	public void pushBroadcastNotification(NotificationEntity notificationEntity,
			KeyPair keyPair) throws Exception;

	/**
	 * 按手机推送通知
	 * 
	 * @param notificationEntity
	 *            通知主题
	 * @param phone
	 *            标签
	 * @return
	 * @throws Exception
	 */
	public void pushPhoneNotification(NotificationEntity notificationEntity, String phone,
			KeyPair keyPair) throws Exception;

	/**
	 * 按手机推送通知
	 * 
	 * @param notificationEntity
	 *            通知主题
	 * @param phones
	 *            标签
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public void pushPhoneNotification(NotificationEntity notificationEntity,
			String[] phones, KeyPair keyPair) throws Exception;

	/**
	 * 按手机推送通知
	 * 
	 * @param notificationEntity
	 *            通知主题
	 * @param phones
	 *            标签
	 * @return
	 * @throws Exception
	 */
	public void pushPhoneNotification(NotificationEntity notificationEntity,
			List<String> phones, KeyPair keyPair) throws Exception;

	/**
	 * 按标签推送通知
	 * 
	 * @param notificationEntity
	 *            通知主题
	 * @param tag
	 *            标签
	 * @return
	 * @throws Exception
	 */
	public void pushTagNotification(NotificationEntity notificationEntity, String tag,
			KeyPair keyPair) throws Exception;

	/**
	 * 按标签推送通知
	 * 
	 * @param notificationEntity
	 *            通知主题
	 * @param tags
	 *            标签
	 * @return
	 * @throws Exception
	 */
	public void pushTagNotification(NotificationEntity notificationEntity, String[] tags,
			KeyPair keyPair) throws Exception;

	/**
	 * 按标签推送通知
	 * 
	 * @param notificationEntity
	 *            通知主题
	 * @param tags
	 *            标签
	 * @return
	 * @throws Exception
	 */
	public void pushTagNotification(NotificationEntity notificationEntity,
			List<String> tags, KeyPair keyPair) throws Exception;

	/**
	 * 按map格式标签推送通知
	 * Add by Ice Wang on 2016-10-14
	 * @param notificationEntity
	 * @param map
	 * @param keyPair
	 * @throws Exception
	 * @date 2016年10月14日
	 */
	public void pushMapNotification(NotificationEntity notificationEntity,
			Map<String, String[]> map, KeyPair keyPair) throws Exception;
}
