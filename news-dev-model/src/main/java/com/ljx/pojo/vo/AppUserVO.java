package com.ljx.pojo.vo;

import java.util.Date;

/**
 * vo：传给前端的视图对象，精简从数据库的查询，删掉无用、敏感信息
 */
public class AppUserVO {
    private String id;


    /**
     * 昵称，媒体号
     */
    private String nickname;

    /**
     * 头像
     */
    private String face;
    /**
     * 激活状态
     */
    private Integer activeStatus;

    public Integer getMyFollowCounts() {
        return myFollowCounts;
    }

    public void setMyFollowCounts(Integer myFollowCounts) {
        this.myFollowCounts = myFollowCounts;
    }

    public Integer getMyFansCounts() {
        return myFansCounts;
    }

    public void setMyFansCounts(Integer myFansCounts) {
        this.myFansCounts = myFansCounts;
    }

    /**
     * 关注数
     */
    private Integer myFollowCounts;
    /**
     * 粉丝数
     */
    private Integer myFansCounts;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }
}