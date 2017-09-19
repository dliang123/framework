package com.yuntu.validator.constraints.zh_CN;


import com.yuntu.validator.support.MobileOperators;
import com.yuntu.validator.utils.ValidateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/*
* Author:   林晓辉
* Date:     15-1-21
* Description: 模块目的、功能描述
* History: 变更记录
* <author>           <time>             <version>        <desc>
* 林晓辉           15-1-21           00000001         创建文件
*
*/
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    public static final Pattern MOBILE_PATTERN= Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[^4,\\D])|(170)|(17[6-8])|(14[5,7]))\\d{8}$");
    private MobileOperators[] mobileOperators;


    public void initialize(PhoneNumber constraintAnnotation) {
        mobileOperators = constraintAnnotation.operators();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return MOBILE_PATTERN.matcher(value).matches() && ValidateUtils.isIn(MobileOperators.findByMobileNumber(value), mobileOperators);
    }

    public static boolean validate(String string){
        return MOBILE_PATTERN.matcher(string).matches();
    }
}
