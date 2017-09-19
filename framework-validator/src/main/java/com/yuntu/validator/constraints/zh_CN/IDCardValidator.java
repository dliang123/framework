package com.yuntu.validator.constraints.zh_CN;

import com.yuntu.validator.support.IDInfo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*
 * Author:
 * Date:     15-1-23
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            15-1-23           00000001         创建文件
 *
 */
public class IDCardValidator implements ConstraintValidator<IDCard, String> {

    public void initialize(IDCard constraintAnnotation) {

    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        try{
            return  null != IDInfo.newInstance(value);
        }catch (Exception e){
            return false;
        }
    }

    public static boolean validate(String value){
        return  null != IDInfo.newInstance(value);
    }
}
