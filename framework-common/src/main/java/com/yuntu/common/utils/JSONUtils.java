package com.yuntu.common.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.*;

import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 *  15/11/12.
 */
public class JSONUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JSONUtils.class);

    static {
    	objectMapper.setSerializationInclusion(Include.NON_NULL);
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper objectMapperCopy(){
        return objectMapper.copy();
    }
    
    public static ObjectMapper getObjectMapper() {
    	return objectMapper;
    }

    public static String toJSON(Object obj) throws JsonProcessingException {
    	
        return objectMapper.writeValueAsString(obj);
    }
    
    public static String toJSON(Object obj,String defaultValue)  {

    	if( null == obj ) 
    		return null;
    	try {
            return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			if( logger.isDebugEnabled() ){ logger.debug("",e); }
			return defaultValue;
		}
    }

    public static <T extends Serializable> T toObject(String json,Class<T> resultClass) throws IOException {
    	
        return objectMapper.readValue(json,resultClass);
    }
    
    public static <T extends Serializable> T toObject(String json,Class<T> resultClass,PropertyNamingStrategy pns) throws IOException {
		if(null != pns){
			ObjectMapper objectMapperCopy = objectMapper.copy();
			objectMapperCopy.setPropertyNamingStrategy(pns);
			return objectMapperCopy.readValue(json,resultClass);
		}
		
		return objectMapper.readValue(json,resultClass);
    }     
    
    public static <T extends Serializable> T toObject(String json,TypeReference<T> typeReference,PropertyNamingStrategy pns) throws IOException {
		if(null != pns){
			ObjectMapper objectMapperCopy = objectMapper.copy();
			objectMapperCopy.setPropertyNamingStrategy(pns);
			return objectMapperCopy.readValue(json,typeReference);
		}
		
		return objectMapper.readValue(json,typeReference);
    }    

    public static JsonNode toJSON(String json) throws IOException {

        return objectMapper.readTree(json);

    }
    
    @SuppressWarnings("unchecked")
	public static <T> Map<String, T> json2map(String jsonStr) throws JsonParseException, JsonMappingException, IOException{
    	return objectMapper.readValue(jsonStr, Map.class);
	}      

    public static <T extends Serializable> List<T> toList(String json,Class<T> resultClass) throws IOException {

        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(List.class, List.class,resultClass);
        return objectMapper.readValue(json, javaType);

    }
}
