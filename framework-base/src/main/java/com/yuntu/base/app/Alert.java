/**  **/
package com.yuntu.base.app;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author icewang
 * @date 2016年12月27日
 */
@JsonInclude(Include.NON_NULL)
public class Alert extends Behavior {

	private static final long serialVersionUID = 1L;
	
	protected String title;
	
	protected int lineSize;
	
	protected Map<String, String> buttons;
	
	protected Alert(String title, String content) {
		super(content);
		this.title = title;
		this.lineSize = 1;
		this.buttons = new HashMap<String, String>();
		this.buttons.put("确定", null);
	}
	
	protected Alert(String title, String content, int lineSize, Map<String, String> buttons) {
		super(content);
		this.title = title;
		this.lineSize = (lineSize > 0 ? lineSize : 1);
		if(buttons == null || buttons.isEmpty()) {
			this.buttons = new HashMap<String, String>();
			this.buttons.put("确定", null);
		} else {
			this.buttons = buttons;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLineSize() {
		return lineSize;
	}

	public void setLineSize(int lineSize) {
		this.lineSize = lineSize;
	}

	public Map<String, String> getButtons() {
		return buttons;
	}

	public void setButtons(Map<String, String> buttons) {
		this.buttons = buttons;
	}
	
	public void addButton(String name, String url) {
		if(this.buttons.size() == 1 && this.buttons.containsKey("确定")) {
			buttons.clear();
		}
		buttons.put(name, url);
	}

	@Override
	public String getType() {
		return BehaviorType.ALERT.toString();
	}
}
