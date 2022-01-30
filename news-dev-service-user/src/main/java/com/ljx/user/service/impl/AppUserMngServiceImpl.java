package com.ljx.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.ljx.api.service.BaseService;
import com.ljx.enums.Sex;
import com.ljx.enums.UserStatus;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AppUser;
import com.ljx.pojo.bo.UpdateUserInfoBO;
import com.ljx.user.mapper.AppUserMapper;
import com.ljx.user.service.AppUserMngService;
import com.ljx.user.service.UserService;
import com.ljx.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AppUserMngServiceImpl extends BaseService implements AppUserMngService {
    @Autowired
    public  AppUserMapper appUserMapper;
    @Override
    public PagedGridResult queryAllUserList(String nickname,
                                            Integer status,
                                            Date startDate,
                                            Date endDate,
                                            Integer page,
                                            Integer pageSize) {
        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        //构建条件
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(nickname)) {
            //模糊查询
            criteria.andLike("nickname","%"+nickname+"%");
        }
        if(UserStatus.isUserStatusValid(status)) {
            criteria.andEqualTo("activeStatus",status);

        }
        if(startDate != null) {
            criteria.andGreaterThanOrEqualTo("createdTime",startDate);
        }
        if(endDate != null) {
            criteria.andLessThanOrEqualTo("createdTime",endDate);
        }
        PageHelper.startPage(page,pageSize);
        List<AppUser> list = appUserMapper.selectByExample(example);
        return setterPagedGrid(list,page);
    }
}
