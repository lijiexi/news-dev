package com.ljx.api.interceptors;

import com.ljx.utils.IPUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器 确保每个ip只会让文章阅读量增加一次
 */
public class ArticleReadInterceptor extends BaseInterceptor implements HandlerInterceptor {
    public static final String REDIS_ALREADY_READ = "redis_already_read";


    /**
     * 在进入controller之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIp = IPUtil.getRequestIp(request);
        String articleId = request.getParameter("articleId");
        //设置针对当前用户id永久存在的key，存入到redis，表示当前ip的用户已经阅读过，无法累加阅读量
        boolean isExist = redis.keyIsExist(REDIS_ALREADY_READ+":"+articleId+":"+userIp);

        if (isExist) {
            return false;
        }
        /**
         * return false:请求被拦截
         * return true：请求放行
         */
        return true;
    }

    /**
     * 在请求访问到controller后，渲染视图之前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * controller之后，渲染视图之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
