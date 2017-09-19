package com.yuntu.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/*
 * Author:
 * Date:     14-12-26
 * Description: 资源文件加载工具类
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            14-12-26           00000001         创建文件
 *
 */
public class ResourceUtils {

    private static final String defaultCharset="UTF-8";
    private static final ResourceBundle.Control control = new Control(defaultCharset);


    public static ResourceBundle newBundle(String baseName){
        return newBundle(baseName, Locale.getDefault());
    }

    public static ResourceBundle newBundle(String baseName,Locale locale){
        return newBundle(baseName, locale, ResourceUtils.class.getClassLoader());
    }

    public static ResourceBundle newBundle(String baseName,Locale locale,ClassLoader classLoader){
        return newBundle(baseName, locale, classLoader, control );
    }

    public static ResourceBundle newBundle(String baseName,Locale locale,ClassLoader classLoader,String charset){

        return newBundle(baseName, locale, classLoader, defaultCharset.equalsIgnoreCase(charset) ? control : new Control(charset) );
    }

    public static ResourceBundle newBundle(String baseName,Locale locale,ClassLoader classLoader,ResourceBundle.Control control){
        return ResourceBundle.getBundle(baseName, locale, classLoader, control);
    }



    private static class Control extends ResourceBundle.Control{

        private String charsetName;

        public Control(String charsetName){
            this.charsetName = charsetName;
        }

        public ResourceBundle newBundle(String baseName, Locale locale, String format,final ClassLoader loader,final boolean reload )
                throws IllegalAccessException, InstantiationException, IOException {

            if ( !format.equals("java.properties") ){
                return super.newBundle(baseName, locale, format, loader, reload);
            }

            InputStreamReader stream = null;
            try {
                String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
                stream = openResourceReader(loader,resourceName,reload,charsetName);
                return new PropertyResourceBundle(stream);
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            } finally {
                try{
                    if( null != stream )
                        stream.close();
                }catch (Exception e){};
            }
        }
    }


    public static InputStream openResource(final ClassLoader loader,final String resourceName,final boolean reload) throws PrivilegedActionException {

        return AccessController.doPrivileged(
                new PrivilegedExceptionAction<InputStream>() {
                    public InputStream run() throws IOException {
                        InputStream is = null;
                        if (reload) {
                            URL url = loader.getResource(resourceName);
                            if (url != null) {
                                URLConnection connection = url.openConnection();
                                if (connection != null) {
                                    connection.setUseCaches(false);
                                    is = connection.getInputStream();
                                }
                            }
                        } else {
                            is = loader.getResourceAsStream(resourceName);
                        }
                        return is;
                    }
                });
    }

    /**
     * 打开资源文件
     * @param loader
     * @param resourceName
     * @param reload
     * @param charset
     * @return
     * @throws PrivilegedActionException
     * @throws UnsupportedEncodingException
     */
    public static InputStreamReader openResourceReader(final ClassLoader loader,final String resourceName,final boolean reload,String charset) throws PrivilegedActionException, UnsupportedEncodingException {

        return new InputStreamReader(openResource(loader, resourceName, reload),charset);
    }
}