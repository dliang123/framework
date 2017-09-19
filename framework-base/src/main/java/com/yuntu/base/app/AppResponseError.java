/**  **/
package com.yuntu.base.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yuntu.base.BusinessException;

/**
 * @author
 * @date 2016年12月27日
 */
@JsonInclude(Include.NON_NULL)
public class AppResponseError {
	
	private static final int DEFAULT_ERROR_CODE = 500000;
	
	private int code;
	
	private String message;
	
	private String debugInfo;

	public AppResponseError() {}
	
	public AppResponseError(Exception ex) {
		this.message = ex.getMessage();
		if(ex instanceof BusinessException) {
			BusinessException bizEx = (BusinessException)ex;
			this.code = bizEx.getCode();
		} else {
			this.code = DEFAULT_ERROR_CODE;
		}
	}
	
	public AppResponseError(Exception ex, boolean debug) {
		this(ex);
		if(debug) {
			debugInfo = getExceptionStack(ex);
		}
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugInfo() {
		return debugInfo;
	}

	public void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	
	private String getExceptionStack(Exception ex) {
		StackTraceElement[] elements = ex.getStackTrace();
		int lines = 0;
		StringBuffer buffer = new StringBuffer();
		for(StackTraceElement element : elements) {
			if((++lines) > 20) {
				break;
			}
			buffer.append(" at ");
			buffer.append(element.getClassName());
			buffer.append(".");
			buffer.append(element.getMethodName());
			buffer.append("(");
			buffer.append(element.getFileName());
			buffer.append(":");
			buffer.append(element.getLineNumber());
			buffer.append(");\r\n");
		}
		buffer.append("……");
		return buffer.toString();
	}
}
