package com.yuntu.configuration.component;

import com.google.common.collect.Maps;

import java.util.HashMap;

@SuppressWarnings({ "serial", "rawtypes","unchecked" })
public class ColumnMap extends HashMap {

	public ColumnMap(){
        this.put("search", Maps.newHashMapWithExpectedSize(2));
    }
}