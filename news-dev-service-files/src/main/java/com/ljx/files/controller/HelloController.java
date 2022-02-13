package com.ljx.files.controller;

import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Object hello(){

        return GraceJSONResult.ok("hello files");
    }

}
