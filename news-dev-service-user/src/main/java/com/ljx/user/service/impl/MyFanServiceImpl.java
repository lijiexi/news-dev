package com.ljx.user.service.impl;

import com.ljx.api.service.BaseService;
import com.ljx.enums.Sex;
import com.ljx.enums.UserStatus;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AppUser;
import com.ljx.pojo.Fans;
import com.ljx.pojo.bo.UpdateUserInfoBO;
import com.ljx.user.mapper.AppUserMapper;
import com.ljx.user.mapper.FansMapper;
import com.ljx.user.service.MyFansService;
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
public class MyFanServiceImpl extends BaseService implements MyFansService {
    @Autowired
    private FansMapper fansMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private Sid sid;
    @Override
    public boolean isMeFollowThisWriter(String writerId, String fanId) {
        Fans fan = new Fans();
        fan.setFanId(fanId);
        fan.setWriterId(writerId);
        //查询mysql数据库
        int count = fansMapper.selectCount(fan);
        return count > 0? true:false;
    }

    @Override
    @Transactional
    public void follow(String writerId, String fanId) {
        //获得粉丝用户信息
        AppUser fanInfo = userService.getUser(fanId);
        String fanPkId = sid.nextShort();
        Fans fan = new Fans();
        fan.setId(fanPkId);
        fan.setFanId(fanId);
        fan.setWriterId(writerId);

        //设置冗余信息
        fan.setFace(fanInfo.getFace());
        fan.setFanNickname(fanInfo.getNickname());
        fan.setSex(fanInfo.getSex());
        fan.setProvince(fanInfo.getProvince());

        //保存到fan表
        fansMapper.insert(fan);
        //将粉丝数量、关注数量在redis中进行累加
        //Redis作家粉丝数累加
        redis.increment(REDIS_WRITER_FANS_COUNTS+":"+writerId,1);
        //Redis自己关注人数累加
        redis.increment(REDIS_MY_FOLLOW_COUNTS+":"+fanId,1);
    }

    @Override
    @Transactional
    public void unfollow(String writerId, String fanId) {
        Fans fan = new Fans();
        fan.setWriterId(writerId);
        fan.setFanId(fanId);
        fansMapper.delete(fan);
        //Redis作家粉丝数减少
        redis.decrement(REDIS_WRITER_FANS_COUNTS+":"+writerId,1);
        //Redis自己关注人数减少
        redis.decrement(REDIS_MY_FOLLOW_COUNTS+":"+fanId,1);
    }
}
