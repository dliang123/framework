package com.yuntu.web;

import org.springframework.http.HttpRequest;

/**
 *  15/11/16.
 */
public interface Keyable {

    String key(HttpRequest request);
}
