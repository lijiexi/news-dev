package com.ljx.article.controller;

import com.ljx.api.BaseController;
import com.ljx.api.controller.article.ArticleControllerApi;
import com.ljx.api.controller.article.ArticlePortalControllerApi;
import com.ljx.article.service.ArticlePortalService;
import com.ljx.article.service.ArticleService;
import com.ljx.enums.ArticleCoverType;
import com.ljx.enums.ArticleReviewStatus;
import com.ljx.enums.YesOrNo;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.Article;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewArticleBO;
import com.ljx.pojo.vo.AppUserVO;
import com.ljx.pojo.vo.IndexArticleVO;
import com.ljx.utils.JsonUtils;
import com.ljx.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.*;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    final static Logger logger = LoggerFactory.getLogger(ArticlePortalController.class);
    @Autowired
    private ArticlePortalService articlePortalService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GraceJSONResult list(String keyword,
                                Integer category,
                                Integer page,
                                Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGESIZE;
        }
        //service查询列表
        PagedGridResult res = articlePortalService.queryIndexArticleList(keyword,category,page,pageSize);
        /**
         * 进行拼接查询
         */
        //start
        List<Article> list = (List<Article>)res.getRows();
        //获取list中所有用户ID，根据ID查询用户表
        //1.构建发布者id列表,使用hashset去重
        Set<String> idSet = new HashSet<>();
        for (Article a : list) {
            //System.out.println(a.getPublishUserId());
            idSet.add(a.getPublishUserId());
        }
        //System.out.println(idSet.toString());
        //2.发起resttemplate发起远程调用，请求用户服务，获得用户列表
        String userServerUrlExecute
                = "http://user.news.com:8003/user/queryByIds?userIds="+JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> resultResponseEntity
                = restTemplate.getForEntity(userServerUrlExecute,GraceJSONResult.class);
        //获得查询结果
        GraceJSONResult bodyResult = resultResponseEntity.getBody();
        //System.out.println(bodyResult);
        List<AppUserVO> publishList = null;
        if (bodyResult.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            publishList = JsonUtils.jsonToList(userJson,AppUserVO.class);
        }
        //3.拼接list，重新组成列表
        List<IndexArticleVO> indexArticleVOS = new ArrayList<>();
        for (Article a: list) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            //拷贝article属性到vo
            BeanUtils.copyProperties(a,indexArticleVO);
            //循环publishList获得用户基本信息
            AppUserVO appUserVO = getUserInfo(a.getPublishUserId(),publishList);
            indexArticleVO.setPublisherVO(appUserVO);
            indexArticleVOS.add(indexArticleVO);
        }
        res.setRows(indexArticleVOS);
        return GraceJSONResult.ok(res);
    }
    private AppUserVO getUserInfo(String publisherId,
                                  List<AppUserVO> publishList) {
        for (AppUserVO user : publishList) {
            if (publisherId.equals(user.getId())) {
                return user;
            }
        }
        return null;
    }
}
