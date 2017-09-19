package com.yuntu.web;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Objects;

/**
 * 建议将 getOrder 设置成 1
 * Created by  on 2015/3/24.
 */
public class Route extends RequestMappingHandlerMapping {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(Route.class);

    private String cmdKey = null;
    private String scope = "request";
    private String base = "";

    //默认构造
    public Route(){

        setOrder(Ordered.LOWEST_PRECEDENCE -1);
    }



    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {

        HandlerMethod  method = super.lookupHandlerMethod(lookupPath, request);
        
        logger.debug("CGI_ROUTE_RECEIVE_REQUEST,uri:{},base:{}",request.getRequestURI(),base);
        
        if( null == method && Objects.equals(request.getRequestURI(),base)){
            final String _lookupPath = lookupPath(lookupPath,request);
            method = super.lookupHandlerMethod(_lookupPath, new HttpServletRequestWrapper(request){
                @Override
                public String getRequestURI() {
                    return _lookupPath;
                }

                @Override
                public String getServletPath() {
                    return "";
                }
            });
        }
        return method;
    }


    protected String lookupPath(String lookupPath,HttpServletRequest request){

        String _lookupPath = null;

        if( null != cmdKey ){
            if(Objects.equals(scope,"header")){
                _lookupPath = request.getHeader(cmdKey);
            }else {
                _lookupPath = request.getParameter(cmdKey);
            }

            if( Strings.isNullOrEmpty(lookupPath) ){
                _lookupPath = lookupPath;
            }
        }
        
        logger.debug("CGI_ROUTE_RECEIVE_REQUEST,uri:{},base:{},scope:{},cmdKey:{},cmd:{}",request.getRequestURI(),base,scope,cmdKey,_lookupPath);
        
        return _lookupPath;
    }


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCmdKey() {
        return cmdKey;
    }

    public void setCmdKey(String cmdKey) {
        this.cmdKey = cmdKey;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}