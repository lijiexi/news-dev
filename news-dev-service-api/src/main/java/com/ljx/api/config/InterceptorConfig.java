package com.ljx.api.config;

import com.ljx.api.interceptors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public PassportInterceptor passportInterceptor(){
        return new PassportInterceptor();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor(){
        return new UserTokenInterceptor();
    }

    /**
     * 用户状态拦截器，拦截为激活、被封禁的用户
     * 用户被封禁后，会从redis删除状态信息
     * 此时查询不到状态信息则进行拦截
     */
    @Bean
    public UserActiveInterceptor userActiveInterceptor(){
        return new UserActiveInterceptor();
    }

    //管理员拦截器
    @Bean
    public AdminTokenInterceptor adminTokenInterceptor(){return new AdminTokenInterceptor();}

    //文章阅读量拦截器
    @Bean
    public ArticleReadInterceptor articleReadInterceptor(){return new ArticleReadInterceptor();}


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 防止相同ip频繁获取验证码
         */
        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/passport/getSMSCode");
        /**
         * 注册登录验证拦截器
         */
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo")
                .addPathPatterns("/fans/follow")
                .addPathPatterns("/fans/unfollow")
                .addPathPatterns("/fs/uploadFace")
                .addPathPatterns("/fs/uploadSomeFiles");

        /**
         * 注册激活验证拦截器
         */
        registry.addInterceptor(userActiveInterceptor())
                .addPathPatterns("/fans/follow")
                .addPathPatterns("/fans/unfollow")
                .addPathPatterns("/fs/uploadSomeFiles");
        /**
         * 管理员状态验证拦截器
         */
        registry.addInterceptor(adminTokenInterceptor())
                .addPathPatterns("/categoryMng/getCatList")
                .addPathPatterns("/categoryMng/saveOrUpdateCategory")
                .addPathPatterns("/adminMng/adminIsExist")
                .addPathPatterns("/adminMng/getAdminList")
                .addPathPatterns("/adminMng/addNewAdmin");
        /**
         * 文章阅读量拦截器
         */
        registry.addInterceptor(articleReadInterceptor())
                .addPathPatterns("/portal/article/readArticle");
    }
}
