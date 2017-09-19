package com.yuntu.common.http;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.base.Objects;
import com.yuntu.common.utils.AssertUtils;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.common.utils.StringUtils;

/**
 * Created by linxiaohui on 15/11/11.
 */
public class Response {

    private int statusCode;

    private String responseBody;

    Response(){

    }

    Response(int statusCode,String responseBody){
        this.statusCode = statusCode;
        this.responseBody = StringUtils.unicodeToString(responseBody);
    }


    public int getStatusCode() {
        return statusCode;
    }


    public String getResponseBody() {
        return responseBody;
    }


    public <T extends Serializable> T convert(Class<T> resultClass) throws IOException {

        AssertUtils.notNull(resultClass,"参数 resultClass 不能为空!");
        return JSONUtils.toObject(responseBody,resultClass);
    }
    
    public <T extends Serializable> T convert(Class<T> resultClass,PropertyNamingStrategy pse) throws IOException {
        AssertUtils.notNull(resultClass,"参数 resultClass 不能为空!");
        return JSONUtils.toObject(responseBody,resultClass,pse);
    }    
    
    public <T extends Serializable> T convert(TypeReference<T> typeReference,PropertyNamingStrategy pse) throws IOException {
        AssertUtils.notNull(typeReference,"参数 typeReference 不能为空!");
        return JSONUtils.toObject(responseBody,typeReference,pse);
    }      
    
    public <T extends Serializable> T convert(Class<T> resultClass,String key) throws IOException {

        AssertUtils.notNull(resultClass,"参数 resultClass 不能为空!");
        AssertUtils.notNull(resultClass,"参数 key 不能为空!");

        JsonNode jsonNode = JSONUtils.toJSON(responseBody);
        return JSONUtils.toObject( keyNodeJson(jsonNode,key) ,resultClass);
    }

    public <T extends Serializable> List<T> convertToList(Class<T> resultClass) throws IOException {

        AssertUtils.notNull(resultClass,"参数 resultClass 不能为空!");
        return JSONUtils.toList(responseBody, resultClass);
    }

    public <T extends Serializable> List<T> convertToList(Class<T> resultClass,String key) throws IOException {

        AssertUtils.notNull(resultClass,"参数 resultClass 不能为空!");
        AssertUtils.notNull(resultClass,"参数 key 不能为空!");
        JsonNode jsonNode = JSONUtils.toJSON(responseBody);
        return JSONUtils.toList(keyNodeJson(jsonNode,key), resultClass);
    }

    private JsonNode keyNode(JsonNode jsonNode,String key){
        JsonNode keyNode = jsonNode.path(key);
        if( null != keyNode ){
            keyNode = jsonNode.get(key);
        }
        return keyNode;
    }

    private String keyNodeJson(JsonNode jsonNode,String key){
        JsonNode keyNode = keyNode(jsonNode,key);
        String json = keyNode.textValue();
        if( null == json || Objects.equal("null",json))
            json = keyNode.toString();
        return json;
    }
}
