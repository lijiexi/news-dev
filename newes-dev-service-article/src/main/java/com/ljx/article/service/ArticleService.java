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



}
