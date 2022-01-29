package com.ljx.user.service.impl;

import com.ljx.enums.Sex;
import com.ljx.enums.UserStatus;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AppUser;
import com.ljx.pojo.bo.UpdateUserInfoBO;
import com.ljx.user.mapper.AppUserMapper;
import com.ljx.user.service.UserService;
import com.ljx.utils.DateUtil;
import com.ljx.utils.DesensitizationUtil;
import com.ljx.utils.JsonUtils;
import com.ljx.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    //继承mymapper包括基本crud
    @Autowired
    public AppUserMapper appUserMapper;

    @Autowired
    public Sid sid;
    @Autowired
    public RedisOperator redis;
    //用户默认头像
    private static final String USER_FACE0 = "https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/IMG_1516.JPG";

    public static final String REDIS_USER_INFO = "redis_user_info";

    @Override
    public AppUser queryMobileIsExist(String mobile) {
        //AppUser是查询的实例
        Example userExample = new Example(AppUser.class);
        //构造查询条件
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("mobile",mobile);
        AppUser user = appUserMapper.selectOneByExample(userExample);
        return user;
    }

    @Transactional
    @Override
    public AppUser createUser(String mobile) {
        String userId = sid.nextShort();
        AppUser user = new AppUser();
        //分布式环境中，使用全局id
        user.setId(userId);
        user.setMobile(mobile);
        user.setNickname("用户:"+ DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE0);
        user.setSex(Sex.secret.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);
        user.setTotalIncome(0);
        user.setBirthday(DateUtil.stringToDate("1999-02-14"));
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        appUserMapper.insert(user);
        return user;
    }

    @Override
    public AppUser getUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        String userId = updateUserInfoBO.getId();
        //保证双写数据一致，先删除redis，后更新mysql
        redis.del(REDIS_USER_INFO+":"+userId);
        AppUser userInfo = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO,userInfo);

        userInfo.setUpdatedTime(new Date());
        userInfo.setActiveStatus(UserStatus.ACTIVE.type);
        //只会更新现有数据，忽略空数据
        int res = appUserMapper.updateByPrimaryKeySelective(userInfo);
        if(res != 1){
            //不用回传到controller，实现解耦
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        //再次查询用户最新信息，放入redis
        AppUser user = getUser(userId);
        redis.set(REDIS_USER_INFO+":"+userId, JsonUtils.objectToJson(user));
        //缓存双删
        try {
            Thread.sleep(100);
            redis.del(REDIS_USER_INFO+":"+userId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
