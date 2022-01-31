package com.ljx.api.controller.article;


import com.ljx.grace.result.GraceJSONResult;
import com.ljx.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

@Api(value = "文章业务controller", tags = {"文章业务controller"})
@RequestMapping("article")
public interface ArticleControllerApi {
    @ApiOperation(value = "用户发文", notes = "用户发文", httpMethod = "POST")
    @PostMapping("createArticle")
    public GraceJSONResult createArticle(@RequestBody @Valid NewArticleBO newArticleBO,
                                         BindingResult result);

    @ApiOperation(value = "用户查询自己全部文章", notes = "用户查询自己全部文章", httpMethod = "POST")
    @PostMapping("queryMyList")
    public GraceJSONResult queryMyList(@RequestParam String userId,
                                       @RequestParam String keyword,
                                       @RequestParam Integer status,
                                       @RequestParam Date startDate,
                                       @RequestParam Date endDate,
                                       @RequestParam Integer page,
                                       @RequestParam Integer pageSize);

    @ApiOperation(value = "管理员查全部文章", notes = "管理员查全部文章", httpMethod = "POST")
    @PostMapping("queryAllList")
    public GraceJSONResult queryAllList(@RequestParam Integer status,
                                        @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
                                        @RequestParam Integer page,
                                        @ApiParam(name = "pageSize",value = "每一页显示条数",required = false)
                                        @RequestParam Integer pageSize);

    @ApiOperation(value = "管理员对文章审核通过或者失败", notes = "管理员对文章审核通过或者失败", httpMethod = "POST")
    @PostMapping("doReview")
    public GraceJSONResult doReview(@RequestParam String articleId,
                                    @RequestParam Integer passOrNot);

    @ApiOperation(value = "用户删除文章", notes = "用户删除文章", httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String userId,
                                  @RequestParam String articleId);

    @ApiOperation(value = "用户撤回文章", notes = "用户撤回文章", httpMethod = "POST")
    @PostMapping("/withdraw")
    public GraceJSONResult withdrawete(@RequestParam String userId,
                                          @RequestParam String articleId);


}
