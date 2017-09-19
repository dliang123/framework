/**  **/
package com.yuntu.push.huawei;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.common.utils.StringUtils;
import com.yuntu.push.Pushable;
import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;

import nsp.NSPClient;
import nsp.OAuth2Client;
import nsp.support.common.AccessToken;
import nsp.support.common.NSPException;

/**
 * 华为推送实现
 * 
 * @author icewang
 * @date 2016年10月13日
 */
public class HuaweiPushImpl implements Pushable {

	private static final Logger logger = LoggerFactory.getLogger(HuaweiPushImpl.class);

	/** 华为广播标签，所有客户端都有该标签 **/
	private static final String HUAWEI_BROADCAST_KEY = "broadcast";
	private static final String HUAWEI_BROADCAST_VALUE = "yuntu";
	private static final Map<String, String[]> HUAWEI_BROADCAST_MAP = Maps.newHashMap();

	static {
		HUAWEI_BROADCAST_MAP.put(HUAWEI_BROADCAST_KEY,
				new String[] { HUAWEI_BROADCAST_VALUE });
	}

	@Override
	public void pushBroadcastNotification(NotificationEntity notificationEntity,
			KeyPair keyPair) throws Exception {
		push(notificationEntity, HUAWEI_BROADCAST_MAP, keyPair);
	}

	/**
	 * 华为推送“别名”标签，由于key"alias"似乎被华为占用，故特殊处理使用uid作为别名的key
	 */
	private static final String HUAWEI_ALIAS_KEY = "uid";

	@Override
	public void pushPhoneNotification(NotificationEntity notificationEntity, String phone,
			KeyPair keyPair) throws Exception {
		if (StringUtils.isNullOrEmpty(phone)) {
			return;
		}
		Map<String, String[]> map = Maps.newHashMap();
		map.put(HUAWEI_ALIAS_KEY, new String[] { phone });
		push(notificationEntity, map, keyPair);
	}

	@Override
	public void pushPhoneNotification(NotificationEntity notificationEntity,
			String[] phones, KeyPair keyPair) throws Exception {
		if (phones == null || phones.length <= 0) {
			return;
		}
		Map<String, String[]> map = Maps.newHashMap();
		map.put(HUAWEI_ALIAS_KEY, phones);
		push(notificationEntity, map, keyPair);
	}

	@Override
	public void pushPhoneNotification(NotificationEntity notificationEntity,
			List<String> phones, KeyPair keyPair) throws Exception {
		if (phones == null || phones.isEmpty()) {
			return;
		}
		Map<String, String[]> map = Maps.newHashMap();
		map.put(HUAWEI_ALIAS_KEY, phones.toArray(new String[phones.size()]));
		push(notificationEntity, map, keyPair);
	}

	private static final String NOT_SUPPORTED = "华为不支持该格式tag";

	@Override
	public void pushTagNotification(NotificationEntity notificationEntity, String tag,
			KeyPair keyPair) throws Exception {
		// 华为不支持该格式tag
		logger.warn(NOT_SUPPORTED);
		return;
	}

	@Override
	public void pushTagNotification(NotificationEntity notificationEntity, String[] tags,
			KeyPair keyPair) throws Exception {
		// 华为不支持该格式tag
		logger.warn(NOT_SUPPORTED);
		return;
	}

	@Override
	public void pushTagNotification(NotificationEntity notificationEntity,
			List<String> tags, KeyPair keyPair) throws Exception {
		// 华为不支持该格式tag
		logger.warn(NOT_SUPPORTED);
		return;
	}

	@Override
	public void pushMapNotification(NotificationEntity notificationEntity,
			Map<String, String[]> map, KeyPair keyPair) throws Exception {
		if (map == null || map.isEmpty()) {
			return;
		}
		push(notificationEntity, map, keyPair);
	}

	private static OAuth2Client oauth2Client = null;

	private static final String AUTH_TYPE = "client_credentials";

	/**
	 * 1：指定用户，必须指定tokens字段 2：所有人，无需指定tokens，tags，exclude_tags
	 * 3：一群人，必须指定tags或者exclude_tags字段 暂时只能按照tags来推送
	 */
	private static final int PUSH_TYPE = 3;

	private static final String PUSH_METHOD = "openpush.openapi.notification_send";

	private void push(NotificationEntity notificationEntity, Map<String, String[]> tagMap,
			KeyPair keyPair) throws Exception {
		if (oauth2Client == null) {
			init();
		}
		String appId = keyPair.getKey();
		String appKey = keyPair.getSecret();
		AccessToken access_token = oauth2Client.getAccessToken(AUTH_TYPE, appId, appKey);
		NSPClient client = new NSPClient(access_token.getAccess_token());
		client.initHttpConnections(30, 50);// 设置每个路由的连接数和最大连接数
		client.initKeyStoreStream(this.getClass().getResourceAsStream("/mykeystorebj.jks"),
				"123456");
		client.setTimeout(10000, 15000);

		// 标签
		HuaweiTags tags = new HuaweiTags(tagMap);

		// 正文
		HuaweiPushContent content = new HuaweiPushContent(notificationEntity.getTitle(),
				notificationEntity.getDescription());
		if (notificationEntity.getParams() != null
				&& !notificationEntity.getParams().isEmpty()) {
			List<Map<String, String[]>> extras = Lists.newArrayList();
			for (Entry<String, String> e : notificationEntity.getParams().entrySet()) {
				Map<String, String[]> map = Maps.newHashMap();
				map.put(e.getKey(), new String[] { e.getValue() });
				extras.add(map);
			}
			content.setExtras(extras);
		}

		// 构造请求
		HashMap<String, Object> hashMap = Maps.newHashMap();
		hashMap.put("push_type", PUSH_TYPE);
		hashMap.put("tags", JSONUtils.toJSON(tags));
		hashMap.put("android", JSONUtils.toJSON(content));
		String rsp = client.call(PUSH_METHOD, hashMap, String.class);
		logger.warn(rsp);
	}

	private synchronized void init() throws NSPException, IOException {
		oauth2Client = new OAuth2Client();
		oauth2Client.initKeyStoreStream(
				this.getClass().getResourceAsStream("/mykeystorebj.jks"), "123456");
	}
}
