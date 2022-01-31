package com.ljx.admin.controller;

import com.ljx.admin.service.CategoryService;
import com.ljx.api.BaseController;
import com.ljx.api.controller.admin.CategoryMngControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.SaveCategoryBO;
import com.ljx.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
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
        //修改、新增分类完成后，删除redis内容
        redis.del(REDIS_ALL_CATEGORY);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getCatList() {
        List<Category> CatList = categoryService.queryCategoryList();
        return GraceJSONResult.ok(CatList);
    }

    @Override
    public GraceJSONResult getCats() {
        //TODO把数据放入到redis并进行查询
        //先从redis查询，为空则查询mysql后，放入redis，再返回
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);
        List<Category> CatList = null;
        if (StringUtils.isBlank(allCatJson)) {
            //通过数据库查询
            CatList = categoryService.queryCategoryList();
            //存入到redis
            redis.set(REDIS_ALL_CATEGORY,JsonUtils.objectToJson(CatList));
        } else {
            //redis存在
            CatList = JsonUtils.jsonToList(allCatJson,Category.class);
        }


        return GraceJSONResult.ok(CatList);
    }
}
