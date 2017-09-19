package com.yuntu.configuration.controller;

import com.yuntu.configuration.component.AuthUtils;
import com.yuntu.configuration.component.JQDataTablePageRequest;
import com.yuntu.configuration.entity.ApplicationEntity;
import com.yuntu.configuration.service.ApplicationService;
import com.yuntu.web.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  16/1/14.
 */
@Controller
@RequestMapping("application")
@Authority(permissions = AuthUtils.PERMISSION_LOGIN)
public class ApplicationController {


    @Autowired
    private ApplicationService applicationService;

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(ApplicationEntity application){
        applicationService.saveOrUpdate(application);
        return application;
    }

    @RequestMapping("list")
    @ResponseBody
    public Object list(JQDataTablePageRequest request){
        return applicationService.find(request,request.searchValue());
    }
}
