package com.yuntu.web.resolver;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.yuntu.web.listener.FileUploadListener;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/*
 * Author:
 * Date:     14-11-27
 * Description: 文件上传组件
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            14-11-27           00000001         创建文件
 *
 */
public class MultipartResolver extends CommonsMultipartResolver {

    public static final String UPLOAD_KEY ="uploadId";
    public static final String DEFAULT_UPLOAD_ID = "DEFAULT_UPLOAD_ID";
    public static final String IS_NATIVE_SUFFIX="IS_NATIVE";
    public static final String IS_UPLOADING_SUFFIX="IS_UPLOADING";
    private static final java.util.Map<String,FileUpload> FILE_UPLOAD_MAP = Maps.newConcurrentMap();

    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        try {
            beforeParse(request,fileUpload);
            java.util.List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return parseFileItems(fileItems, encoding);
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }finally {
            afterParse(request, fileUpload);
        }
    }

    private void beforeParse(HttpServletRequest request,FileUpload fileUpload){

        //获取uploadId
        String id = getUploadId(request);
        //保存上传组件到本服务器的 map
        FILE_UPLOAD_MAP.put(id, fileUpload);
        //设置正在上传中
        request.getSession().setAttribute(id+IS_UPLOADING_SUFFIX,true);
        //设置此服务器为上传服务器
        request.getSession().setAttribute(id+IS_NATIVE_SUFFIX,true);
        if( fileUpload.getProgressListener() instanceof FileUploadListener){
            ( (FileUploadListener)fileUpload.getProgressListener() ).reset();
        }

    }

    /**
     * 是否正在上传中
     * @param request
     * @return
     */
    public static boolean isUploading(HttpServletRequest request){
        return null != request.getSession().getAttribute( getUploadId(request) +IS_UPLOADING_SUFFIX);
    }

    /**
     * 接收文件完成
     * @param request
     * @param fileUpload
     */
    private void afterParse(HttpServletRequest request,FileUpload fileUpload){

        //获取上传id
        String id = getUploadId(request);
        //移除正在上传中状体
        request.getSession().removeAttribute(id+IS_UPLOADING_SUFFIX);
        //移除本地服务器状态
        request.getSession().removeAttribute(id+IS_NATIVE_SUFFIX);
        //移除上传组件
        FILE_UPLOAD_MAP.remove(id);
        ProgressListener progressListener=fileUpload.getProgressListener();
        if( progressListener instanceof FileUploadListener){
            (  (FileUploadListener) progressListener ).complete();
        }
    }

    /**
     * 获取上传文件请求id
     * @param request
     * @return
     */
    private static String getUploadId(HttpServletRequest request){
        String uploadId=request.getParameter(UPLOAD_KEY);
        return Strings.isNullOrEmpty(uploadId) ? DEFAULT_UPLOAD_ID :uploadId;
    }

    /**
     * 获取上传组件
     * @param request
     * @return
     */
    private static FileUpload getFileUpload(HttpServletRequest request){
        return FILE_UPLOAD_MAP.get(getUploadId(request));
    }

    /**
     * 获取上传文件的侦听
     * @param request
     * @param defaultListener 如果侦听不存在,使用的默认侦听器
     * @return
     */
    public static FileUploadListener findFileUploadProgressListener(HttpServletRequest request,FileUploadListener defaultListener){

        FileUpload fileUpload = getFileUpload(request);

        if( null == fileUpload  )
            return null;
        ProgressListener progressListener = fileUpload.getProgressListener();
        if( null == progressListener || !(progressListener instanceof FileUploadListener)){
            fileUpload.setProgressListener(defaultListener);
            progressListener = defaultListener;
        }
        return (FileUploadListener)progressListener;
    }

    /**
     * 注册侦听器
     * @param request
     * @param listener
     */
    public static void register(HttpServletRequest request,FileUploadListener listener){
        FileUpload fileUpload = getFileUpload(request);
        if( null != fileUpload  ){
            fileUpload.setProgressListener(listener);
        }
    }

    /**
     * 是否为上传文件的原始服务器
     * @param request
     * @return
     */
    public static boolean isNative(HttpServletRequest request){
        return null != request.getSession().getAttribute( getUploadId(request)+IS_NATIVE_SUFFIX);
    }
}