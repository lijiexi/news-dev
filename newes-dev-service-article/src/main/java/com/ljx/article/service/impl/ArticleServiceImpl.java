package com.ljx.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.ljx.api.config.RabbitMQDelayConfig;
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
import com.ljx.pojo.eo.ArticleEO;
import com.ljx.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private ArticleMapperCustom articleMapperCustom;
    //@Autowired
    //private RabbitTemplate rabbitTemplate;
    //@Autowired
    //private ElasticsearchTemplate elasticsearchTemplate;

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
        //发送延迟消息到rabbitmq，计算定时发布时间和当前时间的时间差，则为往后延时的时间ms
//        if (article.getIsAppoint() == ArticleAppointType.TIMING.type) {
//            //定时发布时间-当前时间=延迟时间
//            Date futureDate = newArticleBO.getPublishTime();
//            Date nowDate = new Date();
//            int delayTimes = (int)(futureDate.getTime() - nowDate.getTime());
//            //System.out.println(delayTimes);
//
//            MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
//                @Override
//                public Message postProcessMessage(Message message) throws AmqpException {
//                    // 设置消息的持久
//                    message.getMessageProperties()
//                            .setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//                    // 设置消息延迟的时间，单位ms毫秒
//                    message.getMessageProperties()
//                            .setDelay(delayTimes);
//                    return message;
//                }
//            };
//            //传入当前文章id，作为消息
//            rabbitTemplate.convertAndSend(
//                    RabbitMQDelayConfig.EXCHANGE_DELAY,
//                    "publish.delay.do",
//                    articleId,
//                    messagePostProcessor);
//
//            System.out.println("延迟消息，定时发布文章：" + new Date());
//        }

        //TODO使用AI检测文章内容
        //默认AI通过，直接进行人工审核
        this.updateArticleStatus(articleId,ArticleReviewStatus.WAITING_MANUAL.type);
    }
    @Transactional
    @Override
    public void updateArticleStatus(String articleId, Integer pendingStatus) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",articleId);
        //验证文章发布时间，如果小于等于当前时间，才能进行操作，确保只能审核立即发布文章
        if(pendingStatus.equals(3))
        criteria.andLessThanOrEqualTo("publishTime",new Date());

        Article article = new Article();
        //修改状态
        article.setArticleStatus(pendingStatus);
        int res = articleMapper.updateByExampleSelective(article,example);
        if (res != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        //如果审核通过，则查询artilce表，将相应字段信息存入es中
//        if (pendingStatus == ArticleReviewStatus.SUCCESS.type) {
//            Article result = articleMapper.selectByPrimaryKey(articleId);
//            //对立即发布文章，审核完毕立即存入es
//            if (result.getIsAppoint() == ArticleAppointType.IMMEDIATELY.type) {
//                ArticleEO articleEO = new ArticleEO();
//                //将article对象属性拷贝到eo中
//                BeanUtils.copyProperties(result,articleEO);
//                IndexQuery iq = new IndexQueryBuilder().withObject(articleEO).build();
//                //创建document
//                elasticsearchTemplate.index(iq);
//            }
//
//        }

    }

    @Transactional
    @Override
    public void updateAppointTopublish() {
        articleMapperCustom.updateAppointToPublish();
    }

    /**
     * 延迟队列收到消息后，更改mysql对应的一条文章记录，设置其appoint状态为0
     */
    @Transactional
    @Override
    public void updateArticleTopublish(String articleId) {
        Article article = new Article();
        article.setId(articleId);
        //修改文章为即时发布
        article.setIsAppoint(ArticleAppointType.IMMEDIATELY.type);
        articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public PagedGridResult queryMyArticleList(String userId,
                                              String keyword,
                                              Integer status,
                                              Date startDate,
                                              Date endDate,
                                              Integer page,
                                              Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId",userId);
        if (StringUtils.isNotBlank(keyword)) {
            //进行标题模糊查询
            criteria.andLike("title","%"+keyword+"%");
        }
        //判断状态是否有效
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus",status);
        }
        //文章状态1 2是审核中
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus",ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus",ArticleReviewStatus.WAITING_MANUAL.type);
        }
        //没有被逻辑删除的文章，文章还在数据库
        criteria.andEqualTo("isDelete",YesOrNo.NO.type);
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("publishTime",startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("publishTime",endDate);
        }
        //执行分页
        PageHelper.startPage(page,pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        return setterPagedGrid(list,page);
    }

    @Override
    public PagedGridResult queryAllArticleList(Integer status, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        //判断状态是否有效
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus",status);
        }
        //文章状态1 2是审核中
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus",ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus",ArticleReviewStatus.WAITING_MANUAL.type);
        }
        //没有被逻辑删除的文章，文章还在数据库
        criteria.andEqualTo("isDelete",YesOrNo.NO.type);
        //执行分页
        PageHelper.startPage(page,pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        return setterPagedGrid(list,page);
    }

    @Override
    @Transactional
    public void delete(String userId, String articleId) {
        Example articleExample = makeExampleCriteria(userId, articleId);

        Article pending = new Article();
        pending.setIsDelete(YesOrNo.YES.type);
        //在mysql中进行文章假删除操作
        int result = articleMapper.updateByExampleSelective(pending, articleExample);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
        //在es中进行delete操作
        //elasticsearchTemplate.delete(ArticleEO.class,articleId);
    }
    @Transactional
    @Override
    public void withdraw(String userId, String articleId) {
        Example articleExample = makeExampleCriteria(userId, articleId);
        Article pending = new Article();
        pending.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);
        int result = articleMapper.updateByExampleSelective(pending, articleExample);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
        //在es中进行delete操作
        //elasticsearchTemplate.delete(ArticleEO.class,articleId);
    }

    private Example makeExampleCriteria(String userId, String articleId) {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        criteria.andEqualTo("id", articleId);
        return articleExample;
    }
}
