package com.yuntu.configuration.component;

import com.yuntu.base.Response;
import com.yuntu.web.exception.NoAuthorityException;
import com.yuntu.web.exception.NoLoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by linxiaohui on 15/11/12.
 */
@ControllerAdvice
public class ConfigControllerAdvice {


    @ExceptionHandler
    @ResponseBody
    public Object controllerAdvice(Exception e){

        if( e instanceof NoAuthorityException){
            return Response.unAuthentication("没有权限");
        }
        return Response.failure(e);
    }

    @ExceptionHandler(NoLoginException.class)
    @ResponseBody
    public Object controllerAdvice(){

        return Response.unAuthentication("请您登陆!",-10);
    }

}
