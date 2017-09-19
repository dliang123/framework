package com.yuntu.web.example.controller;

import com.yuntu.configuration.Switch;
import com.yuntu.configuration.SwitchManager;

/**
 *  16/1/26.
 */
public class Config {

    @Switch(key = "ABCDE",des = "测试的开关")
    public static String AAAA="12345";

    static {
        SwitchManager.register(Config.class);
    }
}
