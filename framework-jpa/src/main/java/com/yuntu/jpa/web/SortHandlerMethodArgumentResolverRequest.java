package com.yuntu.jpa.web;

import com.google.common.base.Strings;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 *  2016/10/25.
 */
public class SortHandlerMethodArgumentResolverRequest extends ServletWebRequest {


    private String [] cacheSortParameters = null;
    private String sortParameter = "sort";

    public SortHandlerMethodArgumentResolverRequest(HttpServletRequest nativeWebRequest,SortArgumentResolver resolver,String sortParameter) {
        super( nativeWebRequest);

        if(!Strings.isNullOrEmpty(sortParameter) ){
            this.sortParameter = sortParameter;
        }

        if( null != resolver ){
            cacheSortParameters = resolver.resolver(nativeWebRequest);
        }
    }

    @Override
    public String[] getParameterValues(String paramName) {

        String[] result = super.getParameterValues(paramName);
        if(  Objects.equals(paramName,sortParameter) ){
            return cacheSortParameters;
        }
        return result;
    }
}
