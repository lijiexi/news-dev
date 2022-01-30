package com.ljx.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljx.admin.mapper.AdminUserMapper;
import com.ljx.admin.service.AdminUserService;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AdminUser;
import com.ljx.pojo.bo.NewAdminBO;
import com.ljx.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    public AdminUserMapper adminUserMapper;
    @Autowired
    private Sid sid;

    @Override
    public AdminUser queryAdminByUsername(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username",username);
        AdminUser admin = adminUserMapper.selectOneByExample(adminExample);
        return admin;
    }

    @Override
    @Transactional
    public void createAdminUser(NewAdminBO newAdminBO) {
        String adminId = sid.nextShort();
        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(newAdminBO.getUsername());
        adminUser.setAdminName(newAdminBO.getAdminName());
        //密码不为空，则加密入库
        if(StringUtils.isNotBlank(newAdminBO.getPassword())){
            String pwd = BCrypt.hashpw(newAdminBO.getPassword(),BCrypt.gensalt());
            adminUser.setPassword(pwd);
        }
//        if(StringUtils.isNotBlank(newAdminBO.getFaceId()))
//        adminUser.setFaceId(newAdminBO.getFaceId());
        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());
        int res = adminUserMapper.insert(adminUser);
        if(res != 1) {
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }

    }

    @Override
    public PagedGridResult queryAdminList(Integer page, Integer pageSize) {
        Example adminExample = new Example(AdminUser.class);
        adminExample.orderBy("createdTime").desc();
        //开启分页
        PageHelper.startPage(page,pageSize);
        List<AdminUser> adminUserList = adminUserMapper.selectByExample(adminExample);
        //System.out.println(adminUserList);
        return setterPagedGrid(adminUserList,page);
    }
    private PagedGridResult setterPagedGrid(List<?> adminUserList,
                                            Integer page) {
        PageInfo<?> pageList = new PageInfo<>(adminUserList);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(adminUserList);
        gridResult.setPage(page);
        //设置总页数
        gridResult.setRecords(pageList.getPages());
        //设置总记录数 count(0) 查询内容
        gridResult.setTotal(pageList.getTotal());
        return gridResult;
    }
}
