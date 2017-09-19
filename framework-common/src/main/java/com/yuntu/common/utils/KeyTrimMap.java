package com.yuntu.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "serial","unchecked" })
public class KeyTrimMap extends LinkedHashMap {
	@Override
    public Object put(Object key, Object value) {
        if( null != key && key instanceof String){
            key = ((String) key).trim();
        }
        if( value instanceof Map){
            Map  _value = new KeyTrimMap();
            _value.putAll((Map)value);
            value=_value;
        }
        return super.put(key, value);
    }
}