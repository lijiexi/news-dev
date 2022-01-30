package com.ljx.api.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器 拦截adminToken，验证id和token，确保是管理员状态
 */
public class AdminTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {

    /**
     * 在进入controller之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("adminUserId");
        String userToken = request.getHeader("adminUserToken");

        //判断是否放行
        boolean run = verifyUserIdToken(userId,userToken,REDIS_ADMIN_TOKEN);
        /**
         * return false:请求被拦截
         * return true：请求放行
         */
        return run;
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
