package com.yuntu.web.util;

import com.google.common.base.Strings;
import com.yuntu.common.utils.ThreadUtils;
import com.yuntu.web.listener.FileUploadListener;
import com.yuntu.web.resolver.MultipartResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Author:
 * Date:     14-11-28
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            14-11-28           00000001         创建文件
 *
 */
public class WebUtils {

    private static final long interval = 200l;
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);


    /**
     * 上传处理
     * @param request   上传的请求
     * @param response  上传的响应
     * @param callback  上传的回调函数
     */
    public static void fileUploadProgress(HttpServletRequest request,HttpServletResponse response,String callback)  {

        fileUploadProgress(request, response, callback,WebUtils.interval);
    }

    public static void fileUploadProgress(HttpServletRequest request,HttpServletResponse response,String callback,long interval){

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter printWriter =null ;
        try {
            response.reset();
            printWriter=response.getWriter();
        } catch (IOException e) {
            logger.warn("",e);
        }

        //默认脚本(显示完成100%)
        String script="<script> try{ " + callback + "(1,1,0) }catch(e){ console.log(e) } </script>";

        if( MultipartResolver.isUploading(request) ){//是否是在上传中

            if( !MultipartResolver.isNative(request) ){ //不是原始上传文件的服务器
                try {
                    //重定向到相同URL(  去其他服务器节点获取上传进度 )
                    response.sendRedirect(request.getRequestURL().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //获取侦听器
            FileUploadListener listener = MultipartResolver.findFileUploadProgressListener(request, new FileUploadListener());

            if( null != listener ){//如果 listener 不存在,说明以完成上传(二次检测是否完成上传)

                while (!listener.isComplete()){
                    script = "<script> try{ " + callback + "(" + listener.getBytesRead() + "," + listener.getContentLength() + "," + listener.getItems() + ") }catch(e){ console.log(e) } </script>";
                    printWriter.println(script);
                    printWriter.flush();
                    ThreadUtils.sleep(interval);
                }
                script = "<script> try{ " + callback + "(" + listener.getContentLength() + "," + listener.getContentLength() + "," + listener.getItems() + ") }catch(e){ console.log(e) } </script>";
            }
        }

        printWriter.println(script);
        printWriter.flush();
    }

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if( null != requestAttributes && null != requestAttributes.getRequest()){
            return requestAttributes.getRequest();
        }
        return (HttpServletRequest)HolderUtils.getRequest();
    }

    public static HttpSession getSession(){

        HttpServletRequest request =getRequest();
        if( null != request )
            return getRequest().getSession();
        return null;
    }

    public static Object getSessionAttribute(String key){
        HttpSession session = getSession();
        if( null != session )
            return  session.getAttribute(key);
        return null;
    }



    public static String getParameter(ServletRequest servletRequest,String paramName,String defaultValue){
        String value=servletRequest.getParameter(paramName);
        if( Strings.isNullOrEmpty(value) )
            return defaultValue;
        else
            return value;
    }

    public static Cookie findCookie(String cookieName){
        return org.springframework.web.util.WebUtils.getCookie(getRequest(), cookieName);

    }

    public static String findHeader(String header){
        return getRequest().getHeader(header);
    }

    public static String findCookValue(String cookieName){
        Cookie cookie = findCookie(cookieName);
        if( null != cookie ){
            return cookie.getValue();
        }
        return "";
    }


    public static String findRemoteAddr(){

        HttpServletRequest request =getRequest();

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static void noCache(HttpServletResponse response){

        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
        }catch (Exception e){
            logger.warn("",e);
        }
    }

}
