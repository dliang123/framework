package com.yuntu.push.utils;

import com.yuntu.push.Pushable;
import com.yuntu.push.constant.DeviceType;
import com.yuntu.push.domain.NotificationEntity;
import com.yuntu.push.domain.PushConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 推送工具类 Created by 林晓辉 on 2014/9/22.
 */
public class PushUtil {

	private static final Logger logger = LoggerFactory.getLogger(PushUtil.class);

	/**
	 * 广播通知(通知到所有人 时间限制1分钟1次)
	 * 
	 * @param notificationEntity
	 *            通知实体
	 * @param deviceType
	 *            设备类型
	 * @return 发送成功条数
	 */
	public static void pushBroadcastNotification(NotificationEntity notificationEntity,
			DeviceType deviceType, PushConfig pushConfig) throws Exception {

		for (Pushable pushable : pushConfig.getProvider().pushHandles()
				.getHandle(deviceType)) {
			try {
				pushable.pushBroadcastNotification(notificationEntity,
						pushConfig.getKeyPair());
			} catch (Exception e) {
				logger.error("设备 {} 推送广播通知出错 {}", deviceType.name(), e.getMessage());
				throw e;
			}
		}

	}

	/**
	 * 根据标签推送给通知
	 * 
	 * @param notificationEntity
	 *            通知实体
	 * @param tag
	 *            标签
	 * @param deviceType
	 *            设备类型
	 * @return
	 */
	public static void pushTagNotification(NotificationEntity notificationEntity,
			String tag, DeviceType deviceType, PushConfig pushConfig) throws Exception {

		for (Pushable pushable : pushConfig.getProvider().pushHandles()
				.getHandle(deviceType)) {
			try {
				pushable.pushTagNotification(notificationEntity, tag,
						pushConfig.getKeyPair());
			} catch (Exception e) {
				logger.error("设备 {} 推送标签 {} 通知出错 {}",
						new Object[] { deviceType.name(), tag, e.getMessage() });
				throw e;
			}
		}

	}

	/**
	 * 根据标签推送给通知
	 * 
	 * @param notificationEntity
	 *            通知实体
	 * @param tags
	 *            标签
	 * @param deviceType
	 *            设备类型
	 * @return
	 */
	public static void pushTagNotification(NotificationEntity notificationEntity,
			String[] tags, DeviceType deviceType, PushConfig pushConfig)
					throws Exception {

		for (Pushable pushable : pushConfig.getProvider().pushHandles()
				.getHandle(deviceType)) {
			try {
				pushable.pushTagNotification(notificationEntity, tags,
						pushConfig.getKeyPair());
			} catch (Exception e) {
				logger.error("设备 {} 推送标签 {} 通知出错 {}",
						new Object[] { deviceType.name(), tags, e.getMessage() });
				throw e;
			}
		}

	}

	/**
	 * 根据标签推送给通知
	 * 
	 * @param notificationEntity
	 *            通知实体
	 * @param tags
	 *            标签
	 * @param deviceType
	 *            设备类型
	 * @return
	 */
	public static void pushTagNotification(NotificationEntity notificationEntity,
			List<String> tags, DeviceType deviceType, PushConfig pushConfig)
					throws Exception {

		for (Pushable pushable : pushConfig.getProvider().pushHandles()
				.getHandle(deviceType)) {
			try {
				pushable.pushTagNotification(notificationEntity, tags,
						pushConfig.getKeyPair());
			} catch (Exception e) {
				logger.error("设备 {} 推送标签 {} 通知出错 {}",
						new Object[] { deviceType.name(), tags, e.getMessage() });
				throw e;
			}
		}
	}

	/**
	 * 根据手机号推送给通知
	 * 
	 * @param notificationEntity
	 *            通知实体
	 * @param phone
	 *            标签
	 * @param deviceType
	 *            设备类型
	 * @return
	 */
	public static void pushPhoneNotification(NotificationEntity notificationEntity,
			String phone, DeviceType deviceType, PushConfig pushConfig) throws Exception {

		for (Pushable pushable : pushConfig.getProvider().pushHandles()
				.getHandle(deviceType)) {
			try {
				pushable.pushPhoneNotification(notificationEntity, phone,
						pushConfig.getKeyPair());
			} catch (Exception e) {
				logger.error("设备 {} 推送手机 {} 通知出错 {}",
						new Object[] { deviceType.name(), phone, e.getMessage() });
				throw e;
			}
		}
	}

	/**
	 * 根据手机号推送给通知
	 * 
	 * @param notificationEntity
	 *            通知实体
	 * @param phones
	 *            标签
	 * @param deviceType
	 *            设备类型
	 * @return
	 */
	public static void pushPhoneNotification(NotificationEntity notificationEntity,
			String[] phones, DeviceType deviceType, PushConfig pushConfig)
					throws Exception {

		for (Pushable pushable : pushConfig.getProvider().pushHandles()
				.getHandle(deviceType)) {
			try {
				pushable.pushPhoneNotification(notificationEntity, phones,
						pushConfig.getKeyPair());
			} catch (Exception e) {
				logger.error("设备 {} 推送手机通知 {} 出错 {}", new Object[] { deviceType.name(),
						Arrays.toString(phones), e.getMessage() });
				throw e;
			}
		}
	}

