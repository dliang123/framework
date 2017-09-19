package com.yuntu.web;

import com.yuntu.web.exception.NoAuthorityException;

/**
 *  15/11/12.
 */
public interface AuthorityProcess {

    boolean validationByPermissions(String ... auth) throws NoAuthorityException;

    boolean validationByResources(String resources) throws NoAuthorityException;
}
