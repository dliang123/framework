package com.yuntu.configuration.controller;

import com.google.common.collect.Lists;
import com.yuntu.configuration.component.AuthUtils;
import com.yuntu.configuration.component.UserUtils;
import com.yuntu.configuration.entity.UserEntity;
import com.yuntu.configuration.service.UserService;
import com.yuntu.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by linxiaohui on 16/1/14.
 */
@Controller
public class LoginController {


    @Autowired
    private UserService userService;


    @RequestMapping("login")
    @ResponseBody
    public Object login(String userName,String password){

        UserEntity userEntity=userService.findUserByUserNameAndPassword(userName,password);
        if( null == userEntity ){
            throw new RuntimeException("账户或密码错误");
        }

        WebUtils.getSession().setAttribute(AuthUtils.PERMISSION_KEY, Lists.newArrayList(AuthUtils.PERMISSION_LOGIN));
        WebUtils.getSession().setAttribute(UserUtils.USER_ID_KE, userEntity.getId());

        return userEntity.getName();
    }


    @RequestMapping("logout")
    @ResponseBody
    public Object logout(){
        WebUtils.getSession().invalidate();
        return true;
    }

}
