package com.yuntu.web.request;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.yuntu.web.constant.PreventInjectionStrategy;
import com.yuntu.web.exception.PreventInjectionException;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.text.MessageFormat;

/*
 * Author:
 * Date:     14-11-6
 * Description: 防止注入的request装饰器
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            14-11-6           00000001         创建文件
 *
 */
public class PreventInjectionRequest extends HttpServletRequestWrapper {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PreventInjectionRequest.class);

    //遇到 可能执行sql注入的参数是否抛出异常
    private PreventInjectionStrategy strategy = PreventInjectionStrategy.replace;
    //需要过滤的 关键字
    private static String [] leaches = new String[]{
            "'","and" ,"exec","insert","select" ,"delete","update","chr","mid","master" ,"truncate","truncate","declare"
            ,"iframe","script","drop","alter","grant","*" ,"%"
    };




    public PreventInjectionRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String [] getParameterValues(String name){

        return leach(name ,super.getParameterValues(name));
    }

    @Override
    public java.util.Map<String , String[]> getParameterMap(){

        @SuppressWarnings("unchecked")
		java.util.Map<String , String[]> oldParameterMap =  super.getParameterMap();
        java.util.Map<String , String[]> newParameterMap = Maps.newConcurrentMap();
        for(java.util.Map.Entry<String,String[]> entry : oldParameterMap.entrySet()  ){
            String [] value = leach(entry.getKey() , entry.getValue());
            newParameterMap.put(entry.getKey(),value);
        }
        return newParameterMap;
    }

    /**
     * 重写getParameter方法
     *
     * @param name
     *            参数
     * @return
     */
    @Override
    public String getParameter(String name) {
        return leach(name , super.getParameter(name) );
    }

    private String [] leach(String key,String [] oldValue){
        if( null != oldValue ){
            String [] newValue= new String[oldValue.length];
            for(int i=0;i<oldValue.length;i++ ){
                newValue[i]=leach( key ,oldValue[i] );
            }
            return newValue;
        }
        return  oldValue;
    }

    private String leach(String key,String value){

        if( Strings.isNullOrEmpty( value ) || null == leaches || leaches.length < 1){
            return value;
        }

        for (String leach : leaches) {
            if( leach.contains( value.toLowerCase() ) ){ //包含关键字
                switch ( strategy ){
                    case throwException: {

                        if( logger.isDebugEnabled() ){
                            logger.debug("根据策略 {} 对包含关键字 {} 的 key {} 执行抛出异常",strategy, leach ,key);
                        }
                        throw new PreventInjectionException(MessageFormat.format("属性{0} 包含关键字 {1}",value,leach));
                    }
                    case replace: {

                        if( logger.isDebugEnabled() ){
                            logger.debug("根据策略 {} 对包含关键字 {} 的 key {} 进行替换",strategy, leach ,key);
                        }
                        return value.replace("(?i)" + leach.trim(), "");
                    }
                    case none:{
                        if( logger.isDebugEnabled() ){
                            logger.debug("根据策略 {} 对包含关键字 {} 的 key {} 不进行操作",strategy, leach ,key);
                        }
                    }
                }
            }
        }
        return value;
    }

    public PreventInjectionStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(PreventInjectionStrategy strategy) {
        this.strategy = strategy;
    }

    public static String[] getLeaches() {
        return leaches;
    }

    public static void setLeaches(String[] leaches) {
        PreventInjectionRequest.leaches = leaches;
    }
}
