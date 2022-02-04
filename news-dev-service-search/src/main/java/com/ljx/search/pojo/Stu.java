package com.ljx.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * 测试es中的stu索引,类似于数据库表
 */
@Document(indexName = "stu",type = "_doc")
public class Stu {
    @Id
    private Long StuId;
    @Field
    private String name;
    @Field
    private Integer age;
    @Field
    private float money;
    @Field
    private String desc;

    public Long getStuId() {
        return StuId;
    }

    public void setStuId(Long stuId) {
        StuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "StuId=" + StuId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", desc='" + desc + '\'' +
                '}';
    }
}
