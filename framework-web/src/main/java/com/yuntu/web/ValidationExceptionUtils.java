package com.yuntu.web;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.yuntu.common.utils.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

/**
 * Created by linxiaohui on 15/12/2.
 */
public class ValidationExceptionUtils {


    public static boolean isValidationException(Exception e ){

        return e instanceof ValidationException  || e instanceof  MethodArgumentNotValidException || e instanceof BindingResult;
    }

    public static Map<String,String> message(Exception e){

        Map<String,String> message = Maps.newLinkedHashMap();

        Throwable cause = Throwables.getRootCause(e);
        if( cause instanceof ConstraintViolationException){
            ConstraintViolationException exception = (ConstraintViolationException) cause;
            for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()){
                message.put(constraintViolation.getPropertyPath().toString() ,constraintViolation.getMessage() );
            }
        }


        else if( cause instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) cause;
            for (ObjectError objectError : exception.getBindingResult().getAllErrors()){

                if( objectError instanceof FieldError){
                    FieldError fieldError = (FieldError) objectError;
                    message.put( fieldError.getField() ,fieldError.getDefaultMessage());
                }else {
                    message.put( objectError.getObjectName() ,objectError.getDefaultMessage());
                }

            }
        }

        else if( cause instanceof BindingResult){
            BindingResult bindingResult = (BindingResult) cause;
            for (ObjectError objectError : bindingResult.getAllErrors()){

                if( objectError instanceof FieldError){
                    FieldError fieldError = (FieldError) objectError;
                    message.put( fieldError.getField() ,fieldError.getDefaultMessage());
                }else {
                    message.put( objectError.getObjectName() ,objectError.getDefaultMessage());
                }

            }
        }

        if(ObjectUtils.isEmpty(message)){
            message.put("", cause.getMessage());
        }
        return message;
    }

    public static String firstMessage(Exception e){
        Map<String,String> message = message(e);
        if( ObjectUtils.isEmpty(message) )
            return "";
        return (String)message.values().toArray()[0];
    }
}
