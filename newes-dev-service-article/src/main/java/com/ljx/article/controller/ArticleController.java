package com.ljx.article.controller;

import com.ljx.api.BaseController;
import com.ljx.api.controller.article.ArticleControllerApi;
import com.ljx.article.service.ArticleService;
import com.ljx.enums.ArticleCoverType;
import com.ljx.enums.ArticleReviewStatus;
import com.ljx.enums.YesOrNo;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewArticleBO;
import com.ljx.utils.JsonUtils;
import com.ljx.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
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

    @Override
    public GraceJSONResult queryMyList(String userId,
                                       String keyword,
                                       Integer status,
                                       Date startDate,
                                       Date endDate,
                                       Integer page,
                                       Integer pageSize) {
        if(StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGESIZE;
        }
        //service查询列表
        PagedGridResult res = articleService.queryMyArticleList(userId,keyword,status,startDate,endDate,page,pageSize);
        return GraceJSONResult.ok(res);
    }

    @Override
    public GraceJSONResult queryAllList(Integer status, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGESIZE;
        }
        //service查询列表
        PagedGridResult res = articleService.queryAllArticleList(status,page,pageSize);
        return GraceJSONResult.ok(res);
    }

    @Override
    public GraceJSONResult doReview(String articleId, Integer passOrNot) {
        Integer pendingStatus;
        if (passOrNot == YesOrNo.YES.type) {
            //审核文章通过
            pendingStatus = ArticleReviewStatus.SUCCESS.type;
        } else if (passOrNot == YesOrNo.NO.type) {
            //审核文章block
            pendingStatus = ArticleReviewStatus.FAILED.type;
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        //修改文章状态为审核成功、失败
        articleService.updateArticleStatus(articleId,pendingStatus);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult delete(String userId, String articleId) {
        articleService.delete(userId,articleId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult withdrawete(String userId, String articleId) {
        articleService.withdraw(userId,articleId);
        return GraceJSONResult.ok();
    }
}
