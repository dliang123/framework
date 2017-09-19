package com.yuntu.configuration.controller;

import com.yuntu.configuration.component.AuthUtils;
import com.yuntu.configuration.component.JQDataTablePageRequest;
import com.yuntu.configuration.entity.ConfigEntity;
import com.yuntu.configuration.entity.ConfigId;
import com.yuntu.configuration.service.ConfigService;
import com.yuntu.web.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  16/1/13.
 */
@Controller
@RequestMapping("config")
@Authority(permissions = AuthUtils.PERMISSION_LOGIN)
public class ConfigController {


    @Autowired
    private ConfigService configService;

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(ConfigEntity configEntity) throws Exception {

        configService.saveOrUpdate(configEntity);
        return configEntity;
    }

    @RequestMapping("list")
    @ResponseBody
    public Object list(JQDataTablePageRequest request,Long applicationId){
        return configService.findByApplicationId(request,applicationId);
    }

    @RequestMapping("findById")
    @ResponseBody
    public Object findById(ConfigId id){
        return configService.findById(id);
    }

    @RequestMapping("findAll")
    @ResponseBody
    public Object downloadConfig(Long applicationId){
        return configService.findByApplicationId( applicationId );
    }
}
