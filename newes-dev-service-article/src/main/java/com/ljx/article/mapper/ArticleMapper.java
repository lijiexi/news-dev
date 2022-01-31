package com.ljx.article.mapper;

import com.ljx.my.mapper.MyMapper;
import com.ljx.pojo.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapper extends MyMapper<Article> {
}