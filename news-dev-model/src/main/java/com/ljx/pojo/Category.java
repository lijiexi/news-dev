package com.ljx.pojo;

import javax.persistence.Column;
import javax.persistence.Id;

public class Category {
    @Id
    private Integer id;

    /**
     * 分类名，比如：科技，人文，历史，汽车等等
     */
    private String name;

    /**
     * 标签颜色
     */
    @Column(name = "tag_color")
    private String tagColor;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取分类名，比如：科技，人文，历史，汽车等等
     *
     * @return name - 分类名，比如：科技，人文，历史，汽车等等
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类名，比如：科技，人文，历史，汽车等等
     *
     * @param name 分类名，比如：科技，人文，历史，汽车等等
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取标签颜色
     *
     * @return tag_color - 标签颜色
     */
    public String getTagColor() {
        return tagColor;
    }

    /**
     * 设置标签颜色
     *
     * @param tagColor 标签颜色
     */
    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }
}