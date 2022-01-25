package com.ljx.api;


import com.ljx.utils.RedisOperator;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定义redis和用户ip地址
 */
public class BaseController {
    @Autowired
    public RedisOperator redis;

    public static final String MOBILE_SMSCODE = "mobile:smscode";

}
