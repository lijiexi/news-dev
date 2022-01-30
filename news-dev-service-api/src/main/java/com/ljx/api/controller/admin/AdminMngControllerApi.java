package com.ljx.api.controller.admin;


import com.ljx.grace.result.GraceJSONResult;
import com.ljx.pojo.bo.AdminLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "admin维护", tags = {"admin维护"})
@RequestMapping("adminMng")
public interface AdminMngControllerApi {
    @ApiOperation(value = "当前接口作用", notes = "当前接口作用", httpMethod = "POST")
    @PostMapping("/adminLogin")
    public GraceJSONResult adminLogin(@RequestBody AdminLoginBO adminLoginBO,
                                      HttpServletRequest request,
                                      HttpServletResponse response);
}
