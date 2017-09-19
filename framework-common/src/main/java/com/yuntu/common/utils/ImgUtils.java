package com.yuntu.common.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.yuntu.common.img.CompressConfig;
import com.yuntu.common.img.Size;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 图片处理相关工具类
 */
@SuppressWarnings("restriction")
public class ImgUtils {


    /**
     * 图片压缩成字节
     * @param img               要压缩的图片
     * @param compressConfig  压缩配置
     * @return
     */
    public byte [] compress(BufferedImage img,CompressConfig compressConfig) {

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        compress(img, compressConfig,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    /**
     * 图片压缩
     * @param img               要压缩的图片
     * @param compressConfig   压缩配置
     * @param outputStream     输出到哪里
     */
    public static void compress(BufferedImage img,CompressConfig compressConfig,OutputStream outputStream) {

        try {
            if (null== img || img.getWidth(null) == -1) {
                return;
            }

            Size size= compressConfig.getSize();
            int oldWidth=img.getWidth(null);
            int oldHeight = img.getHeight(null);
            int width = size.getWidth( oldWidth,oldHeight );
            int height = size.getHeight( oldWidth,oldHeight );

            BufferedImage tag = new BufferedImage( width,  height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(img.getScaledInstance(width, height, compressConfig.getAlgorithm().value()), 0, 0, null);
            JPEGEncodeParam param=JPEGCodec.getDefaultJPEGEncodeParam(tag);
            param.setQuality(compressConfig.getQuality(),false);
            JPEGImageEncoder encoder=JPEGCodec.createJPEGEncoder(outputStream,param);
            encoder.encode(tag);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            IoUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 获取图片类型
     * @param inputStream   图片流
     * @param autoClose     是否自动关闭流
     * @return
     */
    public static String getImageFormatName(InputStream inputStream,boolean autoClose) {
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(inputStream);
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
            if (!imageReaders.hasNext()) {
                return null;
            }
            return imageReaders.next().getFormatName();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IoUtils.closeQuietly(iis);
            IoUtils.closeOrReset(inputStream,autoClose);
        }
        return null;
    }

    /**
     * 判断是否是图片
     * @param inputStream   图片流
     * @return
     */
    public static boolean isImage(InputStream inputStream,boolean autoClose){

        try {
            return null != ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            IoUtils.closeOrReset(inputStream,autoClose);
        }
    }

    /**
     * 给图片转换格式
     * @param image       图片的流
     * @param format      要转换成的格式
     * @param os          输出的流
     * @return
     * @throws IOException
     */
    public static void converter(BufferedImage image,String format, OutputStream os)
            throws IOException{
        //此方法以关闭 input 与 output
        ImageIO.write(image, format, os);
    }

    /**
     * 给图片转换图片
     * @param image         图片
     * @param format        输出后的格式(.jpg .png 等)
     * @param formatFile    输出到那个文件
     * @return
     * @throws IOException
     */
    public static File converter(BufferedImage image, String format, File formatFile)
            throws IOException{
        ImageIO.write(image, format, formatFile);
        return formatFile;
    }

    public static byte[] converter(BufferedImage image,String format){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            converter(image, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    /**
     * 读取图片
     * @param inputStream   图片的流
     * @return
     * @throws IOException
     */
    public static BufferedImage read(InputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }

    /**
     * 读取图片
     * @param file  图片文件
     * @return
     * @throws IOException
     */
    public static BufferedImage read(File file) throws IOException {
        return ImageIO.read(file);
    }

    /**
     * 读取图片
     * @param url   图片的 url
     * @return
     * @throws IOException
     */
    public static BufferedImage read(URL url) throws IOException {
        return ImageIO.read(url);
    }

    /**
     * 读取图片
     * @param imageInputStream  图片流
     * @return
     * @throws IOException
     */
    public static BufferedImage read(ImageInputStream imageInputStream) throws IOException {
        return ImageIO.read(imageInputStream);
    }




    private static final Pattern pattern= Pattern.compile("data:(\\S*);base64,");

	public static Img base64ToImg(String base64Data) throws IOException {

        String contentType= findContentType(base64Data);
        if( !contentType.contains("image") ){
            throw new IllegalArgumentException("不是一个图片!");
        }
        BASE64Decoder base64Decoder=new BASE64Decoder();
        byte [] data=base64Decoder.decodeBuffer(base64Data.replaceFirst(pattern.pattern(),""));
        String md5=new String(DigestUtils.digest(data, DigestUtils.Digest.MD5_32));
        return new Img(contentType,data,md5);
    }

    public static String findContentType(String base64Data){
        Matcher matcher=pattern.matcher(base64Data);
        matcher.find();
        return matcher.group(1);
    }

    public static class Img{
        private final String type;
        private final byte [] data;
        private final String md5;

        Img(String type,byte [] data,String md5){
            this.data = data;
            this.type = type;
            this.md5 = md5;
        }

        public String getType() {
            return type;
        }

        public byte[] getData() {
            return data;
        }

        public String getMd5() {
            return md5;
        }
    }
}
