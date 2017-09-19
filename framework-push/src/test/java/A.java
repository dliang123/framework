import java.util.Map;

import com.google.common.collect.Maps;
import com.yuntu.push.constant.DeviceType;
import com.yuntu.push.constant.Provider;
import com.yuntu.push.domain.KeyPair;
import com.yuntu.push.domain.NotificationEntity;
import com.yuntu.push.domain.PushConfig;
import com.yuntu.push.utils.PushUtil;

/**
 * Created by linxiaohui on 16/1/28.
 */
public class A {

	public static void main(String... args) throws Exception {

		PushConfig config = PushConfig.newInstance(Provider.huawei, new KeyPair(
				"99849b5d59f7c10b2eb6170a6d2a4868", "10677396", Boolean.FALSE));

		NotificationEntity e = NotificationEntity.newInstance("test", "这是一条测试消息");

		PushUtil.pushPhoneNotification(e, "007", DeviceType.all, config);
		
//		Map<String, String[]> map = Maps.newHashMap();
//		map.put("type", new String[] {"cdr"});
//		PushUtil.pushMapNotification(e, map, DeviceType.all, config);
	}
}
