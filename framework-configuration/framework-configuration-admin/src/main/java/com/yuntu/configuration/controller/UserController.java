package com.yuntu.configuration.controller;

import com.yuntu.configuration.component.AuthUtils;
import com.yuntu.configuration.component.JQDataTablePageRequest;
import com.yuntu.configuration.entity.UserEntity;
import com.yuntu.configuration.service.UserService;
import com.yuntu.web.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  16/1/14.
 */
@Controller
@RequestMapping("user")
@Authority(permissions = AuthUtils.PERMISSION_LOGIN)
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping("list")
    @ResponseBody
    public Object list(JQDataTablePageRequest pageable, String name){
        return userService.find(pageable,pageable.searchValue());
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(UserEntity userEntity){
        userService.saveOrUpdate(userEntity);
        return "操作成功";
    }
}
