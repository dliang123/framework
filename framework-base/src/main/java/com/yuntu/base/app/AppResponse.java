/**  **/
package com.yuntu.base.app;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yuntu.base.BusinessException;

/**
 * @author icewang
 * @date 2016年12月27日
 */
@JsonInclude(Include.NON_NULL)
public class AppResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private T data;
	
	private AppResponseError error;
	
	private Behavior behavior;

	public static <T> AppResponse<T> success(T data) {
		return new AppResponse<T>(data);
	}
	
	public static <T> AppResponse<T> success(T data, Behavior behavior) {
		return new AppResponse<T>(data, behavior);
	}
	
	public static <T> AppResponse<T> error(Exception ex) {
		return new AppResponse<>(ex);
	}
	
	public static <T> AppResponse<T> error(Exception ex, boolean debug) {
		return new AppResponse<>(ex, debug);
	}
	
	public static <T> AppResponse<T> error(Exception ex, Behavior behavior) {
		return new AppResponse<>(ex, behavior);
	}
	
	public static <T> AppResponse<T> error(Exception ex, boolean debug, Behavior behavior) {
		return new AppResponse<>(ex, debug, behavior);
	}
	
	private AppResponse() {
	}
	
	private AppResponse(T data) {
		this.data = data;
	}
	
	private AppResponse(T data, Behavior behavior) {
		this(data);
		this.behavior = behavior;
	}
	
	private AppResponse(Exception ex) {
		this(ex, false);
	}

	private AppResponse(Exception ex, boolean debug) {
		this(ex, debug, null);
	}
	
	private AppResponse(Exception ex, Behavior behavior) {
		this(ex, false, behavior);
	}
	
	private AppResponse(Exception ex, boolean debug, Behavior behavior) {
		this.error = new AppResponseError(ex, debug);
		String content;
		if(ex instanceof BusinessException) {
			BusinessException bizEx = (BusinessException)ex;
			content = bizEx.getMessage();
		} else {
			content = "请求出错，请稍后再试";
		}
		if(behavior != null) {
			this.behavior = behavior;
		} else {
			this.behavior = BehaviorFactory.getToast(content);
		}
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public AppResponseError getError() {
		return error;
	}

	public void setError(AppResponseError error) {
		this.error = error;
	}

	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}
}
