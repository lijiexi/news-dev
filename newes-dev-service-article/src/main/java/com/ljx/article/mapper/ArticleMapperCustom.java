package com.ljx.article.mapper;

import com.ljx.my.mapper.MyMapper;
import com.ljx.pojo.Article;
import org.springframework.stereotype.Repository;

/**
 * 自定义mapper class
 */
@Repository
public interface ArticleMapperCustom extends MyMapper<Article> {

    public void updateAppointToPublish();
}