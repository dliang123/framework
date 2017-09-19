package com.yuntu.jpa.web;

import javax.servlet.http.HttpServletRequest;

/**
 *  2016/10/25.
 */
public interface SortArgumentResolver {

    String [] resolver(HttpServletRequest request);
}
