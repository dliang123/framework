package com.yuntu.common.exception;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

@SuppressWarnings({"rawtypes","unchecked"})
public class DefaultExceptionConvert<T extends Exception> implements ExceptionConvert<T> {

    private static Field FIELD_DETAIL_MESSAGE=null;
    static {
        try {
            FIELD_DETAIL_MESSAGE=Throwable.class.getDeclaredField("detailMessage");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private enum ConstructorType{
        def,
        string,
        throwable

    }

    private ConstructorType type;
	private Constructor constructor;
    public DefaultExceptionConvert(Class<T> excClass){

        Constructor [] constructors = excClass.getDeclaredConstructors();
        Constructor def=null,string=null,throwable=null;
        for( Constructor constructor : constructors ){

            Type[] types=constructor.getGenericParameterTypes();
            if( null == types || types.length<1 ){
                def = constructor;
            }else if( types.length == 1 && types[0] == String.class ){
                string = constructor;
            }else if( types.length == 1 && types[0] == Throwable.class ){
                throwable = constructor;
            }
        }
        if(null != def){
            this.type = ConstructorType.def;
            this.constructor = def;
        }else if(null != throwable){
            this.type = ConstructorType.throwable;
            this.constructor = throwable;
        }else if( null != string  ){
            this.type = ConstructorType.string;
            this.constructor = string;
        }
    }

	public T cast(String msg) {
        try {
            if( type == ConstructorType.def ){
                T exception = (T)constructor.newInstance();
                FIELD_DETAIL_MESSAGE.setAccessible(true);
                FIELD_DETAIL_MESSAGE.set(exception,msg);
                return exception;
            }
            if( type == ConstructorType.string ){
                return (T)constructor.newInstance(msg);
            }
            if( type == ConstructorType.throwable ){
                return (T)constructor.newInstance(new Exception(msg));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public T cast(Throwable throwable) {
        T exception = cast("");
        if( null !=exception )
            exception.initCause(throwable);
        return exception;
    }
}