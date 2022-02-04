package com.ljx.search.controller;

import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.search.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
    /**
     * 测试在es中添加文档
     */
    @GetMapping("addDoc")
    public Object addDoc(){
        Stu stu = new Stu();
        stu.setStuId(1001l);
        stu.setAge(15);
        stu.setName("小明");
        stu.setMoney(1000f);
        stu.setDesc("一个喜欢染发烫头的好孩子");
        IndexQuery query = new IndexQueryBuilder().withObject(stu).build();
        elasticsearchTemplate.index(query);
        return GraceJSONResult.ok();
    }

    /**
     * 测试在es中添加文档
     */
    @GetMapping("updatedoc")
    public Object updatedoc(){

        IndexRequest indexRequest = new IndexRequest();
        Map<String,Object> updateMap = new HashMap<>();
        updateMap.put("desc","hello world es");
        updateMap.put("age",111);
        //修改es中的source
        indexRequest.source(updateMap);
        //使用updatequery修改文档数据
        UpdateQuery updateQuery = new UpdateQueryBuilder()
                                     .withClass(Stu.class)
                                     .withId("1001")   //文档主键
                                     .withIndexRequest(indexRequest)
                                     .build();


        elasticsearchTemplate.update(updateQuery);
        return GraceJSONResult.ok();
    }

    /**
     * 查询es中的文档
     */
    @GetMapping("getDoc")
    public Object getDoc(String id){

        GetQuery getQuery = new GetQuery();
        getQuery.setId(id);
        Stu  stu = elasticsearchTemplate.queryForObject(getQuery,Stu.class);
        return GraceJSONResult.ok(stu);
    }

    /**
     * 删除es中的文档
     */
    @GetMapping("deleteDoc")
    public Object deleteDoc(String id){
        //删除对应id文档
        elasticsearchTemplate.delete(Stu.class,id);
        return GraceJSONResult.ok();
    }
}
