package com.yuntu.validator.constraints.zh_CN;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/*
 * Author:   林晓辉
 * Date:     15-1-22
 * Description: 身份证验证
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * 林晓辉           15-1-22           00000001         创建文件
 *
 */
@Documented
@javax.validation.Constraint(validatedBy = {})
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@javax.validation.ReportAsSingleViolation
public @interface IDCard {

    String message() default "不是一个真实的身份证号码";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
