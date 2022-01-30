package com.ljx.admin.service.impl;

import com.ljx.admin.mapper.AdminUserMapper;
import com.ljx.admin.service.AdminUserService;
import com.ljx.pojo.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    public AdminUserMapper adminUserMapper;

    @Override
    public AdminUser queryAdminByUsername(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username",username);
        AdminUser admin = adminUserMapper.selectOneByExample(adminExample);
        return admin;
    }
}
