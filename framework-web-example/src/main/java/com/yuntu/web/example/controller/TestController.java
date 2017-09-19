package com.yuntu.web.example.controller;

import com.google.common.collect.Maps;
import com.yuntu.base.utils.ConfigUtils;
import com.yuntu.web.annotation.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *  15/11/13.
 */
@Controller
@RequestMapping("test")
@Cache(cacheKey = "rid")
//@Authority(needAuthority = {"10"})
public class TestController {


    @SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
    @RequestMapping("aaa")
//    @Cache(cacheKey = "city",scope = Cache.scope.parameter,expire=1000)
//    @Degrade(degradeFor = RuntimeException.class)
    public Object aaa(LogEntity logEntity) throws Exception{
        System.err.println(logEntity.getRequestTime());
        Map m = Maps.newConcurrentMap();
        m.put("m","success");
        return ConfigUtils.v("a");
//        throw new Exception("333333333");
    }
}
