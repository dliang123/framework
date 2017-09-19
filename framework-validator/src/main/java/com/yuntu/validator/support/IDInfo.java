package com.yuntu.validator.support;


import com.yuntu.validator.utils.ValidateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Author:   林晓辉
 * Date:     14-12-26
 * Description: 身份信息
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           14-12-26           00000001         创建文件
 *
 */
public class IDInfo {

    private static final SimpleDateFormat defaultFormat=new SimpleDateFormat("yyyy-MM-dd");

    private Address address;
    private Sex sex;
    private Date birthday;
    private String idCardCode;

    private IDInfo(){

    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdCardCode() {
        return idCardCode;
    }

    public void setIdCardCode(String idCardCode) {
        this.idCardCode = idCardCode;
    }

    public static final IDInfo newInstance(String IDCardCode){
        return IDCardFactory.newInstance(IDCardCode);
    }

    @Override
    public String toString() {
        return "性别:"+sex+" 出生日期:"+ dateToString(birthday)+" 地址:"+address;
    }


    public static String dateToString(Date date) {
        return defaultFormat.format(date);
    }















    private static class IDCardFactory{

        private static final Date _19000101=new Date(-2209017600000l);
        private static final char[] ValCodeArr = {'1','0','x','9','8','7','6','5','4','3','2'};
        private static final String[] Wi = {"7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"};


        public static final IDInfo newInstance(String IDCardCode){

            if( null == IDCardCode || IDCardCode.trim().length()==0 )
                throw new IllegalArgumentException("错误的身份证号码,身份证长度只能为15或18!");

            String _IDCard= IDCardCode.trim();
            if( _IDCard.length() == 15 ){ //老号码转新号码
                _IDCard= IDCardCode.substring(0,6)+"19"+ IDCardCode.substring(6,15);
            }
            if( _IDCard.startsWith("0") || !ValidateUtils.isIn(_IDCard.length(), 17, 18) || !ValidateUtils.isDigit(_IDCard) ){
                throw new IllegalArgumentException("错误的身份证号码,身份证只能为 15数字 或18为数字 或17为数字加X 组成!");
            }

            if( _IDCard.length() == 18 ){
                int totalMulAiWi =0;
                for(int i=0 ; i<17 ; i++){
                    totalMulAiWi = totalMulAiWi + Integer.parseInt(String.valueOf(_IDCard.charAt(i))) * Integer.parseInt(Wi[i]);
                }
                if( _IDCard.charAt(17) != ValCodeArr[totalMulAiWi % 11] ){
                    throw new IllegalArgumentException("错误的身份证号码,最后一位验证码错误!");
                }
            }

            //性别
            Sex sex= Integer.valueOf(_IDCard.substring(15,17)) % 2==0
                    ? Sex.women
                    : Sex.men;

            //获取地址
            Address address= AreaCodeUtils.findAddressByCode(_IDCard.substring(0, 6));
            //获取生日
            Date birthday = stringToDate(_IDCard.substring(6, 14), "yyyyMMdd", "错误的身份证号码!出生日期错误!");
            if( birthday.after( new Date() ) ||  birthday.before( _19000101 )){
                throw new IllegalArgumentException("错误的身份证号码!出生日期应在 1900年1月1日 至 当前时间!");
            }

            IDInfo idCard= new IDInfo();
            idCard.setAddress(address);
            idCard.setBirthday(birthday);
            idCard.setIdCardCode(IDCardCode);
            idCard.setSex(sex);
            return idCard;
        }

        public static Date stringToDate(String dateString,String pattern,String message){
            try {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern);
                simpleDateFormat.setLenient(false);
                return simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                throw new IllegalArgumentException(message);
            }
        }
    }

}


