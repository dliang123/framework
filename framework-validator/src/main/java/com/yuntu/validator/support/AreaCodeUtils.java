package com.yuntu.validator.support;

import com.yuntu.base.utils.ResourceUtils;

import java.util.Locale;
import java.util.ResourceBundle;

/*
 * Author:   林晓辉
 * Date:     14-12-26
 * Description: 根据区域编码获取地址
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-12-26           00000001         创建文件
 *
 */
public class AreaCodeUtils {

    private static ResourceBundle areaCode = null;
    private static ResourceBundle areaCode(){

        if( null == areaCode  ){
            synchronized (IDInfo.class){
                if( null == areaCode ){
                    areaCode = ResourceUtils.newBundle("GB", Locale.getDefault(), IDInfo.class.getClassLoader());
                }
            }
        }
        return areaCode;
    }

    public static boolean exist(String code){
        return areaCode().containsKey(code.trim());
    }

    public static Address findAddressByCode(String code){

        Address address = new Address(code);
        address.setProvince(findNameByCode(address.getProvinceCode()));
        address.setCity(findNameByCode(address.getCityCode()));
        address.setCounty(findNameByCode(address.getCode()));
        return address;
    }

    private static String findNameByCode(String code){

        String areaCode=areaCode().getString( code );
        return null == areaCode ? "" : areaCode;
    }
}
