package com.ljx.article.service;

import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewArticleBO;
import com.ljx.utils.PagedGridResult;

import java.util.Date;
import java.util.List;

public interface ArticleService {
    /**
     * 通过BO创建文章
     */
    public void createArticle(NewArticleBO newArticleBO,
                              Category category);

    /**
     * 更新定时发布为即时发布
     */
    public void updateAppointTopublish();
    /**
     * 延迟队列收到消息后，更改mysql对应的一条文章记录，设置其appoint状态为0
     * 更新单条文章为即时发布
     */
    public void updateArticleTopublish(String articleId);

    /**
     * 用户中心，用户查询自己对文章列表
     */
    public PagedGridResult queryMyArticleList(String userId,
                                              String keyword,
                                              Integer status,
                                              Date startDate,
                                              Date endDate,
                                              Integer page,
                                              Integer pageSize);
    /**
     * admin中心，管理员查询所有文章列表
     */
    public PagedGridResult queryAllArticleList(Integer status,
                                               Integer page,
                                               Integer pageSize);

    /**
     * 修改文章的状态
     */
    public void updateArticleStatus(String articleId, Integer pendingStatus);

    /**
     * 用户删除文章
     */
    public void delete(String userId, String articleId);

    /**
     * 用户撤回文章
     */
    public void withdraw(String userId, String articleId);
}
