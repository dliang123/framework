package com.yuntu.web.example.controller;

import com.google.common.collect.Maps;
import com.yuntu.web.ResponseType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *  15/11/16.
 */
@Component
public class ResponseProcessor implements com.yuntu.web.processor.ResponseProcessor{


    public Object processor(Object value, ResponseType type) {

        Map<String,Object> m = Maps.newConcurrentMap();
        m.put("success",value);
        return m;
    }

    public Object processorForException(Exception e, ResponseType type) throws Exception {
        Map<String,Object> m = Maps.newConcurrentMap();
        m.put("success",e.getMessage());
        return m;
    }
}
