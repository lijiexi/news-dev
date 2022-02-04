package com.ljx.search.controller;

import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.search.pojo.Stu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @RequestMapping("hello")
    public Object hello(){

        return GraceJSONResult.ok();
    }

    /**
     * 测试在es中创建索引
     */
    @GetMapping("createIndex")
    public Object createIndex(){
        //从类中创建索引
        elasticsearchTemplate.createIndex(Stu.class);
        return GraceJSONResult.ok();
    }
    /**
     * 测试在es中删除索引
     */
    @GetMapping("deleteIndex")
    public Object deleteIndex(){
        //从类中创建索引
        elasticsearchTemplate.deleteIndex(Stu.class);
        return GraceJSONResult.ok();
    }
}
