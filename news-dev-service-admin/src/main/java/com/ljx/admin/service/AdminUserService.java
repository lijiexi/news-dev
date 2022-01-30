package com.ljx.admin.service;

import com.ljx.pojo.AdminUser;

public interface AdminUserService {
    /**
     * 获得管理员用户信息
     */
    public AdminUser queryAdminByUsername(String username);

}
