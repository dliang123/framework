package com.yuntu.base;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * -2 参数错误
 * -3 重复提交的请求
 * Created by linxiaohui on 15/12/4.
 */
public final class Response<T> implements Serializable {


    private static final long serialVersionUID = 3686621546404243711L;
    private static final String DEFAULT_SUCCESS_MESSAGE="ok";
    private static final String DEFAULT_FAILURE_MESSAGE="failure";
    /** 默认异常 **/
    private static final int DEFAULT_ERROR_CODE = -1;
    /** 参数异常 **/
    private static final int PARAMETER_ERROR_CODE = -2;
    /** 提交重复 **/
    private static final int SUBMITTED_REPEATED_CODE = -3;

    private boolean success;
    private boolean unAuthentication;
    private Integer code=0;
    private String  message;
    private T data;
    private Object ext;
    private String requestId;

    private Response(){

    }

    private Response(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public static <T> Response<T> success(){
        Response<T>  response = new Response<T>(true,DEFAULT_SUCCESS_MESSAGE);
        return response;
    }

    public static <T> Response<T> success(T data,String message){
        Response<T>  response = new Response<T>(true,message);
        response.data = data;
        return response;
    }

    public static <T> Response<T> success(T data){
        Response<T>  response = new Response<T>(true,DEFAULT_SUCCESS_MESSAGE);
        response.data = data;
        return response;
    }

    public static <T> Response<T> failure(){
        return new Response<T>(false,DEFAULT_FAILURE_MESSAGE);
    }

    public static <T> Response<T> failure(String message){
        Response<T> response = new Response<T>(false,message);
        response.code = DEFAULT_ERROR_CODE;
        return response;
    }


    public static <T> Response<T> parameterError(String message){
        Response<T> response = failure(message);
        response.code = PARAMETER_ERROR_CODE;
        return response;
    }

    public static <T> Response<T> submittedRepeated(String message){
        Response<T> response = failure(message);
        response.code = SUBMITTED_REPEATED_CODE;
        return response;
    }

    public static <T> Response<T> failure(Exception exception){
        if( null == exception )
            return new Response<T>(false,null);
        Throwable throwable = rootCause(exception);
        String message = throwable.getMessage();

        if( exception instanceof BusinessException ){
            return failure(message , ((BusinessException) exception).getCode());
        }
        if( throwable instanceof BusinessException ){
            return failure(message , ((BusinessException) throwable).getCode());
        }
        if( exception instanceof BusinessRuntimeException ){
            return failure(message, ((BusinessRuntimeException) exception).getCode());
        }
        if( throwable instanceof BusinessRuntimeException ){
            return failure(message, ((BusinessRuntimeException) throwable).getCode());
        }
        if( throwable.getClass().getName().equals("javax.validation.ValidationException") ){
            return parameterError(message);
        }
        if( throwable instanceof java.sql.SQLException && "23000".endsWith(((SQLException)throwable).getSQLState())  ){
            return submittedRepeated(message);
        }
        return failure(message);
    }

    public static <T> Response<T> failure(String message,Integer code){
        Response<T> response = failure(message);
        response.code = code;
        return response;
    }

    public static <T> Response<T> unAuthentication(String message){
        Response<T> response = failure(message);
        response.unAuthentication = true;
        return response;
    }

    public static <T> Response<T> unAuthentication(String message,Integer code){
        Response<T> response = failure(message,code);
        response.unAuthentication = true;
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public Response<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }


    public T getData() {
        return data;
    }

    public boolean isUnAuthentication() {
        return unAuthentication;
    }

    public Object getExt() {
        return ext;
    }

    public Response setExt(Object ext) {
        this.ext = ext;
        return this;
    }

    private static Throwable rootCause(Throwable throwable) {
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
        }
        return throwable;
    }

    public String getRequestId() {
        return requestId;
    }

    public Response setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
