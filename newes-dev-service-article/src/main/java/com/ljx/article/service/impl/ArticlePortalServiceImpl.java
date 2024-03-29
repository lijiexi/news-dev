package com.ljx.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.ljx.api.service.BaseService;
import com.ljx.article.mapper.ArticleMapper;
import com.ljx.article.mapper.ArticleMapperCustom;
import com.ljx.article.service.ArticlePortalService;
import com.ljx.article.service.ArticleService;
import com.ljx.enums.ArticleAppointType;
import com.ljx.enums.ArticleReviewStatus;
import com.ljx.enums.YesOrNo;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.Article;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewArticleBO;
import com.ljx.pojo.vo.ArticleDetailVO;
import com.ljx.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


@Service
public class ArticlePortalServiceImpl extends BaseService implements ArticlePortalService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PagedGridResult queryIndexArticleList(String keyword,
                                                 Integer category,
                                                 Integer page,
                                                 Integer pageSize) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("publishTime").desc();
        Example.Criteria criteria = articleExample.createCriteria();
        /**
         * 查询首页文章条件
         * 1.即时发布文章或者定时任务到点发布
         * 2.isDelete=未删除 逻辑未删除
         * 3.articleStatus==3 审核通过的文章
         */

        criteria.andEqualTo("isAppoint",YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);

        //keyword为空，则查询所有；category为空，则查询所有分类文章
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title","%"+keyword+"%");
        }

        if (category != null) {
            criteria.andEqualTo("categoryId",category);
        }
        PageHelper.startPage(page,pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list,page);
    }

    @Override
    public PagedGridResult queryIndexqueryArticleListOfWriterArticleList(String writerId,
                                                                         Integer page,
                                                                         Integer pageSize) {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = setDefaultArticleExample(articleExample);
        criteria.andEqualTo("publishUserId",writerId);
        PageHelper.startPage(page,pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list,page);
    }

    @Override
    public List<Article> queryHotList() {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = setDefaultArticleExample(articleExample);
        //分页，只查5条数据
        PageHelper.startPage(1,5);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return list;
    }

    @Override
    public PagedGridResult queryGoodArticleListOfWriter(String writerId) {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = setDefaultArticleExample(articleExample);
        criteria.andEqualTo("publishUserId",writerId);
        //分页，只查5条数据
        PageHelper.startPage(1,5);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list,1);
    }

    private Example.Criteria setDefaultArticleExample(Example articleExample) {
        articleExample.orderBy("publishTime").desc();
        Example.Criteria criteria = articleExample.createCriteria();
        /**
         * 查询首页文章条件
         * 1.即时发布文章或者定时任务到点发布
         * 2.isDelete=未删除 逻辑未删除
         * 3.articleStatus==3 审核通过的文章
         */

        criteria.andEqualTo("isAppoint",YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);
        return criteria;
    }

    @Override
    public ArticleDetailVO queryDetail(String articleId) {
        Article article = new Article();
        article.setId(articleId);
        article.setIsAppoint(YesOrNo.NO.type);
        article.setIsDelete(YesOrNo.NO.type);
        article.setArticleStatus(ArticleReviewStatus.SUCCESS.type);

        Article res = articleMapper.selectOne(article);
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(res,articleDetailVO);

        return articleDetailVO;
    }
}
