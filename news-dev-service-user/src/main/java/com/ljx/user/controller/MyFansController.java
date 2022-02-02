package com.ljx.user.controller;

import com.ljx.api.BaseController;
import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.api.controller.user.MyFansControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.user.service.MyFansService;
import com.ljx.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyFansController extends BaseController implements MyFansControllerApi {

    final static Logger logger = LoggerFactory.getLogger(MyFansController.class);
    @Autowired
    private MyFansService myFansService;

    @Override
    public GraceJSONResult isMeFollowThisWriter(String writerId, String fanId) {
        boolean res = myFansService.isMeFollowThisWriter(writerId,fanId);
        return GraceJSONResult.ok(res);
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId,fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult unfollow(String writerId, String fanId) {
        myFansService.unfollow(writerId,fanId);
        return GraceJSONResult.ok();
    }
}
