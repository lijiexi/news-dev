package com.ljx.api.interceptors;

import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseInterceptor {
    @Autowired
    public RedisOperator redis;
    public  static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";
    public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";


    public boolean verifyUserIdToken(String id,String token,
                                     String redisKeyPrefix){
        if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(token)){
            String redisToken = redis.get(redisKeyPrefix+":"+id);
            if(StringUtils.isBlank(redisToken)){
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            }else{
                if(!token.equalsIgnoreCase(token)){
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }
        }else{
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        
        return true;
    }
}
