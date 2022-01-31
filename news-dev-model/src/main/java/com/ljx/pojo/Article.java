package com.ljx.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class Article {
    @Id
    private String id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章所属分类id
     */
    @Column(name = "category_id")
    private Integer categoryId;

    /**
     * 文章类型，1：图文（1张封面），2：纯文字
     */
    @Column(name = "article_type")
    private Integer articleType;

    /**
     * 文章封面图，article_type=1 的时候展示
     */
    @Column(name = "article_cover")
    private String articleCover;

    /**
     * 是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布    在预约时间到点的时候，把1改为0，则发布
     */
    @Column(name = "is_appoint")
    private Integer isAppoint;

    /**
     * 文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
     */
    @Column(name = "article_status")
    private Integer articleStatus;

    /**
     * 发布者用户id
     */
    @Column(name = "publish_user_id")
    private String publishUserId;

    /**
     * 文章发布时间（也是预约发布的时间）
     */
    @Column(name = "publish_time")
    private Date publishTime;

    /**
     * 用户累计点击阅读数（喜欢数）（点赞） - 放redis
     */
    @Column(name = "read_counts")
    private Integer readCounts;

    /**
     * 文章评论总数。评论防刷，距离上次评论需要间隔时间控制几秒
     */
    @Column(name = "comment_counts")
    private Integer commentCounts;

    @Column(name = "mongo_file_id")
    private String mongoFileId;

    /**
     * 逻辑删除状态，非物理删除，1：删除，0：未删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 文章的创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 文章的修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 文章内容，长度不超过9999，需要在前后端判断
     */
    private String content;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取文章标题
     *
     * @return title - 文章标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置文章标题
     *
     * @param title 文章标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取文章所属分类id
     *
     * @return category_id - 文章所属分类id
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 设置文章所属分类id
     *
     * @param categoryId 文章所属分类id
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取文章类型，1：图文（1张封面），2：纯文字
     *
     * @return article_type - 文章类型，1：图文（1张封面），2：纯文字
     */
    public Integer getArticleType() {
        return articleType;
    }

    /**
     * 设置文章类型，1：图文（1张封面），2：纯文字
     *
     * @param articleType 文章类型，1：图文（1张封面），2：纯文字
     */
    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    /**
     * 获取文章封面图，article_type=1 的时候展示
     *
     * @return article_cover - 文章封面图，article_type=1 的时候展示
     */
    public String getArticleCover() {
        return articleCover;
    }

    /**
     * 设置文章封面图，article_type=1 的时候展示
     *
     * @param articleCover 文章封面图，article_type=1 的时候展示
     */
    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    /**
     * 获取是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布    在预约时间到点的时候，把1改为0，则发布
     *
     * @return is_appoint - 是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布    在预约时间到点的时候，把1改为0，则发布
     */
    public Integer getIsAppoint() {
        return isAppoint;
    }

    /**
     * 设置是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布    在预约时间到点的时候，把1改为0，则发布
     *
     * @param isAppoint 是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布    在预约时间到点的时候，把1改为0，则发布
     */
    public void setIsAppoint(Integer isAppoint) {
        this.isAppoint = isAppoint;
    }

    /**
     * 获取文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
     *
     * @return article_status - 文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
     */
    public Integer getArticleStatus() {
        return articleStatus;
    }

    /**
     * 设置文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
     *
     * @param articleStatus 文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
     */
    public void setArticleStatus(Integer articleStatus) {
        this.articleStatus = articleStatus;
    }

    /**
     * 获取发布者用户id
     *
     * @return publish_user_id - 发布者用户id
     */
    public String getPublishUserId() {
        return publishUserId;
    }

    /**
     * 设置发布者用户id
     *
     * @param publishUserId 发布者用户id
     */
    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    /**
     * 获取文章发布时间（也是预约发布的时间）
     *
     * @return publish_time - 文章发布时间（也是预约发布的时间）
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * 设置文章发布时间（也是预约发布的时间）
     *
     * @param publishTime 文章发布时间（也是预约发布的时间）
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 获取用户累计点击阅读数（喜欢数）（点赞） - 放redis
     *
     * @return read_counts - 用户累计点击阅读数（喜欢数）（点赞） - 放redis
     */
    public Integer getReadCounts() {
        return readCounts;
    }

    /**
     * 设置用户累计点击阅读数（喜欢数）（点赞） - 放redis
     *
     * @param readCounts 用户累计点击阅读数（喜欢数）（点赞） - 放redis
     */
    public void setReadCounts(Integer readCounts) {
        this.readCounts = readCounts;
    }

    /**
     * 获取文章评论总数。评论防刷，距离上次评论需要间隔时间控制几秒
     *
     * @return comment_counts - 文章评论总数。评论防刷，距离上次评论需要间隔时间控制几秒
     */
    public Integer getCommentCounts() {
        return commentCounts;
    }

    /**
     * 设置文章评论总数。评论防刷，距离上次评论需要间隔时间控制几秒
     *
     * @param commentCounts 文章评论总数。评论防刷，距离上次评论需要间隔时间控制几秒
     */
    public void setCommentCounts(Integer commentCounts) {
        this.commentCounts = commentCounts;
    }

    /**
     * @return mongo_file_id
     */
    public String getMongoFileId() {
        return mongoFileId;
    }

    /**
     * @param mongoFileId
     */
    public void setMongoFileId(String mongoFileId) {
        this.mongoFileId = mongoFileId;
    }

    /**
     * 获取逻辑删除状态，非物理删除，1：删除，0：未删除
     *
     * @return is_delete - 逻辑删除状态，非物理删除，1：删除，0：未删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置逻辑删除状态，非物理删除，1：删除，0：未删除
     *
     * @param isDelete 逻辑删除状态，非物理删除，1：删除，0：未删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取文章的创建时间
     *
     * @return create_time - 文章的创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置文章的创建时间
     *
     * @param createTime 文章的创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取文章的修改时间
     *
     * @return update_time - 文章的修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置文章的修改时间
     *
     * @param updateTime 文章的修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取文章内容，长度不超过9999，需要在前后端判断
     *
     * @return content - 文章内容，长度不超过9999，需要在前后端判断
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置文章内容，长度不超过9999，需要在前后端判断
     *
     * @param content 文章内容，长度不超过9999，需要在前后端判断
     */
    public void setContent(String content) {
        this.content = content;
    }
}