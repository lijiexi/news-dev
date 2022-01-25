package com.ljx.user.controller;

import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Object hello(){
//        logger.debug("debug:hello");
//        logger.info("debug:hello");
//        logger.warn("debug:hello");
//        logger.error("debug:hello");

        return GraceJSONResult.ok();
    }
}
