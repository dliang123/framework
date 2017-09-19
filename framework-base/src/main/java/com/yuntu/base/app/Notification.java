/**  **/
package com.yuntu.base.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author
 * @date 2016年12月27日
 */
@JsonInclude(Include.NON_NULL)
public class Notification extends Behavior {

	private static final long serialVersionUID = 1L;
	
	protected String urlScheme;
	
	protected Notification(String content, String urlScheme) {
		super(content);
		this.urlScheme = urlScheme;
	}

	public String getUrlScheme() {
		return urlScheme;
	}

	public void setUrlScheme(String urlScheme) {
		this.urlScheme = urlScheme;
	}

	@Override
	public String getType() {
		return BehaviorType.NOTICE.toString();
	}
}
