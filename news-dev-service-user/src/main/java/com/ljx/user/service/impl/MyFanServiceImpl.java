package com.ljx.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.ljx.api.service.BaseService;
import com.ljx.enums.Sex;
import com.ljx.enums.UserStatus;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AppUser;
import com.ljx.pojo.Fans;
import com.ljx.pojo.bo.UpdateUserInfoBO;
import com.ljx.pojo.vo.RegionRatioVO;
import com.ljx.user.mapper.AppUserMapper;
import com.ljx.user.mapper.FansMapper;
import com.ljx.user.service.MyFansService;
import com.ljx.user.service.UserService;
import com.ljx.utils.*;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public PagedGridResult queryMyFansList(String writerId,
                                           Integer page,
                                           Integer pageSize) {
        Fans fan = new Fans();
        fan.setWriterId(writerId);
        PageHelper.startPage(page,pageSize);
        List<Fans> list = fansMapper.select(fan);
        return setterPagedGrid(list,page);
    }

    @Override
    public Integer queryFansCounts(String writerId, Sex sex) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(sex.type);
        int count = fansMapper.selectCount(fans);
        return count;
    }
    public static final String[] regions = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
            "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};

    @Override
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);

        List<RegionRatioVO> list = new ArrayList<>();
        for (String r : regions) {
            fans.setProvince(r);
            //现在使用mysql，后期可以进行升级
            Integer count = fansMapper.selectCount(fans);

            RegionRatioVO regionRatioVO = new RegionRatioVO();
            regionRatioVO.setName(r);
            regionRatioVO.setValue(count);

            list.add(regionRatioVO);
        }

        return list;
    }
}
