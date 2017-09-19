package com.yuntu.validator.constraints.zh_CN;


import com.yuntu.validator.support.MobileOperators;

import javax.validation.Payload;

/*
 * Author:
 * Date:     15-1-21
 * Description: 手机号码验证
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            15-1-21           00000001         创建文件
 *
 */
@java.lang.annotation.Documented
@javax.validation.Constraint(validatedBy = {PhoneNumberValidator.class})
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.ReportAsSingleViolation
public @interface PhoneNumber {

    String message() default "不是一个符合要求的手机号码!";

    MobileOperators [] operators() default {MobileOperators.chinaUnicom,MobileOperators.chinaMobile,MobileOperators.chinaTelecom};

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}