package com.ljx.user.service;

import com.ljx.pojo.AppUser;
import com.ljx.pojo.bo.UpdateUserInfoBO;

public interface UserService {
    /**
     *判断用户是否存在，如果存在返回user信息
     */
    public AppUser queryMobileIsExist(String mobile);
    /**
     * check user is exist
     */
    public AppUser queryEmailIsExist(String mobile);
    /**
     * 创建用户，新增用户记录到数据库
     */
    public AppUser createUser(String mobile);
    /**
     * create a new user by email
     */
    public AppUser createUserByEmail(String email);
    /**
     * 查询用户信息,根据用户主键id
     */
    public AppUser getUser(String userId);
    /**
     *根据BO对象更新用户信息
     */
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);
}