	/**
	 * 根据手机号推送给通知
	 * 
	 * @param notificationEntity
	 *            通知实体
	 * @param phones
	 *            标签
	 * @param deviceType
	 *            设备类型
	 * @return
	 */
	public static void pushPhoneNotification(NotificationEntity notificationEntity,
			List<String> phones, DeviceType deviceType, PushConfig pushConfig)
					throws Exception {

		for (Pushable pushable : pushConfig.getProvider().pushHandles()
				.getHandle(deviceType)) {
			try {
				pushable.pushPhoneNotification(notificationEntity, phones,
						pushConfig.getKeyPair());
			} catch (Exception e) {
				logger.error("设备 {} 推送手机通知 {} 出错 {}", new Object[] { deviceType.name(),
						Arrays.toString(phones.toArray()), e.getMessage() });
				throw e;
			}
		}
	}

	/**
	 * 按照map格式标签推送
	 * 
	 * @param notificationEntity
	 * @param map
	 * @param deviceType
	 * @param pushConfig
	 * @throws Exception
	 * @date 2016年10月14日
	 */
	public static void pushMapNotification(NotificationEntity notificationEntity,
			Map<String, String[]> map, DeviceType deviceType, PushConfig pushConfig)
					throws Exception {
		for (Pushable pushable : pushConfig.getProvider().pushHandles()
				.getHandle(deviceType)) {
			try {
				pushable.pushMapNotification(notificationEntity, map,
						pushConfig.getKeyPair());
			} catch (Exception e) {
				logger.error("设备 {} 推送标签 {} 通知出错 {}",
						new Object[] { deviceType.name(), map, e.getMessage() });
				throw e;
			}
		}
	}

	/**
	 * 按照map格式标签推送，可以同时推送多平台
	 * 
	 * @param notificationEntity
	 * @param map
	 * @param deviceType
	 * @param pushConfigs
	 * @throws Exception
	 * @date 2016年10月14日
	 */
	public static void pushMapNotification(NotificationEntity notificationEntity,
			Map<String, String[]> map, DeviceType deviceType, PushConfig... pushConfigs)
					throws Exception {
		for (PushConfig pushConfig : pushConfigs) {
			for (Pushable pushable : pushConfig.getProvider().pushHandles()
					.getHandle(deviceType)) {
				try {
					pushable.pushMapNotification(notificationEntity, map,
							pushConfig.getKeyPair());
				} catch (Exception e) {
					logger.error("设备 {} 推送标签 {} 通知出错 {}",
							new Object[] { deviceType.name(), map, e.getMessage() });
				}
			}
		}
	}

	public static void pushPhoneNotification(NotificationEntity notificationEntity,
			String phone, DeviceType deviceType, PushConfig... pushConfigs)
					throws Exception {
		for (PushConfig pushConfig : pushConfigs) {
			for (Pushable pushable : pushConfig.getProvider().pushHandles()
					.getHandle(deviceType)) {
				try {
					pushable.pushPhoneNotification(notificationEntity, phone,
							pushConfig.getKeyPair());
				} catch (Exception e) {
					logger.error("设备 {} 推送手机 {} 通知出错 {}",
							new Object[] { deviceType.name(), phone, e.getMessage() });
				}
			}
		}
	}

	public static void pushPhoneNotification(NotificationEntity notificationEntity,
			List<String> phones, DeviceType deviceType, PushConfig... pushConfigs)
					throws Exception {
		for (PushConfig pushConfig : pushConfigs) {
			for (Pushable pushable : pushConfig.getProvider().pushHandles()
					.getHandle(deviceType)) {
				try {
					pushable.pushPhoneNotification(notificationEntity, phones,
							pushConfig.getKeyPair());
				} catch (Exception e) {
					logger.error("设备 {} 推送手机 {} 通知出错 {}",
							new Object[] { deviceType.name(), phones, e.getMessage() });
				}
			}
		}
	}

	public static void pushBroadcastNotification(NotificationEntity notificationEntity,
			DeviceType deviceType, PushConfig... pushConfigs) throws Exception {
		for (PushConfig pushConfig : pushConfigs) {
			for (Pushable pushable : pushConfig.getProvider().pushHandles()
					.getHandle(deviceType)) {
				try {
					pushable.pushBroadcastNotification(notificationEntity,
							pushConfig.getKeyPair());
				} catch (Exception e) {
					logger.error("设备 {} 推送广播通知出错 {}", deviceType.name(), e.getMessage());
				}
			}
		}
	}
}
