package com.ljx.user.service;

import com.ljx.utils.PagedGridResult;

import java.util.Date;

public interface MyFansService {
    /**
     * 查询用户是否关注作家
     */
    public boolean isMeFollowThisWriter(String writerId, String fanId);

    /**
     * 关注成为粉丝
     */
    public void follow(String writerId, String fanId);
    /**
     * 用户取关作家
     */
    public void unfollow(String writerId, String fanId);
}
