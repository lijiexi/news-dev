package com.ljx.article.controller;

import com.ljx.api.BaseController;
import com.ljx.api.controller.article.ArticleControllerApi;
import com.ljx.article.service.ArticleService;
import com.ljx.enums.ArticleCoverType;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewArticleBO;
import com.ljx.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {

    final static Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    private  ArticleService articleService;

    @Override
    public GraceJSONResult createArticle(@Valid NewArticleBO newArticleBO,
                                         BindingResult result) {
        //校验BO
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return GraceJSONResult.errorMap(errorMap);
        }
        //判断封面是否有图片
        if (newArticleBO.getArticleType() == ArticleCoverType.ONE_IMAGE.type) {
            //文章设置封面有图片，则图片地址为空就报错
            if (StringUtils.isBlank(newArticleBO.getArticleCover())) {
                //文章封面不存在
                return GraceJSONResult.errorCustom(ResponseStatusEnum.FAILED);
            } else if (newArticleBO.getArticleType() == ArticleCoverType.WORDS.type) {
                //文章封面为纯文字
                newArticleBO.setArticleCover("");
            }
        }
        //判断文章分类Id是否存在，直接查询Redis
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);
        List<Category> catList = JsonUtils.jsonToList(allCatJson,Category.class);
        Category temp = null;
        for (Category c: catList) {
            if (c.getId() == newArticleBO.getCategoryId()) {
                temp = c;
                break;
            }
        }
        //BO中id不符合现有分类id
        if (temp == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FAILED);
        }
        //System.out.println(newArticleBO.toString());
        //Service实现
        articleService.createArticle(newArticleBO,temp);
        return GraceJSONResult.ok();
    }
}
