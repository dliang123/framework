package com.yuntu.validator.constraints;

import javax.validation.Payload;

import com.yuntu.validator.constraints.InternalNetworkValidator;

/*
 * Author:
 * Date:     15-1-22
 * Description: 是否是内部网络
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            15-1-22           00000001         创建文件
 *
 */
@java.lang.annotation.Documented
@javax.validation.Constraint(validatedBy = {InternalNetworkValidator.class})
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.ReportAsSingleViolation
//@javax.validation.constraints.Pattern(regexp = "(127[.]0[.]0[.]1)|(localhost)|(10[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3})|(172[.]((1[6-9])|(2\\d)|(3[01]))[.]\\d{1,3}[.]\\d{1,3})|(192[.]168[.]\\d{1,3}[.]\\d{1,3})")
public @interface InternalNetwork {

    String message() default "不是一个内部网络地址";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
