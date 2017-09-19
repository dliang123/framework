package com.yuntu.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by  on 2014/9/26.
 */
public class MultipartFileUtil {

    /**
     * 获取 HttpServletRequest
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 根据 HttpServletRequest 和 文件名称获取 文件列表
     * @param request   请求
     * @param fileNme   文件名称
     * @return
     * @throws Exception
     */
    public static List<MultipartFile> getFiles(HttpServletRequest request,String fileNme)  throws Exception{
        if(request instanceof MultipartHttpServletRequest){

            MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
            return multipartHttpServletRequest.getFiles(fileNme);

        }else{
            throw new Exception("不是上传模式，请检查form表头设置是否正确！");
        }

    }

    /**
     * 根据 HttpServletRequest 和 文件名称获取 文件
     * @param request   请求
     * @param fileName   文件名称
     * @return
     * @throws Exception
     */
    public static MultipartFile getFile(HttpServletRequest request,String fileName)  throws Exception{
        if(request instanceof MultipartHttpServletRequest){

            MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
            return multipartHttpServletRequest.getFile(fileName);

        }else{
            throw new Exception("不是上传模式，请检查form表头设置是否正确！");
        }
    }
}
