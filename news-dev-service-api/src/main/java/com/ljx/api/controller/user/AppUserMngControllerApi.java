package com.ljx.api.controller.user;


import com.ljx.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Api(value = "admin用户管理相关接口", tags = {"admin用户管理相关接口"})
@RequestMapping("appUser")
public interface AppUserMngControllerApi {
    @ApiOperation(value = "查询所有网站用户", notes = "查询所有网站用户", httpMethod = "POST")
    @PostMapping("queryAll")
    public GraceJSONResult queryAll(@RequestParam String nickname,
                                    @RequestParam Integer status,
                                    @RequestParam Date startDate,
                                    @RequestParam Date endDate,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize);
}
