/**  **/
package com.yuntu.base.app;

import java.io.Serializable;

/**
 * @author
 * @date 2016年12月27日
 */
public abstract class Behavior implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String content;
	
	protected String extra;
	
	public abstract String getType();
	
	protected Behavior(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
	
}
