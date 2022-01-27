package com.ljx.api.interceptors;

import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.utils.IPUtil;
import com.ljx.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器 拦截ip，确保60s发送一次验证码
 */
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    public RedisOperator redis;

    public static final String MOBILE_SMSCODE = "mobile:smscode";

    /**
     * 在进入controller之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获得用户ip
        String userIp = IPUtil.getRequestIp(request);

        boolean keyIsExit = redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp);

        if(keyIsExit){
            //System.out.println("频率过快");
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
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
