package com.yuntu.common.utils;

import com.yuntu.common.exception.DefaultExceptionConvert;
import com.yuntu.common.exception.ExceptionConvert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  15/8/27.
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ExceptionUtils {


	public static final Map<Class,ExceptionConvert> castCache = new ConcurrentHashMap<Class,ExceptionConvert>();


    public static  <T extends Exception> ExceptionConvert<T> register(Class<T> exClass,ExceptionConvert<T> cast){
        castCache.put(exClass,cast);
        return cast;
    }


	public static <T extends Exception> T cast(String msg,Class<T> excClass){

        ExceptionConvert cast = castCache.get(excClass);
        if( null == cast ){
            cast = register(excClass,new DefaultExceptionConvert(excClass));
        }

        return (T)cast.cast(msg);
    }


    public static <T extends Exception> void tryThrow(boolean isThrow,String msg,Class<T> excClass) throws T{

        if( isThrow )
            throw cast(msg,excClass);

    }

    public static <T extends Exception> void tryThrow(boolean isThrow,Class<T> excClass) throws T {

        if( isThrow )
            throw cast(null,excClass);

    }

    public static <T extends Exception> void tryThrow(boolean isThrow,T  exception) throws T {

        if(isThrow)
            throw exception;
    }
}
