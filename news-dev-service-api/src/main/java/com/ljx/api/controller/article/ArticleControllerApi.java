package com.ljx.api.controller.article;


import com.ljx.grace.result.GraceJSONResult;
import com.ljx.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(value = "文章业务controller", tags = {"文章业务controller"})
@RequestMapping("article")
public interface ArticleControllerApi {
    @ApiOperation(value = "用户发文", notes = "用户发文", httpMethod = "POST")
    @PostMapping("createArticle")
    public GraceJSONResult createArticle(@RequestBody @Valid NewArticleBO newArticleBO,
                                         BindingResult result);
}
