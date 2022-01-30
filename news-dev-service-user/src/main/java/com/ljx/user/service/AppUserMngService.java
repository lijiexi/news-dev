package com.ljx.user.service;

import com.ljx.pojo.AppUser;
import com.ljx.utils.PagedGridResult;

import java.util.Date;

public interface AppUserMngService {
    /**
     *管理员端，查询所有用户
     */
    public PagedGridResult queryAllUserList(String nickname,
                                            Integer status,
                                            Date startDate,
                                            Date endDate,
                                            Integer page,
                                            Integer pageSize);

}
