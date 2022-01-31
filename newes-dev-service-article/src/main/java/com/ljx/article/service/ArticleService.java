package com.ljx.article.service;

import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewArticleBO;

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

}
