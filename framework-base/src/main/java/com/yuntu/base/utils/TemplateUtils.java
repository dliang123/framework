package com.yuntu.base.utils;

import com.yuntu.base.CompensationTemplate;
import com.yuntu.base.SimpleCompensationTemplate;
import com.yuntu.base.Template;
import com.yuntu.base.VoidTemplate;
import org.apache.log4j.Logger;


/**
 * Created by linxiaohui on 16/1/20.
 */
public class TemplateUtils {


    private static final Logger logger = Logger.getLogger("templateUtils");

    public static <T> T find(int retries,Class<? extends Exception>  retriesFor,CompensationTemplate<T> template) throws Exception {

        return CompensationUtils.execute(retries,retriesFor,template);
    }

    public static <T> T find(SimpleCompensationTemplate<T> template) throws Exception{

        return CompensationUtils.execute(template);
    }

    public static <T> T findWithNoThrow(Template<T> template){

        try {
            if( template instanceof SimpleCompensationTemplate ){
                return find((SimpleCompensationTemplate<T>)template);
            }else {
                return template.execute();
            }
        }catch (Exception e){
            if( logger.isDebugEnabled() ) logger.info("",e);
            return null;
        }
    }

    public static void executeWithNoThrow(VoidTemplate voidTemplate){
        try {
            voidTemplate.execute();
        }catch (Exception e){
            if( logger.isDebugEnabled() ) logger.info("",e);
        }
    }
}
