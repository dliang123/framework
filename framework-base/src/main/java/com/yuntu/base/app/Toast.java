/**  **/
package com.yuntu.base.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author
 * @date 2016年12月27日
 */
@JsonInclude(Include.NON_NULL)
public class Toast extends Behavior {
	
	private static final long serialVersionUID = 1L;
	
	public Toast(String content) {
		super(content);
	}

	@Override
	public String getType() {
		return BehaviorType.TOAST.toString();
	}
}
