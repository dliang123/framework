package com.yuntu.web.processor;

import com.yuntu.web.ResponseType;

public interface ResponseProcessor{

	Object processor(Object obj,ResponseType type);
	
	Object processorForException(Exception e,ResponseType type) throws Exception;

}
