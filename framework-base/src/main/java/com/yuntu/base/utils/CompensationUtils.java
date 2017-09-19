package com.yuntu.base.utils;

import com.yuntu.base.CompensationTemplate;
import com.yuntu.base.SimpleCompensationTemplate;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by linxiaohui on 15/12/8.
 */
public class CompensationUtils {

    private static final Logger logger = Logger.getLogger(CompensationUtils.class.getName());

    @SafeVarargs
	public static <T> T execute(CompensationTemplate<T> template,int retries,Class<? extends Exception> ... retriesFor) throws Exception {

        if(  null == retriesFor || retries<1 )
            return template.execute();

        for(  int i=0; i<retries; i++ ){
            try {
                return template.execute();
            }catch (Exception e){

                if( needCompensation( e,retriesFor ) ){
                    if( logger.isLoggable(Level.INFO) || logger.isLoggable(Level.WARNING)
                            || logger.isLoggable(Level.FINER) || logger.isLoggable(Level.ALL) ){
                        logger.info(MessageFormat.format("出现异常{0} 尝试第{1}次补偿",e,i+1));
                    }
                    continue;
                }
                throw e;
            }
        }
        return template.execute();
    }

	public static <T> T execute(int retries,Class<? extends Exception>  retriesFor,CompensationTemplate<T> template) throws Exception {

        return execute(template,retries,retriesFor);
    }

    private static boolean needCompensation(Exception e,Class<? extends Exception> [] retriesFor){
        if( null ==e || null == retriesFor )
            return false;

        for(Class<? extends Exception> c : retriesFor ){
            if( null != c && c.isInstance(e) ){
             return true;
            }
        }
        return false;
    }

    public static <T> T execute(SimpleCompensationTemplate<T> template) throws Exception{

        return execute(template,template.retries(),template.retriesFor());
    }
}
