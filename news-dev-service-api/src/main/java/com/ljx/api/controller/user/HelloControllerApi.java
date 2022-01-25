package com.ljx.api.controller.user;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "controller的标题", tags = {"带有xx功能的controller"})
public interface HelloControllerApi {
    @ApiOperation(value = "当前接口作用", notes = "当前接口作用", httpMethod = "GET")
    @GetMapping("/hello")
    public Object hello();
}
