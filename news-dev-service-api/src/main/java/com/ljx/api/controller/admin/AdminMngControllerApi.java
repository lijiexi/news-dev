package com.ljx.api.controller.admin;


import com.ljx.grace.result.GraceJSONResult;
import com.ljx.pojo.bo.AdminLoginBO;
import com.ljx.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "admin维护", tags = {"admin维护"})
@RequestMapping("adminMng")
public interface AdminMngControllerApi {
    @ApiOperation(value = "管理员登录", notes = "管理员登录", httpMethod = "POST")
    @PostMapping("/adminLogin")
    public GraceJSONResult adminLogin(@RequestBody AdminLoginBO adminLoginBO,
                                      HttpServletRequest request,
                                      HttpServletResponse response);

    @ApiOperation(value = "查询admin用户名是否存在", notes = "查询admin用户名是否存在", httpMethod = "POST")
    @PostMapping("/adminIsExist")
    public GraceJSONResult adminIsExist(@RequestParam String username);

    @ApiOperation(value = "添加新管理员", notes = "添加新管理员", httpMethod = "POST")
    @PostMapping("/addNewAdmin")
    public GraceJSONResult addNewAdmin(@RequestBody NewAdminBO newAdminBO,
                                       HttpServletRequest request,
                                       HttpServletResponse response);
    @ApiOperation(value = "查询admin列表", notes = "查询admin列表", httpMethod = "POST")
    @PostMapping("/getAdminList")
    public GraceJSONResult getAdminList(
            @ApiParam(name = "page",value = "查询下一页（第几页）",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "分页查询每一页显示的条数",required = false)
            @RequestParam Integer pageSize);
    @ApiOperation(value = "管理员退出登录", notes = "管理员退出登录", httpMethod = "POST")
    @PostMapping("/adminLogout")
    public GraceJSONResult adminLogout(@RequestParam String adminId,
                                       HttpServletRequest request,
                                       HttpServletResponse response);

}
