package com.yuntu.web;

/**
 * Created by linxiaohui on 15/11/12.
 */
public class Constants {

    /** 运行异常信息在 REQUEST 中的 key **/
    public static final String EXCEPTION_KEY = "_EXCEPTION";

    /** 运行结果在 REQUEST 中的 key **/
    public static final String RESULT_KEY = "_RESULT";

    /** 运行结果缓存 REQUEST 中的 key **/
    public static final String RESULT_CACHE_DOMAIN = "RESULT:CACHE";

    /** 运行结果处理类型在 REQUEST中的 key **/
    public static final String RESULT_TYPE_KEY = "_RESULT_TYPE";
    
    /** 运行结果响应类型在 REQUEST中的 key  **/
    public static final String RESPONSE_TYPE_KEY = "_RESPONSE_TYPE";

    /** requestHeader 在request中的key **/
    public static final String REQUEST_HEADERS_KEY = "_REQUEST_HEADERS";
    
    /** requestBody 在 request 中的 key **/
    public static final String REQUEST_BODY_KEY = "_REQUEST_BODY";

    /** 运行结果在 REQUEST 中的 key(可能带封包) **/
    public static final String RESULT_FINAL_KEY = "_RESULT_PACKAGE";
}