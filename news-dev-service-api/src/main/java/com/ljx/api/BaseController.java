package com.ljx.api;


import com.ljx.utils.RedisOperator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义redis和用户ip地址
 */
public class BaseController {
    @Autowired
    public RedisOperator redis;

    public static final String MOBILE_SMSCODE = "mobile:smscode";

    public  static final String REDIS_USER_TOKEN = "redis_user_token";
    public  static final String REDIS_ADMIN_TOKEN = "redis_admin_token";

    public  static final String REDIS_ALL_CATEGORY = "redis_all_category";
    public static final Integer COOKIE_MONTH = 30*24*60*60;
    public static final Integer COOKIE_DELETE = 0;
    public static final String REDIS_USER_INFO = "redis_user_info";
    public static final Integer COMMON_START_PAGE = 1;
    public static final Integer COMMON_PAGESIZE = 10;

    public static final String REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";
    public static final String REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";

    @Value("${website.domain-name}")
    public String DOMAIN_NAME;

    /**
     * 验证BO(前端返回的对象，是否为空)中的错误信息，并且获取
     */
    public Map<String,String> getErrors(BindingResult result) {
        Map<String,String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for(FieldError error : errorList){
            //发生验证错误时，对应的某个属性
            String field = error.getField();
            //验证的错误消息
            String msg = error.getDefaultMessage();
            map.put(field,msg);
        }
        return map;
    }

    //设置浏览器cookie信息
    public void setCookie(HttpServletRequest request,
                          HttpServletResponse response,
                          String cookieName,
                          String cookieValue,
                          Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue,"utf-8");
            setCookieValue(request,response,cookieName,cookieValue,maxAge);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void setCookieValue(HttpServletRequest request,
                          HttpServletResponse response,
                          String cookieName,
                          String cookieValue,
                          Integer maxAge) {
            Cookie cookie = new Cookie(cookieName,cookieValue);
            cookie.setMaxAge(maxAge);
            cookie.setDomain(DOMAIN_NAME);
            cookie.setPath("/");
            response.addCookie(cookie);
    }
    //删除cookie信息
    public void deleteCookie(HttpServletRequest request,
                             HttpServletResponse response,
                             String cookieName) {
        try {
            String deleteValue = URLEncoder.encode("","utf-8");
            setCookieValue(request,response,cookieName,deleteValue,COOKIE_DELETE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public Integer getCountsFromRedis(String key) {
        String countsStr = redis.get(key);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return  Integer.valueOf(countsStr);
    }

}
