package com.yuntu.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  15/10/30.
 */
public class MapUtils {

    private Map<String,Object> source=null;

    public MapUtils(Map<String, Object> source){
        this.source=source;
    }

    public Map<String, Object> get() {
        return source;
    }

    private MapUtils setSource(Map<String, Object> source) {
        this.source = source;
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static MapUtils pojoToMap(Object pojo){

        MapUtils convert = new MapUtils(null);
        if( null == pojo ){
            return convert;
        }

        if( pojo instanceof Map){
            return new MapUtils( Maps.newLinkedHashMap((Map) pojo) );
        }

        try {
            Map<String, Object> m = PropertyUtils.describe(pojo);
            LinkedHashMap<String,Object> result= Maps.newLinkedHashMap(m);
            return convert.setSource( result ).ignores("class");
        } catch (Exception e) {
            e.printStackTrace();
            return convert;
        }

    }

    public MapUtils ignores(String ... ignores){

        if( null == source ){
            return this;
        }
        for(String ignore : ignores){
            source.remove(ignore);
        }
        return this;

    }


    public MapUtils dateToSqlDateTime(){
        if( null == source ){
            return this;
        }
        Map<String,Object> tempMap = Maps.newHashMap();

        for(Map.Entry<String,Object> entry : source.entrySet()){
            if( entry.getValue() instanceof Date){
                try {
                    tempMap.put(entry.getKey(),new java.sql.Timestamp(((Date) entry.getValue()).getTime()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        source.putAll(tempMap);
        return this;

    }

    public MapUtils dateToSqlDate(){
        if( null == source ){
            return this;
        }
        Map<String,Object> tempMap = Maps.newHashMap();

        for(Map.Entry<String,Object> entry : source.entrySet()){
            if( entry.getValue() instanceof Date){
                try {
                    tempMap.put(entry.getKey(),new java.sql.Date(((Date) entry.getValue()).getTime()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        source.putAll(tempMap);
        return this;

    }

    public MapUtils dateFormat(String pattern){
        if( null == source || null == pattern ){
            return this;
        }
        Map<String,Object> tempMap = Maps.newHashMap();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        for(Map.Entry<String,Object> entry : source.entrySet()){
            if( entry.getValue() instanceof Date){
                try {
                    tempMap.put(entry.getKey(),simpleDateFormat.format(entry.getValue()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        source.putAll(tempMap);
        return this;

    }


    public MapUtils keyRename(String oldName,String newName){
        Map<String,Object> tempMap = Maps.newHashMap();
        if( source.containsKey(oldName) ){
            tempMap.put(newName,source.remove(oldName));
        }

        source.putAll(tempMap);
        return this;
    }

    public MapUtils keyRename(Map<String,String> names){
        for(Map.Entry<String,String> entry : names.entrySet()){
            if( source.containsKey(entry.getKey()) ){
                keyRename(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }
}
