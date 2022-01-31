package com.ljx.article.service.impl;

import com.ljx.api.service.BaseService;
import com.ljx.article.mapper.ArticleMapper;
import com.ljx.article.mapper.ArticleMapperCustom;
import com.ljx.article.service.ArticleService;
import com.ljx.enums.ArticleAppointType;
import com.ljx.enums.ArticleReviewStatus;
import com.ljx.enums.YesOrNo;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.Article;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewArticleBO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private ArticleMapperCustom articleMapperCustom;

    @Transactional
    @Override
    public void createArticle(NewArticleBO newArticleBO, Category category) {
        String articleId = sid.nextShort();
        Article  article = new Article();
        BeanUtils.copyProperties(newArticleBO,article);
        article.setId(articleId);
        article.setCategoryId(category.getId());
        //设置文章状态:正在审核
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        //设置评论数量
        article.setCommentCounts(0);
        article.setReadCounts(0);
        //设置是否表面删除
        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        //设置发布时间
        //判断是否为定时发布文章
        if (newArticleBO.getIsAppoint() == ArticleAppointType.TIMING.type) {
            //如果是定时发布，则从BO取出发布时间
            article.setPublishTime(newArticleBO.getPublishTime());
        } else if (newArticleBO.getIsAppoint() == ArticleAppointType.IMMEDIATELY.type) {
            //即时发布则设置当前时间
            article.setPublishTime(new Date());
        }
        int res = articleMapper.insert(article);
        if (res != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
    }
    @Transactional
    @Override
    public void updateAppointTopublish() {
        articleMapperCustom.updateAppointToPublish();
    }
}
