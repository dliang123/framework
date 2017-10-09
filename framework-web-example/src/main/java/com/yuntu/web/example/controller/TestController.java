package com.yuntu.web.example.controller;

import com.google.common.collect.Maps;
import com.yuntu.base.utils.ConfigUtils;
import com.yuntu.web.annotation.Cache;
import com.yuntu.web.core.dao.MarketingActivityDao;
import com.yuntu.web.core.entity.MarketingActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 15/11/13.
 */
@Controller
@RequestMapping("test")
@Cache(cacheKey = "Test")
//@Authority(needAuthority = {"10"})
public class TestController {

    @Autowired
    private MarketingActivityDao marketingActivityDao;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "/cacheTest", method = {RequestMethod.GET, RequestMethod.POST})
    @Cache(cacheKey = "id", scope = Cache.scope.parameter, expire = 100000)
//    @Degrade(degradeFor = RuntimeException.class)
    public MarketingActivityEntity cacheTest(Long id) throws Exception {

        Map<String, String> map = Maps.newHashMap();
        map.put("1", "aaa1");
        map.put("2", "bbb");
        map.put("3", "ccc3");

        return marketingActivityDao.findOne(id);
//        return ConfigUtils.v("a") + map.get(key);
//        throw new Exception("333333333");
    }
}
