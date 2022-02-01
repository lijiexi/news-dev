package com.ljx.api.controller.article;


import com.ljx.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "首页文章业务controller", tags = {"首页文章业务controller"})
@RequestMapping("portal/article")
public interface ArticlePortalControllerApi {


    @ApiOperation(value = "首页查询文章列表", notes = "首页查询文章列表", httpMethod = "GET")
    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String keyword,
                                @RequestParam Integer category,
                                @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
                                    @RequestParam Integer page,
                                @ApiParam(name = "pageSize",value = "每一页显示条数",required = false)
                                    @RequestParam Integer pageSize);

    @ApiOperation(value = "首页查热文列表", notes = "首页查热文列表", httpMethod = "GET")
    @GetMapping("hotList")
    public GraceJSONResult hotList();

    /**
     * 作家页接口，作家发布的所有文章
     */
    @GetMapping("queryArticleListOfWriter")
    @ApiOperation(value = "查询作家发布的所有文章列表", notes = "查询作家发布的所有文章列表", httpMethod = "GET")
    public GraceJSONResult queryArticleListOfWriter(@RequestParam String writerId,
                                                    @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                                                    @RequestParam Integer page,
                                                    @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
                                                    @RequestParam Integer pageSize);
    @GetMapping("queryGoodArticleListOfWriter")
    @ApiOperation(value = "作家页面查询近期佳文", notes = "作家页面查询近期佳文", httpMethod = "GET")
    public GraceJSONResult queryGoodArticleListOfWriter(@RequestParam String writerId);

}
