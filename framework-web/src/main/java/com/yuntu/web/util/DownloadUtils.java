package com.yuntu.web.util;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.yuntu.common.utils.ImgUtils;
import com.yuntu.common.utils.IoUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * Author:
 * Date:     14-11-4
 * Description: 文件下载工具类 支持多浏览器[chrome,firefox,ie,safari]中文与空格 {
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-4           00000001         创建文件
 *
 */
public class DownloadUtils {


    /***
     * 常用下载头   application/x-msdownload,application/octet-stream,application/x-msdownload ,application/force-download
     *
     * @param file
     * @param response
     * @return
     */
    public static HttpServletResponse download(File file,HttpServletRequest request, HttpServletResponse response) {

        try {
            response.addHeader("Content-Length", ""+file.length());
            download( new FileInputStream(file), file.getName() ,request, response );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpServletResponse download(InputStream inputStream,String fileName,HttpServletRequest request,HttpServletResponse response) {

        return download(inputStream ,fileName, request, response ,null);
    }


    public static HttpServletResponse download(InputStream inputStream,String fileName,HttpServletRequest request,HttpServletResponse response , String contentType) {

        try {


            if( (  !Strings.isNullOrEmpty(contentType) && contentType.contains("image") ) || ImgUtils.isImage(inputStream, false)  ){ //图片
                IoUtils.copy(inputStream, response.getOutputStream());
                return response;
            }

            String charset = "UTF-8";
            String userAgent = request.getHeader("user-Agent");
            if( null !=userAgent && ( userAgent.toUpperCase().contains("MSIE") || userAgent.toUpperCase().contains("LIKE GECKO") ) ){ //IE 浏览器使用 GBK 编码
                charset = "GBK";
            }
            String displayFileName = new String(fileName.getBytes(charset), "ISO-8859-1") ;

            // application/force-download 响应头如果浏览器安装有插件会提示是否通过插件打开( 比如 IE 的 excel )
            response.setContentType("application/force-download");
            response.addHeader("Content-Type","application/force-download");
            response.addHeader("Content-Disposition",  String.format("attachment; filename=\"%s\"", displayFileName));

            IoUtils.copy(inputStream,response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Closeables.closeQuietly( inputStream );
        }
        return response;
    }
}
