package com.yuntu.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 *  15/11/12.
 */
@ControllerAdvice
public class ResponseBodyResolver extends AbstractJsonpResponseBodyAdvice {

    public ResponseBodyResolver(){
        super("jsonp","callback");
    }

    public ResponseBodyResolver(String ... queryParamNames) {
        super(queryParamNames);
    }


    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }


}
