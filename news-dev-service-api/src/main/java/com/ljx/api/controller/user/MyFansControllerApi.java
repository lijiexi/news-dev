package com.ljx.api.controller.user;


import com.ljx.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "粉丝管理", tags = {"粉丝管理"})
@RequestMapping("fans")
public interface MyFansControllerApi {
    @ApiOperation(value = "查询当前用户是否关注作家", notes = "查询当前用户是否关注作家", httpMethod = "POST")
    @PostMapping("/isMeFollowThisWriter")
    public GraceJSONResult isMeFollowThisWriter(@RequestParam String writerId,
                                                @RequestParam String fanId);

    @ApiOperation(value = "用户关注作家成为粉丝", notes = "用户关注作家成为粉丝", httpMethod = "POST")
    @PostMapping("/follow")
    public GraceJSONResult follow(@RequestParam String writerId,
                                                @RequestParam String fanId);

    @ApiOperation(value = "用户取关作家成", notes = "用户取关作家成", httpMethod = "POST")
    @PostMapping("/unfollow")
    public GraceJSONResult unfollow(@RequestParam String writerId,
                                    @RequestParam String fanId);
}
