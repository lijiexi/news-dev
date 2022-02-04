package com.ljx.article.controller;

import com.ljx.api.BaseController;
import com.ljx.api.controller.article.ArticlePortalControllerApi;
import com.ljx.article.service.ArticlePortalService;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.pojo.Article;
import com.ljx.pojo.eo.ArticleEO;
import com.ljx.pojo.vo.AppUserVO;
import com.ljx.pojo.vo.ArticleDetailVO;
import com.ljx.pojo.vo.IndexArticleVO;
import com.ljx.utils.IPUtil;
import com.ljx.utils.JsonUtils;
import com.ljx.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    final static Logger logger = LoggerFactory.getLogger(ArticlePortalController.class);
    @Autowired
    private ArticlePortalService articlePortalService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public GraceJSONResult eslist(String keyword,
                                  Integer category,
                                  Integer page,
                                  Integer pageSize) {
        /**
         * es查询：
         *      1.首页默认查询不带参数
         *      2.按照文类查询
         *      3.搜索框关键字查询
         */
        //es的页码是从0开始计算的
        if (page < 1) return null;
        page--;
        //分页
        Pageable pageable = PageRequest.of(page,pageSize);
        SearchQuery query = null;
        //1.首页默认查询不带参数
        if (StringUtils.isBlank(keyword) && category == null) {
             query = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchAllQuery())
                    .withPageable(pageable) //实现分页
                    .build();
        }
        //2.按照文类查询，使用term全匹配
        if (StringUtils.isBlank(keyword) && category != null) {
            query = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.termQuery("categoryId",category))
                    .withPageable(pageable) //实现分页
                    .build();


        }
        //3.搜索框关键字查询
        if (StringUtils.isNotBlank(keyword) && category == null) {
            query = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery("title",keyword))
                    .withPageable(pageable) //实现分页
                    .build();
        }
        AggregatedPage<ArticleEO> pagedArticle = elasticsearchTemplate.queryForPage(query, ArticleEO.class);
        List<ArticleEO> articleList = pagedArticle.getContent();

        // 重新封装成grid格式
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(articleList);
        gridResult.setPage(page + 1);
        gridResult.setTotal(pagedArticle.getTotalPages());
        gridResult.setRecords(pagedArticle.getTotalElements());

        gridResult = rebuildArticleGrid(gridResult);

        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult mysqlList(String keyword,
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
        List<String> idList = new ArrayList<>();
        for (Article a : list) {
            //1.构建发布者的set
            idSet.add(a.getPublishUserId());
            //2.构建文章id的list
            idList.add(REDIS_ARTICLE_READ_COUNTS+":"+a.getId());
        }
        //发起redis的mget批量查询api，获得对应的值
        List<String> readCountsRedisList = redis.mget(idList);
        List<AppUserVO> publishList = null;
//        //2.发起resttemplate发起远程调用，请求用户服务，获得用户列表
//        String userServerUrlExecute
//                = "http://user.news.com:8003/user/queryByIds?userIds="+JsonUtils.objectToJson(idSet);
//        ResponseEntity<GraceJSONResult> resultResponseEntity
//                = restTemplate.getForEntity(userServerUrlExecute,GraceJSONResult.class);
//        //获得查询结果
//        GraceJSONResult bodyResult = resultResponseEntity.getBody();
//        //System.out.println(bodyResult);
//        List<AppUserVO> publishList = null;
//        if (bodyResult.getStatus() == 200) {
//            String userJson = JsonUtils.objectToJson(bodyResult.getData());
//            publishList = JsonUtils.jsonToList(userJson,AppUserVO.class);
//        }
        publishList = getPublisherList(idSet);
        //3.拼接list，重新组成列表
        List<IndexArticleVO> indexArticleVOS = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            Article a = list.get(i);
            //拷贝article属性到vo
            BeanUtils.copyProperties(a,indexArticleVO);
            //循环publishList获得用户基本信息
            AppUserVO appUserVO = getUserInfo(a.getPublishUserId(),publishList);
            indexArticleVO.setPublisherVO(appUserVO);
            //获得首页文章列表阅读数，放入VO,对数据库压力太，采取mget方式
//            int readCounts = getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS+":"+a.getId());
//            indexArticleVO.setReadCounts(readCounts);
            String redisCountsStr = readCountsRedisList.get(i);
            int readCounts = 0;
            if (StringUtils.isNotBlank(redisCountsStr)) {
                readCounts = Integer.valueOf(redisCountsStr);
            }
            indexArticleVO.setReadCounts(readCounts);
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

    @Override
    public GraceJSONResult hotList() {
        List<Article> list = articlePortalService.queryHotList();
        return GraceJSONResult.ok(list);
    }

    @Override
    public GraceJSONResult queryArticleListOfWriter(String writerId,
                                                    Integer page,
                                                    Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGESIZE;
        }
        //service查询列表
        PagedGridResult res
                = articlePortalService.queryIndexqueryArticleListOfWriterArticleList(writerId,page,pageSize);
        return GraceJSONResult.ok(res);
    }

    @Override
    public GraceJSONResult queryGoodArticleListOfWriter(String writerId) {
        PagedGridResult res
                = articlePortalService.queryGoodArticleListOfWriter(writerId);
        return GraceJSONResult.ok(res);
    }

    @Override
    public GraceJSONResult detail(String articleId) {

        ArticleDetailVO detailVO = articlePortalService.queryDetail(articleId);
        //此时还未获得用户昵称
        //发起远程调用，查询用户昵称
        Set<String> idSet = new HashSet<>();
        idSet.add(detailVO.getPublishUserId());
        List<AppUserVO> publisherList = getPublisherList(idSet);
        if (!publisherList.isEmpty()) {
            detailVO.setPublishUserName(publisherList.get(0).getNickname());
        }
        detailVO.setReadCounts(getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS+":"+articleId));
        return GraceJSONResult.ok(detailVO);
    }
    //使用远程调用，获得userInfo
    private List<AppUserVO> getPublisherList(Set idSet) {
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
        return publishList;
    }

    @Override
    public GraceJSONResult readArticle(String articleId,
                                       HttpServletRequest request) {
        //防止阅读量被刷
        String userIp = IPUtil.getRequestIp(request);
        //设置针对当前用户id永久存在的key，存入到redis，表示当前ip的用户已经阅读过，无法累加阅读量
        redis.setnx(REDIS_ALREADY_READ+":"+articleId+":"+userIp,userIp);
        redis.increment(REDIS_ARTICLE_READ_COUNTS+":"+articleId,1);
        return GraceJSONResult.ok();
    }


    /**
     * 从redis获取阅读量、远程调用获取作者昵称和头像，重组文章信息
     */
    private PagedGridResult rebuildArticleGrid(PagedGridResult gridResult) {
        // START

        List<ArticleEO> list = (List<ArticleEO>)gridResult.getRows();

        // 1. 构建发布者id列表
        Set<String> idSet = new HashSet<>();
        List<String> idList = new ArrayList<>();
        for (ArticleEO a : list) {
            // 1.1 构建发布者的set
            idSet.add(a.getPublishUserId());
            // 1.2 构建文章id的list
            idList.add(REDIS_ARTICLE_READ_COUNTS + ":" + a.getId());
        }
        // 发起redis的mget批量查询api，获得对应的值
        List<String> readCountsRedisList = redis.mget(idList);
        //使用远程调用，获得userInfo
        List<AppUserVO> publisherList = getPublisherList(idSet);

        // 3. 拼接两个list，重组文章列表
        List<IndexArticleVO> indexArticleList = new ArrayList<>();

        for (int i = 0 ; i < list.size() ; i ++) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            ArticleEO a = list.get(i);
            BeanUtils.copyProperties(a, indexArticleVO);

            // 3.1 从publisherList中获得发布者的基本信息
            AppUserVO publisher  = getUserIfPublisher(a.getPublishUserId(), publisherList);
            indexArticleVO.setPublisherVO(publisher);

            // 3.2 重新组装设置文章列表中的阅读量
            String redisCountsStr = readCountsRedisList.get(i);
            int readCounts = 0;
            if (StringUtils.isNotBlank(redisCountsStr)) {
                readCounts = Integer.valueOf(redisCountsStr);
            }
            indexArticleVO.setReadCounts(readCounts);

            indexArticleList.add(indexArticleVO);
        }


        gridResult.setRows(indexArticleList);
// END
        return gridResult;
    }
    private AppUserVO getUserIfPublisher(String publisherId,
                                         List<AppUserVO> publisherList) {
        for (AppUserVO user : publisherList) {
            if (user.getId().equalsIgnoreCase(publisherId)) {
                return user;
            }
        }
        return null;
    }


}
