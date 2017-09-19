package com.yuntu.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/*
 * Author:
 * Date:     15-1-23
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           15-1-23           00000001         创建文件
 *
 */
public class InternalNetworkValidator implements ConstraintValidator<InternalNetwork, String> {

    public static final Pattern pattern=Pattern.compile( "(127[.]0[.]0[.]1)|(localhost)|(10[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3})|(172[.]((1[6-9])|(2\\d)|(3[01]))[.]\\d{1,3}[.]\\d{1,3})|(192[.]168[.]\\d{1,3}[.]\\d{1,3})");

    public void initialize(InternalNetwork constraintAnnotation) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }

    public static boolean validate(String input){
        return pattern.matcher(input).matches();
    }
}
