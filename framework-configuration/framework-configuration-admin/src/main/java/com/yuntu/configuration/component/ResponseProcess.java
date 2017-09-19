package com.yuntu.configuration.component;

import com.google.common.collect.Maps;
import com.yuntu.base.Response;
import com.yuntu.web.ResponseType;
import com.yuntu.web.processor.ResponseProcessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by linxiaohui on 16/1/15.
 */
@Component
public class ResponseProcess implements ResponseProcessor {


    public Object processor(Object obj, ResponseType type) {

        if( obj instanceof Page){
            Page<?> page = (Page<?>) obj;
            Map<Object,Object> _data = Maps.newHashMap();

            _data.put("recordsTotal",page.getTotalElements());
            _data.put("recordsFiltered",page.getTotalElements());
            _data.put("data", page.getContent());

            return Response.success(_data);
        }

        return Response.success(obj);
    }

    public Object processorForException(Exception e, ResponseType type) throws Exception {
        return Response.failure(e);
    }
}
