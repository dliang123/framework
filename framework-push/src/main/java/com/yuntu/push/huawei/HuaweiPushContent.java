/**  **/
package com.yuntu.push.huawei;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 华为推送正文
 * 
 * @author icewang
 * @date 2016年10月13日
 */
public class HuaweiPushContent implements Serializable {

	private static final long serialVersionUID = 1L;

	private String notification_title;

	private String notification_content;

	/** 默认行为是打开app，值为1 **/
	private int doings = 1;

	private List<Map<String, String[]>> extras;

	public HuaweiPushContent() {
	}

	public HuaweiPushContent(String title, String content) {
		this.notification_title = title;
		this.notification_content = content;
	}

	public String getNotification_title() {
		return notification_title;
	}

	public void setNotification_title(String notification_title) {
		this.notification_title = notification_title;
	}

	public String getNotification_content() {
		return notification_content;
	}

	public void setNotification_content(String notification_content) {
		this.notification_content = notification_content;
	}

	public int getDoings() {
		return doings;
	}

	public void setDoings(int doings) {
		this.doings = doings;
	}

	public List<Map<String, String[]>> getExtras() {
		return extras;
	}

	public void setExtras(List<Map<String, String[]>> extras) {
		this.extras = extras;
	}

}
