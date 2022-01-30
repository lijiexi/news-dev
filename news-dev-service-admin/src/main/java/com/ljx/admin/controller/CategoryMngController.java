package com.ljx.admin.controller;

import com.ljx.admin.service.CategoryService;
import com.ljx.api.BaseController;
import com.ljx.api.controller.admin.CategoryMngControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.SaveCategoryBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryMngController extends BaseController implements CategoryMngControllerApi {

    final static Logger logger = LoggerFactory.getLogger(CategoryMngController.class);
    @Autowired
    private CategoryService categoryService;
    @Override
    public GraceJSONResult saveOrUpdateCategory(@Valid SaveCategoryBO saveCategoryBO,
                                                BindingResult result) {
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return GraceJSONResult.errorMap(errorMap);
        }
        //创建分类pojo对象，从bo拷贝数据
        Category category = new Category();
        BeanUtils.copyProperties(saveCategoryBO,category);
        // id为空新增，不为空修改
        if(saveCategoryBO.getId() == null) {
            //确保新增分类名称不存在
            boolean isExist = categoryService.queryCatIsExist(category.getName(),null);
            if(!isExist) {
                categoryService.createCategory(category);
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }else {
            //查询修改分类名称不存在
            boolean isExist = categoryService.queryCatIsExist(category.getName(),saveCategoryBO.getOldName());
            if(!isExist) {
                //修改数据库
                categoryService.modifyCategory(category);
            } else {
                //存在该分类，报错
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getCatList() {
        List<Category> CatList = categoryService.queryCategoryList();
        return GraceJSONResult.ok(CatList);
    }
}
