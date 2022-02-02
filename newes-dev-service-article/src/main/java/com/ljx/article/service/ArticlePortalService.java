package com.ljx.article.service;

import com.ljx.pojo.Article;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewArticleBO;
import com.ljx.pojo.vo.ArticleDetailVO;
import com.ljx.utils.PagedGridResult;

import java.util.Date;
import java.util.List;

public interface ArticlePortalService {

    /**
     * 首页查询文章列表
     */
    public PagedGridResult queryIndexArticleList(String keyword,
                                                 Integer category,
                                                 Integer page,
                                                 Integer pageSize);
    /**
     * 首页查热文列表
     */
    public List<Article> queryHotList();
    /**
     * 查询某人热文列表
     */
    public PagedGridResult queryGoodArticleListOfWriter(String writerId);

    /**
     * 查询作家所有文章列表
     */
    public PagedGridResult queryIndexqueryArticleListOfWriterArticleList(String writerId,
                                                                         Integer page,
                                                                         Integer pageSize);
    /**
     * 查询文章详细内容
     */
    public ArticleDetailVO queryDetail(String articleId);
}
