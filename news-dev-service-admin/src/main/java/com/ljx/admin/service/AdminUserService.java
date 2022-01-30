package com.ljx.admin.service;

import com.ljx.pojo.AdminUser;
import com.ljx.pojo.bo.NewAdminBO;
import com.ljx.utils.PagedGridResult;

public interface AdminUserService {
    /**
     * 获得管理员用户信息
     */
    public AdminUser queryAdminByUsername(String username);

    /**
     * 创建新的管理员到数据库
     */
    public void createAdminUser(NewAdminBO newAdminBO);

    /**
     * 分页查询所有管理员列表
     */
    public PagedGridResult queryAdminList(Integer page, Integer pageSize);
}
