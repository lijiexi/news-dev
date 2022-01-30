package com.ljx.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "admin_user")
public class AdminUser {
    @Id
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 人脸入库图片信息，该信息保存到mongoDB的gridFS中
     */
    @Column(name = "face_id")
    private String faceId;

    /**
     * 管理人员的姓名
     */
    @Column(name = "admin_name")
    private String adminName;

    /**
     * 创建时间 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取人脸入库图片信息，该信息保存到mongoDB的gridFS中
     *
     * @return face_id - 人脸入库图片信息，该信息保存到mongoDB的gridFS中
     */
    public String getFaceId() {
        return faceId;
    }

    /**
     * 设置人脸入库图片信息，该信息保存到mongoDB的gridFS中
     *
     * @param faceId 人脸入库图片信息，该信息保存到mongoDB的gridFS中
     */
    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    /**
     * 获取管理人员的姓名
     *
     * @return admin_name - 管理人员的姓名
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * 设置管理人员的姓名
     *
     * @param adminName 管理人员的姓名
     */
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    /**
     * 获取创建时间 创建时间
     *
     * @return created_time - 创建时间 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间 创建时间
     *
     * @param createdTime 创建时间 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取更新时间 更新时间
     *
     * @return updated_time - 更新时间 更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置更新时间 更新时间
     *
     * @param updatedTime 更新时间 更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}