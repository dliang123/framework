/**  **/
package com.yuntu.base.app;

import java.util.Map;

/**
 * @author
 * @date 2016年12月27日
 */
public abstract class BehaviorFactory {
	
	public static Behavior getToast(String content) {
		return new Toast(content);
	}
	
	public static Behavior getAlert(String title, String content) {
		return new Alert(title, content);
	}
	
	public static Behavior getAlert(String title, String content, 
			int lineSize, Map<String, String> buttons) {
		return new Alert(title, content, lineSize, buttons);
	}
	
	public static Behavior getNotification(String content, String urlScheme) {
		return new Notification(content, urlScheme);
	}
}
