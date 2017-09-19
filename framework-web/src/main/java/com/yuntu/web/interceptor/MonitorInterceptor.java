package com.yuntu.web.interceptor;

import com.yuntu.base.utils.ConfigUtils;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.web.Constants;
import com.yuntu.web.request.BodyReaderHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 邮件请通过 log4j.xml 配置成异步发送
 *    monitor_exception  为异常发送邮件
 *    monitor_timeout    为超时发送邮件
 *  15/11/16.
 */
public class MonitorInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME_KEY="__START_TIME";
    private static final Logger LOG = LoggerFactory.getLogger(MonitorInterceptor.class);

    private static final Logger EXCEPTION_LOG = LoggerFactory.getLogger("monitor_exception");
    private static final Logger TIMEOUT_LOG = LoggerFactory.getLogger("monitor_timeout");


//    private static final Map<Method,Long> statistics = Maps.newConcurrentMap();

    @Override
    public boolean preHandle(final HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            request.setAttribute(START_TIME_KEY, System.nanoTime());
            return super.preHandle( request , response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //开始时间
        long startTime =  (Long)request.getAttribute(START_TIME_KEY);
        //时间用时
        long executionTime = System.nanoTime() - startTime;

        if( handler instanceof HandlerMethod){

            HandlerMethod handlerMethod = (HandlerMethod) handler;

            Map<String,Object> headers = requestHeaders(request);
            
            String body = requestBody(request);

            Method method = handlerMethod.getMethod();
            String name = method.getName();

            //标准时间
            long standardTime=ConfigUtils.v( name , 3000L) * 100000;

            if( executionTime > standardTime ){
                TIMEOUT_LOG.warn("方法 {} 执行时间 {}/{}",method,executionTime,standardTime);
            }

            if( ex == null ){
                ex = (Exception) request.getAttribute(Constants.EXCEPTION_KEY);
            }

            if( ex != null ){
                //error级别的任务会有一些额外流程.
                EXCEPTION_LOG.warn(ex.getMessage(),ex);
            }

            if( LOG.isDebugEnabled() ){
                LOG.debug("\n请求{}成功 \n参数{} \n请求头{} \n请求体{} \n响应{} \n结果类型{} \n用时{} \nEx:{}",
                        request.getRequestURI(),
                        JSONUtils.toJSON(request.getParameterMap()),
                        JSONUtils.toJSON(headers),
                        body,
                        JSONUtils.toJSON(request.getAttribute(Constants.RESULT_KEY),""),
                        request.getAttribute(Constants.RESPONSE_TYPE_KEY),
                        String.valueOf(executionTime),ex
                );
            }
        }

        super.afterCompletion(request, response, handler, ex);
    }



    protected String requestBody(HttpServletRequest request){
        return BodyReaderHttpServletRequestWrapper.requestBody(request);
    }
    
    protected Map<String, Object> requestHeaders(HttpServletRequest request){
        return BodyReaderHttpServletRequestWrapper.requestHeaders(request);
    }    


}
