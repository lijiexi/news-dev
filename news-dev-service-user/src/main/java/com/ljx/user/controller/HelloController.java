package com.ljx.user.controller;

import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    private RedisOperator redis;

    public Object hello(){
//        logger.debug("debug:hello");
//        logger.info("debug:hello");
//        logger.warn("debug:hello");
//        logger.error("debug:hello");

        return GraceJSONResult.ok();
    }
    @GetMapping("/redis")
    public Object redis(){
        redis.set("age","22");

        return GraceJSONResult.ok(redis.get("age"));
    }
}
