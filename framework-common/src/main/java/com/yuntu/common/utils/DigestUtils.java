package com.yuntu.common.utils;



import java.security.MessageDigest;

/**
 * 加密工具
 * Created by 林晓辉 on 2015/5/1.
 */
public class DigestUtils {

    public enum Digest{
        MD5_16("MD5",(short)16),
        MD5_32("MD5",(short)32);

        private Digest(String name,Short length){
            this.name = name;
            this.length = length;
        }
        private String name;
        private short length;
    }


    public static String bytes2HexString(byte b[]) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            result.append(byte2HexString(b[i]));
        }
        return result.toString();
    }

    private static String byte2HexString(byte b) {
        int n = b<0 ? b+256 : b;
        int d1 = n / 16 , d2 = n % 16;
        return Integer.toHexString(d1) + Integer.toHexString(d2);
    }

    public static String digest(String str,Digest digest) {
        return digest(str,null,digest);
    }

    public static String digest(byte bytes[],Digest digest) {
        try {
            MessageDigest md = MessageDigest.getInstance(digest.name);
            String digestString = bytes2HexString(md.digest(bytes));
            switch ( digest.length ){
                case 16: return digestString.substring(8,24);
                case 32: return digestString;
                default: return digestString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    public static String digest(String str,String charsetName,Digest digest) {
        try {
            byte bytes[] =  !StringUtils.isNullOrEmpty(charsetName) ? str.getBytes(charsetName) : str.getBytes();
            return digest(bytes,digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

}