package com.yuntu.common.utils;

import com.google.common.base.Joiner;
import com.google.common.io.*;

import java.io.*;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

/*
 * Copyright (C), 2012-2014
 * Author:
 * Date:     14-11-4
 * Description: io 操作工具类
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-4           00000001         创建文件
 *
 */
public class IoUtils {

    /**
     * 把输入流中内容copy到输出流中 [ 不会关闭输出流 ]
     *
     * @param from          文件来源
     * @param to            文件去处
     * @throws IOException
     */
    public static long copy(InputStream from, OutputStream to) throws IOException {

        return ByteStreams.copy(from,to);
    }

    /**
     * 把输入流中内容copy到输出流中 [ 不会关闭输出流 ]
     * @param from          文件来源
     * @param to            文件去处
     * @throws IOException
     */
    public static long copy(ReadableByteChannel from,WritableByteChannel to) throws IOException {

        return ByteStreams.copy(from, to);
    }


    /**
     * 把输入流中内容copy到输出流中 [ 不会关闭输出流 ]
     *
     * @param from          文件来源
     * @param to            文件去处
     * @throws IOException
     */
    public static long copy(Readable from, Appendable to) throws IOException {

        return CharStreams.copy(from , to);
    }

    /**
     * 把输入流中内容copy到输出流中
     * @param from          文件来源
     * @param to            文件去处
     * @throws IOException
     */
    public static void copy(File from,File to) throws IOException {

        Files.copy(from,to);
    }


    /**
     * 把输入流中内容copy到输出流中 [ 不会关闭输出流 ]
     * @param from          文件来源
     * @param to            文件去处
     * @throws IOException
     */
    public static void copy(File from,OutputStream to) throws IOException {

        Files.copy(from,to);
    }

    /**
     * 文件拷贝(追加)
     * @param from      文件来源
     * @param charset   编码
     * @param to        文件去处
     * @throws IOException
     */
    public static void copy(File from, Charset charset, Appendable to) throws IOException {

        Files.copy(from, charset, to);
    }


    /**
     * 读取文件
     * @param readable
     * @return
     * @throws IOException
     */
    public static java.util.List<String> readLines(Readable readable) throws IOException {

        return CharStreams.readLines(readable);
    }

    /**
     * 读取文件
     * @param readable
     * @param processor
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T readLines(Readable readable, LineProcessor<T> processor) throws IOException {

        return CharStreams.readLines(readable,processor);
    }

    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte [] toByteArray(InputStream inputStream) throws IOException {

        return ByteStreams.toByteArray(inputStream);
    }

    public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {

        return ByteStreams.readBytes( input,processor );
    }

    /**
     * 静默关闭
     * @param closeableArray
     */
    public static void closeQuietly(Closeable ... closeableArray){

        if( null != closeableArray && closeableArray.length>0 ){
            for(Closeable closeable : closeableArray){
                closeQuietly(closeable);
            }
        }
    }

    /**
     * 静默关闭
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable){

        if(  null == closeable )
            return;

        if(  closeable instanceof Flushable ){
            Flushables.flushQuietly((Flushable)closeable);
        }

        try {
            closeable.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 重置流
     * @param inputStream
     * @return
     */
    public static boolean reset(InputStream inputStream){

        if( null == inputStream  )
            return false;
        try {
            inputStream.reset();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 重置流
     * @param inputStream
     * @return
     */
    public static boolean reset(Reader inputStream){

        if( null == inputStream  )
            return false;
        try {
            inputStream.reset();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 关闭或重置流
     * @param inputStream
     * @param isClose
     */
    public static void closeOrReset(InputStream inputStream,boolean isClose){
        if( null != inputStream ){
            if( isClose )
                closeQuietly(inputStream);
            else
                reset(inputStream);
        }
    }

    public static String readFully(Reader reader){
        try {
            return Joiner.on("").join(readLines(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readFully(InputStream inputStream){

        try {
            return new String(ByteStreams.toByteArray(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

//
//
//
//    /**
//     * 打开资源文件
//     * @param loader
//     * @param resourceName
//     * @param reload
//     * @return
//     * @throws PrivilegedActionException
//     */
//    public static InputStream openResource(final ClassLoader loader,final String resourceName,final boolean reload) throws PrivilegedActionException {
//
//       return ResourceUtils.openResource(loader, resourceName, reload);
//    }
//
//    /**
//     * 打开资源文件
//     * @param loader
//     * @param resourceName
//     * @param reload
//     * @param charset
//     * @return
//     * @throws PrivilegedActionException
//     * @throws UnsupportedEncodingException
//     */
//    public static InputStreamReader openResourceReader(final ClassLoader loader,final String resourceName,final boolean reload,String charset) throws PrivilegedActionException, UnsupportedEncodingException {
//
//        return ResourceUtils.openResourceReader(loader, resourceName, reload, charset);
//    }
}
