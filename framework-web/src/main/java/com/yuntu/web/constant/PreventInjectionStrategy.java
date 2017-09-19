package com.yuntu.web.constant;

public enum PreventInjectionStrategy{
    /** 遇到可能sql注入的参数时 抛出异常 **/
    throwException
    /** 遇到可能sql注入的参数时 删除关键字 **/
    ,replace
    /** 遇到可能sql注入的参数时 不进行任何操作 **/
    ,none
}