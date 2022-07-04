package com.ljx.api.controller.user;


import com.ljx.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "粉丝管理", tags = {"粉丝管理"})
@RequestMapping("fans")
public interface MyFansControllerApi {
    @ApiOperation(value = "被动更新作家粉丝信息", notes = "被动更新作家粉丝信息", httpMethod = "POST")
    @PostMapping("/forceUpdateFanInfo")
    public GraceJSONResult forceUpdateFanInfo(@RequestParam String relationId,
                                                @RequestParam String fanId);

    @ApiOperation(value = "查询当前用户是否关注作家", notes = "查询当前用户是否关注作家", httpMethod = "POST")
    @PostMapping("/isMeFollowThisWriter")
    public GraceJSONResult isMeFollowThisWriter(@RequestParam String writerId,
                                                @RequestParam String fanId);

    @ApiOperation(value = "用户关注作家成为粉丝", notes = "用户关注作家成为粉丝", httpMethod = "POST")
    @PostMapping("/follow")
    public GraceJSONResult follow(@RequestParam String writerId,
                                                @RequestParam String fanId);

    @ApiOperation(value = "用户取关作家", notes = "用户取关作家", httpMethod = "POST")
    @PostMapping("/unfollow")
    public GraceJSONResult unfollow(@RequestParam String writerId,
                                    @RequestParam String fanId);

    @ApiOperation(value = "查询我的所有粉丝列表", notes = "查询我的所有粉丝列表", httpMethod = "POST")
    @PostMapping("/queryAll")
    public GraceJSONResult queryAll(
            @RequestParam String writerId,
            @ApiParam(name = "page",value = "查询下一页（第几页）",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "分页查询每一页显示的条数",required = false)
            @RequestParam Integer pageSize);

    /**
     * 粉丝数据可视化
     */

    @ApiOperation(value = "查询男女粉丝数量", notes = "查询男女粉丝数量", httpMethod = "POST")
    @PostMapping("/queryRatio")
    public GraceJSONResult queryRatio(@RequestParam String writerId);

    @ApiOperation(value = "根据地域查询粉丝数量", notes = "根据地域查询粉丝数量", httpMethod = "POST")
    @PostMapping("/queryRatioByRegion")
    public GraceJSONResult queryRatioByRegion(@RequestParam String writerId);
}
