package com.ljx.user.service;

import com.ljx.enums.Sex;
import com.ljx.pojo.vo.FansCountsVO;
import com.ljx.pojo.vo.RegionRatioVO;
import com.ljx.utils.PagedGridResult;

import java.util.Date;
import java.util.List;

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
    /**
     * 查询用户所有粉丝
     */
    public PagedGridResult queryMyFansList(String writerId,
                                           Integer page,
                                           Integer pageSize);

    /**
     * 从es中查询用户所有粉丝
     */
    public PagedGridResult queryMyFansESList(String writerId,
                                           Integer page,
                                           Integer pageSize);
    /**
     * 查询粉丝数
     */
    public Integer queryFansCounts(String writerId, Sex sex);
    /**
     * 从ES中查询粉丝数
     */
    public FansCountsVO queryFansESCounts(String writerId);

    /**
     * 根据地域查询粉丝
     */
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId);
    /**
     * 从ES中国根据地域查询粉丝
     */
    public List<RegionRatioVO> queryRegionRatioESCounts(String writerId);
    /**
     * 基于ES的粉丝信息被动更新
     */
    public void forceUpdateFanInfo(String relationId, String fanId);
}
