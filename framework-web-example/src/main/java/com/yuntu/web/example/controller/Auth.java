package com.yuntu.web.example.controller;

import com.google.common.collect.Lists;
import com.yuntu.web.AuthorityProcess;
import com.yuntu.web.exception.NoAuthorityException;
import com.yuntu.web.util.WebUtils;
import org.springframework.stereotype.Component;

/**
 *  15/11/13.
 */
@Component
public class Auth implements AuthorityProcess {

    public boolean validationByPermissions(String ... auth) throws NoAuthorityException {
        return Lists.newArrayList(auth).contains(WebUtils.getRequest().getParameter("auth"));
    }

    public boolean validationByResources(String resources) throws NoAuthorityException {
        return false;
    }
}
