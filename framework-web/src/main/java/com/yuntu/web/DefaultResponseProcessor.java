package com.yuntu.web;

import com.yuntu.base.Response;

import java.util.Objects;

public class DefaultResponseProcessor implements com.yuntu.web.processor.ResponseProcessor {

    public Object processor(Object obj, ResponseType type) {

        if( obj instanceof Response || Objects.equals(type, ResponseType.viewAndModel)){
            return obj;
        }
        return Response.success(obj);
    }

    public Object processorForException(Exception e, ResponseType type) throws Exception {

        if(Objects.equals(type, ResponseType.responseBody)){
            if( ValidationExceptionUtils.isValidationException(e) ){
                return Response.parameterError( ValidationExceptionUtils.firstMessage(e) );
            }else {
                return Response.failure(e);
            }
        }
        throw e;
    }
}
