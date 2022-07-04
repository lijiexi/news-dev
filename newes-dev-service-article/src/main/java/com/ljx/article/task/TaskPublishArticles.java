package com.ljx.article.task;

import com.ljx.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


/**
 * 文章发布定时任务
 */
@Configuration      //1.标记配置类，使springboot扫描
@EnableScheduling   //2.开启定时任务
public class TaskPublishArticles {
    @Autowired
    private ArticleService articleService;
    //添加定时任务，和其执行任务表达式
    //@Scheduled(cron = "0/5 * * * * ？*")
    @Scheduled(cron = "0 0 0/2 * * ? ")
    private void publishArticles () {
        //System.out.println("----test----"+ LocalDateTime.now());
        //调用自定义文章service，将当前时间该发布的文章类型，改为即时发布
        articleService.updateAppointTopublish();
    }
}
