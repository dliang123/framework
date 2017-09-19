package com.yuntu.validator.support;

import com.yuntu.validator.utils.ValidateUtils;

/** 移动运营商 **/
public enum MobileOperators {

    /**
     中国移动
     2G号段（GSM网络）有134x（0-8）、135、136、137、138、139、150、151、152、158、159、182、183、184。
     3G号段（TD-SCDMA网络）有157、187、188
     3G上网卡 147
     4G号段 178
     *
     * **/
    chinaMobile(){
        final String [] chinaMobile=new String[]{ "134","135","136","137","138","139","150","151","152","157","158","159","182","183","184","187","188","147","178","1705" };
        @Override
        String[] numberSegment() {
            return chinaMobile;
        }

        @Override
        String operatorName() {
            return "中国移动";
        }
    },

    /**
     中国联通
     2G号段（GSM网络）130、131、132、155、156
     3G上网卡145
     3G号段（WCDMA网络）185、186
     4G号段 176、185
     *
     * **/
    chinaUnicom {
        final String [] chinaUnicom=new String[]{ "130","131","132","155","156","185","186","145","176","1709" };
        @Override
        String[] numberSegment() {
            return chinaUnicom;
        }

        @Override
        String operatorName() {
            return "中国联通";
        }
    },

    /**
     中国电信 （1349卫通）
     2G/3G号段（CDMA2000网络）133、153、180、181、189
     4G号段 177
     **/
    chinaTelecom {
       final String [] chinaTelecom=new String[]{ "133","153","180","181","189","177","1349","1700" };
        @Override
        String[] numberSegment() {
            return chinaTelecom;
        }

        @Override
        String operatorName() {
            return "中国电信";
        }
    };

    abstract String [] numberSegment();

    abstract String operatorName();

    public static MobileOperators findByMobileNumber(String mobile){

        if(null == mobile || mobile.trim().length()!=11 )
            return null;

        int size = ( mobile.startsWith("1349") || mobile.startsWith("170") ) ? 4 : 3;
        String prefix = mobile.substring(0,size);
        if( ValidateUtils.isIn(prefix, chinaMobile.numberSegment()) )
            return chinaMobile;
        if( ValidateUtils.isIn( prefix , chinaTelecom.numberSegment() ) )
            return chinaTelecom;
        if( ValidateUtils.isIn( prefix , chinaUnicom.numberSegment() ) )
            return chinaUnicom;
        return null;
    }
}