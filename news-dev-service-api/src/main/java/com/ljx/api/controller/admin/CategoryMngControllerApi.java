package com.ljx.api.controller.admin;


import com.ljx.grace.result.GraceJSONResult;
import com.ljx.pojo.bo.SaveCategoryBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(value = "文章分类维护", tags = {"文章分类controller"})
@RequestMapping("categoryMng")
public interface CategoryMngControllerApi {
    @ApiOperation(value = "新增或修改分类", notes = "新增或修改分类", httpMethod = "POST")
    @PostMapping("/saveOrUpdateCategory")
    public GraceJSONResult saveOrUpdateCategory(@RequestBody @Valid SaveCategoryBO saveCategoryBO,
                                                BindingResult result);
    @ApiOperation(value = "管理员查询分类列表", notes = "管理员查询分类列表", httpMethod = "POST")
    @PostMapping("/getCatList")
    public GraceJSONResult getCatList();

    @ApiOperation(value = "用户查询分类列表", notes = "用户查询分类列表", httpMethod = "GET")
    @GetMapping("/getCats")
    public GraceJSONResult getCats();


}
