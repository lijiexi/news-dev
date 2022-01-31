package com.ljx.api.interceptors;

import com.ljx.enums.UserStatus;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AppUser;
import com.ljx.utils.JsonUtils;
import com.ljx.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器 拦截用未激活用户，不能发表文章、评论等操作
 */
public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {


    /**
     * 在进入controller之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从redis中获取信息
        String userId = request.getHeader("headerUserId");
        String userJson = redis.get(REDIS_USER_INFO+":"+userId);
        AppUser user = null;
        //查询判断redis是否包含用户信息，包含则直接返回
        if(StringUtils.isNotBlank(userJson)) {
            user = JsonUtils.jsonToPojo(userJson,AppUser.class);
        }else{
            //redis没有信息，则表明用户未登录或者被封禁后删除信息
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        if(user.getActiveStatus() == null|| user.getActiveStatus() != UserStatus.ACTIVE.type){
            GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
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
