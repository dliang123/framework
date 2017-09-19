package com.yuntu.validator.support;

import com.yuntu.validator.utils.ValidateUtils;

/*
 * Author:   林晓辉
 * Date:     14-12-26
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-12-26           00000001         创建文件
 *
 */
public class Address {

    private String province;
    private String city;
    private String county;
    private String code;

    public Address(String code) {

        if( null==code || code.trim().length()!=6 || !ValidateUtils.isDigit(code.trim())  )
            throw new IllegalArgumentException("错误的区域编码,区域编码为6位整数");

        this.code = code;
    }

    public String getProvince() {
        return province;
    }

    public String getProvinceCode() {
        return padEnd(code.substring(0, 2), 6, '0');
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return padEnd(code.substring(0, 4), 6, '0');
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return province+city+county;
    }

    private String padEnd(String string, int minLength, char padChar) {
//
        checkNotNull(string);

        if (string.length() >= minLength) {
            return string;
        }
        StringBuilder sb = new StringBuilder(minLength);
        sb.append(string);
        for (int i = string.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    public  <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
